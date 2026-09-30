package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.TapsellPlusVideoAdHolder
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel

object TapsellAds {
    private const val TAG = "TapsellAds"

    fun initialize(context: Context) {
        TapsellPlus.initialize(
            context.applicationContext,
            TapsellConfig.APP_ID,
            object : TapsellPlusInitListener {
                override fun onInitializeSuccess(adNetworks: AdNetworks) {
                    Log.d(TAG, "Tapsell initialized: ${adNetworks.name}")
                }
                override fun onInitializeFailed(adNetworks: AdNetworks, adNetworkError: AdNetworkError) {
                    Log.e(TAG, "Tapsell init failed: ${adNetworks.name} / ${adNetworkError.errorMessage}")
                }
            }
        )
        TapsellPlus.setGDPRConsent(context.applicationContext, true)
    }

    fun showRewarded(activity: Activity, onRewarded: () -> Unit, onError: (String) -> Unit = {}) {
        if (activity.isFinishing || activity.isDestroyed) return
        TapsellPlus.requestRewardedVideoAd(activity, TapsellConfig.ZONE_REWARDED_VIDEO,
            object : AdRequestCallback() {
                override fun response(ad: TapsellPlusAdModel) {
                    if (activity.isFinishing || activity.isDestroyed) return
                    TapsellPlus.showRewardedVideoAd(activity, ad.responseId, object : AdShowListener() {
                        override fun onRewarded(ad: TapsellPlusAdModel) { onRewarded() }
                        override fun onError(error: TapsellPlusErrorModel) { onError(error.toString()) }
                    })
                }
                override fun error(message: String) { onError(message) }
            })
    }

    fun showInterstitial(activity: Activity, onError: (String) -> Unit = {}) {
        if (activity.isFinishing || activity.isDestroyed) return
        TapsellPlus.requestInterstitialAd(activity, TapsellConfig.ZONE_INTERSTITIAL,
            object : AdRequestCallback() {
                override fun response(ad: TapsellPlusAdModel) {
                    if (activity.isFinishing || activity.isDestroyed) return
                    TapsellPlus.showInterstitialAd(activity, ad.responseId, object : AdShowListener() {
                        override fun onError(error: TapsellPlusErrorModel) { onError(error.toString()) }
                    })
                }
                override fun error(message: String) { onError(message) }
            })
    }

    fun showNativeVideo(activity: Activity, container: ViewGroup, onError: (String) -> Unit = {}) {
        if (activity.isFinishing || activity.isDestroyed) return
        TapsellPlus.requestNativeVideo(activity, TapsellConfig.ZONE_NATIVE_VIDEO,
            object : AdRequestCallback() {
                override fun response(ad: TapsellPlusAdModel) {
                    if (activity.isFinishing || activity.isDestroyed) return
                    val holder = TapsellPlusVideoAdHolder.Builder()
                        .setContentViewTemplate(com.example.R.layout.native_vid_template)
                        .setAppInstallationViewTemplate(ir.tapsell.sdk.R.layout.tapsell_app_installation_video_ad_template)
                        .setAdContainer(container)
                        .build()
                    TapsellPlus.showNativeVideo(activity, ad.responseId, holder, object : AdShowListener() {
                        override fun onError(error: TapsellPlusErrorModel) { onError(error.toString()) }
                    })
                }
                override fun error(message: String) { onError(message) }
            })
    }

    fun loadBanner(activity: Activity, container: ViewGroup) {
        if (activity.isFinishing || activity.isDestroyed) return
        TapsellPlus.requestStandardBannerAd(activity, TapsellConfig.ZONE_STANDARD_BANNER,
            TapsellPlusBannerType.BANNER_320x50, object : AdRequestCallback() {
                override fun response(ad: TapsellPlusAdModel) {
                    if (activity.isFinishing || activity.isDestroyed) return
                    TapsellPlus.showStandardBannerAd(activity, ad.responseId, container, object : AdShowListener() {
                        override fun onError(error: TapsellPlusErrorModel) {
                            Log.e(TAG, "Banner error: $error")
                        }
                    })
                }
                override fun error(message: String) { Log.e(TAG, "Banner request error: $message") }
            })
    }
}

@Composable
fun TapsellNativeVideo(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = {
                FrameLayout(context).also { container ->
                    container.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    TapsellAds.showNativeVideo(activity, container)
                }
            }
        )
    }
}

@Composable
fun TapsellBanner(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return
    Box(modifier = modifier.fillMaxWidth().height(60.dp)) {
        AndroidView(modifier = Modifier.fillMaxWidth(), factory = {
            FrameLayout(context).also { container ->
                container.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                TapsellAds.loadBanner(activity, container)
            }
        })
    }
}
