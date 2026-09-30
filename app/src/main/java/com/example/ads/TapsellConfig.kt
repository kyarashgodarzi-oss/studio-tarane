package com.example.ads

import com.example.BuildConfig

object TapsellConfig {
    val APP_ID: String get() = BuildConfig.TAPSELL_APP_KEY
    val ZONE_REWARDED_VIDEO: String get() = BuildConfig.TAPSELL_REWARDED_ZONE
    val ZONE_STANDARD_BANNER: String get() = BuildConfig.TAPSELL_STANDARD_BANNER_ZONE
    val ZONE_INTERSTITIAL: String get() = BuildConfig.TAPSELL_INSTANT_ZONE
    val ZONE_NATIVE_VIDEO: String get() = BuildConfig.TAPSELL_NATIVE_VIDEO_ZONE
    val ZONE_PRE_ROLL: String get() = BuildConfig.TAPSELL_PRE_ROLL_ZONE
}
