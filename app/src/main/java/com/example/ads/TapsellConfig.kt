package com.example.ads

import com.example.BuildConfig

object TapsellConfig {
    val APP_ID: String get() = BuildConfig.TAPSELL_APP_KEY
    val ZONE_REWARDED_VIDEO: String get() = BuildConfig.TAPSELL_REWARDED_ZONE
    val ZONE_STANDARD_BANNER: String get() = BuildConfig.TAPSELL_STANDARD_BANNER_ZONE
}
