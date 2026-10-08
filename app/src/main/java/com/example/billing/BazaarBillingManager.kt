package com.example.billing

import android.content.Context
import androidx.activity.ComponentActivity
import com.example.BuildConfig
import ir.cafebazaar.poolakey.Connection
import ir.cafebazaar.poolakey.Payment
import ir.cafebazaar.poolakey.config.PaymentConfiguration
import ir.cafebazaar.poolakey.config.SecurityCheck
import ir.cafebazaar.poolakey.request.PurchaseRequest

class BazaarBillingManager(
    context: Context,
    private val onVipChanged: (Boolean) -> Unit,
    private val onMessage: (String) -> Unit
) {
    companion object {
        const val MONTHLY = "vip_monthly"
        const val THREE_MONTHS = "vip_3months"
        const val YEARLY = "vip_yearly"
        const val LIFETIME = "vip_lifetime"
        private val SUBSCRIPTION_IDS = setOf(MONTHLY, THREE_MONTHS, YEARLY)
    }

    private val applicationContext = context.applicationContext

    private val payment: Payment by lazy {
        Payment(
            context = applicationContext,
            config = PaymentConfiguration(
                localSecurityCheck = if (BuildConfig.DEBUG) {
                    SecurityCheck.Disable
                } else {
                    SecurityCheck.Enable(rsaPublicKey = BuildConfig.BAZAAR_RSA_PUBLIC_KEY)
                },
                shouldSupportSubscription = true
            )
        )
    }
    private var connection: Connection? = null

    fun connect() {
        if (connection != null) return
        try {
            connection = payment.connect {
                connectionSucceed {
                    onMessage("اتصال به کافه‌بازار برقرار شد")
                    restorePurchases()
                }
                connectionFailed { error ->
                    onMessage("اتصال به کافه‌بازار ناموفق بود: " + (error.message ?: "خطای نامشخص"))
                }
                disconnected { connection = null }
            }
        } catch (t: Throwable) {
            connection = null
            onMessage("اتصال به کافه‌بازار در دسترس نیست")
        }
    }

    fun disconnect() {
        connection?.disconnect()
        connection = null
    }

    fun purchase(activity: ComponentActivity, productId: String) {
        payment.purchaseProduct(
            registry = activity.activityResultRegistry,
            request = PurchaseRequest(productId = productId, payload = "studio_taraneh_vip")
        ) {
            purchaseSucceed { info ->
                if (info.productId == LIFETIME) {
                    onVipChanged(true)
                    onMessage("VIP مادام‌العمر فعال شد")
                }
            }
            purchaseCanceled { onMessage("خرید لغو شد") }
            purchaseFailed { error -> onMessage("خرید ناموفق بود: " + (error.message ?: "خطای نامشخص")) }
            failedToBeginFlow { error -> onMessage("شروع پرداخت ناموفق بود: " + (error.message ?: "خطای نامشخص")) }
        }
    }

    fun subscribe(activity: ComponentActivity, productId: String) {
        payment.subscribeProduct(
            registry = activity.activityResultRegistry,
            request = PurchaseRequest(productId = productId, payload = "studio_taraneh_vip")
        ) {
            purchaseSucceed { info ->
                if (info.productId in SUBSCRIPTION_IDS) {
                    onVipChanged(true)
                    onMessage("اشتراک VIP فعال شد")
                }
            }
            purchaseCanceled { onMessage("خرید اشتراک لغو شد") }
            purchaseFailed { error -> onMessage("خرید اشتراک ناموفق بود: " + (error.message ?: "خطای نامشخص")) }
            failedToBeginFlow { error -> onMessage("شروع پرداخت اشتراک ناموفق بود: " + (error.message ?: "خطای نامشخص")) }
        }
    }

    fun restorePurchases() {
        payment.getPurchasedProducts {
            querySucceed { purchases ->
                if (purchases.any { it.productId == LIFETIME }) {
                    onVipChanged(true)
                    onMessage("خرید VIP مادام‌العمر بازیابی شد")
                } else querySubscriptions()
            }
            queryFailed { error -> onMessage("بازیابی خریدها ناموفق بود: " + (error.message ?: "خطای نامشخص")) }
        }
    }

    private fun querySubscriptions() {
        payment.getSubscribedProducts {
            querySucceed { subscriptions ->
                val active = subscriptions.any { it.productId in SUBSCRIPTION_IDS }
                onVipChanged(active)
                if (active) onMessage("اشتراک VIP بازیابی شد")
            }
            queryFailed { error -> onMessage("بازیابی اشتراک ناموفق بود: " + (error.message ?: "خطای نامشخص")) }
        }
    }
}