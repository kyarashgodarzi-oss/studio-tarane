package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusBannerType
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel
// برای تبلیغات Native باید از این کلاس استفاده کرد
import ir.tapsell.plus.TapsellPlusNativeBannerAd

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
            // بر اساس مستندات، متد initialize نیاز به context و APP_ID دارد [citation:5][citation:18]
            TapsellPlus.initialize(context, TapsellConfig.APP_ID)
            isInitialized = true
            Log.d(TAG, "Tapsell initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Tapsell init failed: ${e.message}")
        }
    }

    /**
     * بنر استاندارد (Standard Banner)
     * بر اساس مستندات، ابتدا درخواست می‌شود و سپس نمایش داده می‌شود [citation:9]
     */
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
                    override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel)
                        TapsellPlus.showStandardBannerAd(
                            activity,
                            tapsellPlusAdModel.responseId,
                            container,
                            object : AdShowListener() {
                                // در کلاس AdShowListener متد خطا به صورت `error(message: String)` است
                                override fun onError(error: TapsellPlusErrorModel?) {
                                    onFailed()
                                }
                            }
                        )
                    }

                    // در کلاس AdRequestCallback متد خطا به صورت `error(message: String)` است
                    override fun error(message: String) {
                        Log.e(TAG, "Banner request error: $message")
                        onFailed()
                    }
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Banner request exception: ${e.message}")
            onFailed()
        }
    }

    /**
     * بنر آنی (Interstitial Banner)
     * برای جلوگیری از مزاحمت، بین هر نمایش یک فاصله زمانی در نظر گرفته شده است.
     */
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
                    override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel)
                        TapsellPlus.showInterstitialAd(
                            activity,
                            tapsellPlusAdModel.responseId,
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

                    override fun error(message: String) {
                        onClosed()
                    }
                }
            )
        } catch (e: Exception) {
            onClosed()
        }
    }

    /**
     * ویدیو جایزه‌ای (Rewarded Video)
     */
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
                    override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel)
                        TapsellPlus.showRewardedVideoAd(
                            activity,
                            tapsellPlusAdModel.responseId,
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

                    override fun error(message: String) {
                        onClosed()
                    }
                }
            )
        } catch (e: Exception) {
            onClosed()
        }
    }

    /**
     * ویدیو همسان (Native Video)
     * نام صحیح متد و کلاس طبق مستندات تپسل [citation:12]
     */
    fun requestNativeVideo(
        activity: Activity,
        container: FrameLayout,
        onFailed: () -> Unit = {}
    ) {
        if (!isInitialized) { onFailed(); return }
        try {
            TapsellPlus.requestNativeVideoAd(
                activity,
                TapsellConfig.ZONE_NATIVE_VIDEO,
                object : AdRequestCallback() {
                    override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel)
                        TapsellPlus.showNativeVideoAd(
                            activity,
                            tapsellPlusAdModel.responseId,
                            container,
                            object : AdShowListener() {
                                override fun onError(error: TapsellPlusErrorModel?) {
                                    onFailed()
                                }
                            }
                        )
                    }

                    override fun error(message: String) {
                        onFailed()
                    }
                }
            )
        } catch (e: Exception) {
            onFailed()
        }
    }

    /**
     * ویدیو پیش‌نمایشی (Pre-roll Video)
     * در تپسل پلاس، این نوع تبلیغ با متد Interstitial درخواست می‌شود.
     */
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
                    override fun response(tapsellPlusAdModel: TapsellPlusAdModel) {
                        super.response(tapsellPlusAdModel)
                        TapsellPlus.showInterstitialAd(
                            activity,
                            tapsellPlusAdModel.responseId,
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

                    override fun error(message: String) {
                        onCompleted()
                    }
                }
            )
        } catch (e: Exception) {
            onCompleted()
        }
    }
}
