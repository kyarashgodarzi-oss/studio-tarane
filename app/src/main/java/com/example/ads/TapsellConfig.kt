package com.example.ads

import com.example.BuildConfig

object TapsellConfig {
    const val APP_ID: String
        get() = BuildConfig.TAPSELL_APP_KEY
    const val ZONE_REWARDED_VIDEO: String
        get() = BuildConfig.TAPSELL_REWARDED_ZONE
    const val ZONE_STANDARD_BANNER: String
        get() = BuildConfig.TAPSELL_STANDARD_BANNER_ZONE
}
