package com.example.ads

import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.PlayerView
import com.google.ads.interactivemedia.v3.api.AdErrorEvent
import com.google.ads.interactivemedia.v3.api.AdEvent
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.VastRequestListener
import ir.tapsell.sdk.preroll.TapsellPrerollAd
import ir.tapsell.sdk.preroll.ima.ImaAdsLoader

class AdsShowcaseActivity : ComponentActivity() {

    private val sampleVideoUrl =
        "https://storage.backtory.com/tapsell-server/sdk/VASTContentVideo.mp4"

    private lateinit var playerView: PlayerView
    private lateinit var adUiContainer: FrameLayout
    private lateinit var companionContainer: FrameLayout

    private var player: ExoPlayer? = null
    private var tapsellPrerollAd: TapsellPrerollAd? = null
    private var adsLoader: ImaAdsLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(20, 24, 20, 20)
        }

        root.addView(TextView(this).apply {
            text = "ویدیوی آموزشی استودیو"
            textSize = 22f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 8)
        })

        root.addView(TextView(this).apply {
            text = "پیش از پخش ویدیو، تبلیغ Pre-roll نمایش داده می‌شود."
            textSize = 13f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 16)
        })

        val videoContainer = FrameLayout(this)
        adUiContainer = FrameLayout(this)
        companionContainer = FrameLayout(this)
        playerView = PlayerView(this)

        videoContainer.addView(adUiContainer, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 420
        ))
        adUiContainer.addView(playerView, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 420
        ))

        root.addView(videoContainer, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 420
        ))

        root.addView(companionContainer, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 100
        ))

        root.addView(Button(this).apply {
            text = "پخش ویدیو"
            setOnClickListener { requestPreRoll() }
        })

        setContentView(root)
    }

    private fun requestPreRoll() {
        releasePreRoll()

        val tag = TapsellPlus.getVastTag(TapsellConfig.ZONE_PRE_ROLL)
        tapsellPrerollAd = TapsellPlus.requestVastAd(
            this,
            playerView,
            sampleVideoUrl,
            adUiContainer,
            companionContainer,
            object : VastRequestListener {
                override fun onAdsLoaderCreated(loader: ImaAdsLoader) {
                    loader.release()
                    adsLoader = loader
                    initializePlayer(tag)
                }

                override fun onAdEvent(adEvent: AdEvent) = Unit

                override fun onAdError(adErrorEvent: AdErrorEvent) {
                    Toast.makeText(
                        this@AdsShowcaseActivity,
                        "Pre-roll در دسترس نیست: ${adErrorEvent.error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun initializePlayer(tag: String?) {
        if (adsLoader == null || tag == null) return

        val mediaSourceFactory = DefaultMediaSourceFactory(this)
            .setLocalAdInsertionComponents({ adsLoader }, playerView)

        player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        playerView.player = player
        adsLoader?.setPlayer(player)
        player?.playWhenReady = true

        val mediaItem = MediaItem.Builder()
            .setUri(Uri.parse(sampleVideoUrl))
            .setAdsConfiguration(
                MediaItem.AdsConfiguration.Builder(Uri.parse(tag)).build()
            )
            .build()

        player?.setMediaItem(mediaItem)
        player?.prepare()
    }

    private fun releasePreRoll() {
        tapsellPrerollAd?.destroyAd()
        tapsellPrerollAd = null
        if (::playerView.isInitialized) playerView.player = null
        player?.release()
        player = null
        adsLoader = null
        if (::adUiContainer.isInitialized) adUiContainer.removeAllViews()
        if (::companionContainer.isInitialized) companionContainer.removeAllViews()
    }

    override fun onResume() {
        super.onResume()
        if (::playerView.isInitialized) playerView.player?.let { it.playWhenReady = true }
        tapsellPrerollAd?.resumeAd()
    }

    override fun onPause() {
        tapsellPrerollAd?.pauseAd()
        super.onPause()
    }

    override fun onDestroy() {
        releasePreRoll()
        super.onDestroy()
    }
}
