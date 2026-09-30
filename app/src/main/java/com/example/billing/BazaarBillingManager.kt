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
    private var paymentConnection: Connection? = null

    fun connect(
        onConnected: () -> Unit = {},
        onDisconnected: () -> Unit = {},
        onFailed: (Throwable) -> Unit = {}
    ) {
        try {
            // طبق مستندات، PaymentConfiguration فقط localSecurityCheck می‌گیرد
            val securityCheck = SecurityCheck.Enable(rsaPublicKey = BazaarConfig.RSA_PUBLIC_KEY)
            val paymentConfig = PaymentConfiguration(localSecurityCheck = securityCheck)
            
            payment = Payment(context = context, config = paymentConfig)
            
            // طبق مستندات Poolakey، اتصال از طریق متد connect با کالبک‌ها انجام می‌شود
            paymentConnection = payment?.connect {
                connectionSucceed {
                    Log.d(TAG, "Connected to Bazaar")
                    onConnected()
                }
                
                connectionFailed { throwable ->
                    Log.e(TAG, "Connection failed: ${throwable.message}")
                    onFailed(throwable)
                }
                
                disconnected {
                    Log.d(TAG, "Disconnected from Bazaar")
                    onDisconnected()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Connection exception: ${e.message}")
            onFailed(e)
        }
    }

    fun disconnect() {
        try {
            paymentConnection?.disconnect()
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
        try {
            val request = PurchaseRequest(
                productId = sku,
                payload = "studio_taraneh_vip"
            )
            
            val result = payment?.purchaseProduct(activity, request)
            result?.fold(
                onSuccess = { purchaseInfo ->
                    Log.d(TAG, "Purchase successful: ${purchaseInfo.orderId}")
                    onSuccess(purchaseInfo)
                },
                onFailure = { throwable ->
                    Log.e(TAG, "Purchase failed: ${throwable.message}")
                    onFailed(throwable)
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

    fun restorePurchases(
        onRestored: (List<PurchaseInfo>) -> Unit,
        onFailed: (Throwable) -> Unit
    ) {
        try {
            val result = payment?.getPurchasedProducts()
            result?.fold(
                onSuccess = { purchases ->
                    Log.d(TAG, "Restored ${purchases.size} purchases")
                    onRestored(purchases)
                },
                onFailure = { throwable ->
                    Log.e(TAG, "Restore failed: ${throwable.message}")
                    onFailed(throwable)
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Restore exception: ${e.message}")
            onFailed(e)
        }
    }

    fun checkVipStatus(onResult: (Boolean) -> Unit) {
        try {
            val result = payment?.getPurchasedProducts()
            result?.fold(
                onSuccess = { purchases ->
                    val isVip = purchases.any { it.productId in ALL_VIP_SKUS }
                    onResult(isVip)
                },
                onFailure = {
                    onResult(false)
                }
            )
        } catch (e: Exception) {
            onResult(false)
        }
    }

    fun isConnected(): Boolean {
        return paymentConnection?.getState() == ConnectionState.Connected
    }
}
