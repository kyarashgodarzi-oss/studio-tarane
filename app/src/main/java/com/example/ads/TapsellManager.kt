package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel

class TapsellManager private constructor() {

    companion object {
        private const val TAG = "TapsellManager"

        @Volatile
        private var instance: TapsellManager? = null

        fun getInstance(): TapsellManager {
            return instance ?: synchronized(this) {
                instance ?: TapsellManager().also { instance = it }
            }
        }
    }

    private var isInitialized = false
    private var lastInterstitialTime = 0L

    fun initialize(context: Context) {
        if (isInitialized) return
        try {
            TapsellPlus.initialize(context, TapsellConfig.APP_ID)
            isInitialized = true
            Log.d(TAG, "Tapsell initialized")
        } catch (e: Exception) {
            Log.e(TAG, "Tapsell init failed: ${e.message}")
        }
    }

    fun requestStandardBanner(
        activity: Activity,
        container: FrameLayout,
        onFailed: () -> Unit = {}
    ) {
        if (!isInitialized) { onFailed(); return }
        try {
            TapsellPlus.requestStandardBannerAd(
                activity,
                TapsellConfig.ZONE_STANDARD_BANNER,
                TapsellPlusBannerType.BANNER_320x50,
                object : AdRequestCallback() {
                    override fun response(adModel: TapsellPlusAdModel) {
                        super.response(adModel)
                        TapsellPlus.showStandardBannerAd(
                            activity,
                            adModel.responseId,
                            container,
                            object : AdShowListener() {
                                override fun onError(error: TapsellPlusErrorModel?) {
                                    onFailed()
                                }
                            }
                        )
                    }
                    override fun error(error: TapsellPlusErrorModel?) {
                        onFailed()
                    }
                }
            )
        } catch (e: Exception) {
            onFailed()
        }
    }

    fun showInterstitial(
        activity: Activity,
        forceShow: Boolean = false,
        onClosed: () -> Unit = {}
    ) {
        if (!isInitialized) { onClosed(); return }
        val now = System.currentTimeMillis() / 1000
        if (!forceShow && (now - lastInterstitialTime) < TapsellConfig.INTERSTITIAL_COOLDOWN_SECONDS) {
            onClosed()
            return
        }
        try {
            TapsellPlus.requestInterstitialAd(
                activity,
                TapsellConfig.ZONE_INTERSTITIAL_BANNER,
                object : AdRequestCallback() {
                    override fun response(adModel: TapsellPlusAdModel) {
                        super.response(adModel)
                        TapsellPlus.showInterstitialAd(
                            activity,
                            adModel.responseId,
                            object : AdShowListener() {
                                override fun onAdClosed(ad: TapsellPlusAdModel?) {
                                    lastInterstitialTime = System.currentTimeMillis() / 1000
                                    onClosed()
                                }
                                override fun onError(error: TapsellPlusErrorModel?) {
                                    onClosed()
                                }
                            }
                        )
                    }
                    override fun error(error: TapsellPlusErrorModel?) {
                        onClosed()
                    }
                }
            )
        } catch (e: Exception) {
            onClosed()
        }
    }

    fun showRewardedVideo(
        activity: Activity,
        onRewarded: () -> Unit,
        onClosed: () -> Unit = {}
    ) {
        if (!isInitialized) { onClosed(); return }
        try {
            TapsellPlus.requestRewardedVideoAd(
                activity,
                TapsellConfig.ZONE_REWARDED_VIDEO,
                object : AdRequestCallback() {
                    override fun response(adModel: TapsellPlusAdModel) {
                        super.response(adModel)
                        TapsellPlus.showRewardedVideoAd(
                            activity,
                            adModel.responseId,
                            object : AdShowListener() {
                                override fun onRewarded(ad: TapsellPlusAdModel?) {
                                    onRewarded()
                                }
                                override fun onAdClosed(ad: TapsellPlusAdModel?) {
                                    onClosed()
                                }
                                override fun onError(error: TapsellPlusErrorModel?) {
                                    onClosed()
                                }
                            }
                        )
                    }
                    override fun error(error: TapsellPlusErrorModel?) {
                        onClosed()
                    }
                }
            )
        } catch (e: Exception) {
            onClosed()
        }
    }

    fun requestNativeVideo(
        activity: Activity,
        container: ViewGroup,
        onFailed: () -> Unit = {}
    ) {
        if (!isInitialized) { onFailed(); return }
        try {
            TapsellPlus.requestNativeVideoAd(
                activity,
                TapsellConfig.ZONE_NATIVE_VIDEO,
                object : AdRequestCallback() {
                    override fun response(adModel: TapsellPlusAdModel) {
                        super.response(adModel)
                        TapsellPlus.showNativeVideoAd(
                            activity,
                            adModel.responseId,
                            container,
                            object : AdShowListener() {
                                override fun onError(error: TapsellPlusErrorModel?) {
                                    onFailed()
                                }
                            }
                        )
                    }
                    override fun error(error: TapsellPlusErrorModel?) {
                        onFailed()
                    }
                }
            )
        } catch (e: Exception) {
            onFailed()
        }
    }

    fun showPreRollVideo(
        activity: Activity,
        onCompleted: () -> Unit
    ) {
        if (!isInitialized) { onCompleted(); return }
        try {
            TapsellPlus.requestInterstitialAd(
                activity,
                TapsellConfig.ZONE_PRE_ROLL_VIDEO,
                object : AdRequestCallback() {
                    override fun response(adModel: TapsellPlusAdModel) {
                        super.response(adModel)
                        TapsellPlus.showInterstitialAd(
                            activity,
                            adModel.responseId,
                            object : AdShowListener() {
                                override fun onAdClosed(ad: TapsellPlusAdModel?) {
                                    onCompleted()
                                }
                                override fun onError(error: TapsellPlusErrorModel?) {
                                    onCompleted()
                                }
                            }
                        )
                    }
                    override fun error(error: TapsellPlusErrorModel?) {
                        onCompleted()
                    }
                }
            )
        } catch (e: Exception) {
            onCompleted()
        }
    }
}
