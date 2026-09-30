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

class BazaarBillingManager(private val context: Context) {

    companion object {
        private const val TAG = "BazaarBilling"
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

    fun connect(
        onConnected: () -> Unit = {},
        onDisconnected: () -> Unit = {},
        onFailed: (Throwable) -> Unit = {}
    ) {
        try {
            // نام‌گذاری صحیح پارامترها طبق مستندات Poolakey
            val config = PaymentConfiguration(
                localSecurityCheck = SecurityCheck.Disable,
                remoteSecurityCheck = SecurityCheck.Enable(BazaarConfig.RSA_PUBLIC_KEY)
            )

            payment = Payment(context, config)
            connection = payment?.connect()

            // تغییر نام متد از connectionState() به connectionState (طبق مستندات)
            connection?.connectionState()?.observeForever { state ->
                when (state) {
                    is ConnectionState.Connected -> {
                        isConnected = true
                        onConnected()
                    }
                    is ConnectionState.Disconnected -> {
                        isConnected = false
                        onDisconnected()
                    }
                    // طبق مستندات، کلاس خطا ConnectionState.Failed است
                    is ConnectionState.Failed -> {
                        isConnected = false
                        onFailed(state.throwable)
                    }
                }
            }
        } catch (e: Exception) {
            onFailed(e)
        }
    }

    fun disconnect() {
        try {
            connection?.disconnect()
            isConnected = false
        } catch (e: Exception) {
            Log.e(TAG, "Disconnect error: ${e.message}")
        }
    }

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

                // در Poolakey، متد purchaseProduct به صورت مستقیم نتیجه را برمی‌گرداند
                // و نیازی به fold ندارد. برای مدیریت خطا از try/catch استفاده می‌کنیم.
                val purchaseInfo = payment?.purchaseProduct(activity, request)
                if (purchaseInfo != null) {
                    onSuccess(purchaseInfo)
                } else {
                    onFailed(Exception("Purchase failed or canceled"))
                }
            } catch (e: Exception) {
                // اگر خطا مربوط به لغو توسط کاربر باشد
                if (e.message?.contains("cancel", ignoreCase = true) == true) {
                    onCanceled()
                } else {
                    onFailed(e)
                }
            }
        }
    }

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
                val purchases = payment?.getPurchasedProducts()
                if (purchases != null) {
                    onRestored(purchases)
                } else {
                    onFailed(Exception("Failed to get purchases"))
                }
            } catch (e: Exception) {
                onFailed(e)
            }
        }
    }

    fun checkVipStatus(onResult: (Boolean) -> Unit) {
        if (!isConnected) {
            onResult(false)
            return
        }
        scope.launch {
            try {
                val purchases = payment?.getPurchasedProducts()
                val isVip = purchases?.any { it.productId in ALL_VIP_SKUS } ?: false
                onResult(isVip)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    fun isConnected(): Boolean = isConnected
}
