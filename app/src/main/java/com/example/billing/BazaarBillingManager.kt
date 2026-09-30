package com.example.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.ConnectionState
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import ir.cafebazaar.poolakey.entity.PurchaseInfo
import ir.cafebazaar.poolakey.request.PurchaseRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * مدیر خرید درون‌برنامه‌ای کافه‌بازار
 * با استفاده از کتابخانه Poolakey
 */
class BazaarBillingManager(private val context: Context) {

    companion object {
        private const val TAG = "BazaarBilling"

        /** لیست تمام SKUهای VIP */
        val ALL_VIP_SKUS = listOf(
            BazaarConfig.SKU_VIP_MONTHLY,
            BazaarConfig.SKU_VIP_QUARTERLY,
            BazaarConfig.SKU_VIP_YEARLY,
            BazaarConfig.SKU_VIP_LIFETIME
        )
    }

    private var payment: Payment? = null
    private var connection: Connection? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var isConnected = false

    /**
     * اتصال به سرویس کافه‌بازار
     */
    fun connect(
        onConnected: () -> Unit = {},
        onDisconnected: () -> Unit = {},
        onFailed: (Throwable) -> Unit = {}
    ) {
        try {
            val config = PaymentConfiguration(
                localSecurityCheck = SecurityCheck.Disable,
                remoteSecurityCheck = SecurityCheck.Enable(BazaarConfig.RSA_PUBLIC_KEY)
            )

            payment = Payment(context, config)
            connection = payment?.connect()

            connection?.connectionState()?.observeForever { state ->
                when (state) {
                    is ConnectionState.Connected -> {
                        Log.d(TAG, "Connected to Bazaar")
                        isConnected = true
                        onConnected()
                    }
                    is ConnectionState.Disconnected -> {
                        Log.d(TAG, "Disconnected from Bazaar")
                        isConnected = false
                        onDisconnected()
                    }
                    is ConnectionState.Failed -> {
                        Log.e(TAG, "Connection failed: ${state.throwable.message}")
                        isConnected = false
                        onFailed(state.throwable)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Connection exception: ${e.message}")
            onFailed(e)
        }
    }

    /**
     * قطع اتصال
     */
    fun disconnect() {
        try {
            connection?.disconnect()
            isConnected = false
        } catch (e: Exception) {
            Log.e(TAG, "Disconnect error: ${e.message}")
        }
    }

    /**
     * خرید محصول VIP
     */
    fun purchase(
        activity: Activity,
        sku: String,
        onSuccess: (PurchaseInfo) -> Unit,
        onFailed: (Throwable) -> Unit,
        onCanceled: () -> Unit = {}
    ) {
        if (!isConnected) {
            onFailed(Exception("Bazaar not connected"))
            return
        }

        scope.launch {
            try {
                val request = PurchaseRequest(
                    productId = sku,
                    payload = "studio_taraneh_vip"
                )

                val result = payment?.purchaseProduct(activity, request)

                result?.fold(
                    onSuccess = { purchaseInfo ->
                        Log.d(TAG, "Purchase successful: ${purchaseInfo.orderId}")
                        verifyPurchase(sku, onSuccess, onFailed)
                    },
                    onFailure = { throwable ->
                        Log.e(TAG, "Purchase failed: ${throwable.message}")
                        if (throwable.message?.contains("cancel", ignoreCase = true) == true) {
                            onCanceled()
                        } else {
                            onFailed(throwable)
                        }
                    },
                    onCanceled = {
                        Log.d(TAG, "Purchase canceled by user")
                        onCanceled()
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Purchase exception: ${e.message}")
                onFailed(e)
            }
        }
    }

    /**
     * بررسی اعتبار خرید
     */
    private fun verifyPurchase(
        sku: String,
        onSuccess: (PurchaseInfo) -> Unit,
        onFailed: (Throwable) -> Unit
    ) {
        scope.launch {
            try {
                val result = payment?.getPurchasedProducts()
                result?.fold(
                    onSuccess = { purchasedProducts ->
                        val isPurchased = purchasedProducts.any { it.productId == sku }
                        if (isPurchased) {
                            onSuccess(purchasedProducts.first { it.productId == sku })
                        } else {
                            onFailed(Exception("Purchase not verified"))
                        }
                    },
                    onFailure = { onFailed(it) }
                )
            } catch (e: Exception) {
                onFailed(e)
            }
        }
    }

    /**
     * بازیابی خریدهای قبلی (Restore Purchase)
     */
    fun restorePurchases(
        onRestored: (List<PurchaseInfo>) -> Unit,
        onFailed: (Throwable) -> Unit
    ) {
        if (!isConnected) {
            onFailed(Exception("Bazaar not connected"))
            return
        }

        scope.launch {
            try {
                val result = payment?.getPurchasedProducts()
                result?.fold(
                    onSuccess = { purchases ->
                        Log.d(TAG, "Restored ${purchases.size} purchases")
                        onRestored(purchases)
                    },
                    onFailure = { onFailed(it) }
                )
            } catch (e: Exception) {
                onFailed(e)
            }
        }
    }

    /**
     * بررسی وضعیت VIP
     */
    fun checkVipStatus(
        onResult: (Boolean) -> Unit
    ) {
        if (!isConnected) {
            onResult(false)
            return
        }

        scope.launch {
            try {
                val result = payment?.getPurchasedProducts()
                result?.fold(
                    onSuccess = { purchases ->
                        val isVip = purchases.any { it.productId in ALL_VIP_SKUS }
                        onResult(isVip)
                    },
                    onFailure = { onResult(false) }
                )
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    /**
     * بررسی اتصال
     */
    fun isConnected(): Boolean = isConnected
}
