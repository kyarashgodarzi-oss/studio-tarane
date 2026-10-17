package com.example.audio

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.File

class AudioPlayerManager {

    private var mediaPlayer: MediaPlayer? = null
    private var currentPlayingPath: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private var progressRunnable: Runnable? = null

    fun play(
        filePath: String,
        onProgress: (currentMs: Int, totalMs: Int) -> Unit = { _, _ -> },
        onCompletion: () -> Unit = {}
    ) {
        val file = File(filePath)
        if (!file.exists()) {
            Log.e("AudioPlayer", "File does not exist: $filePath")
            return
        }

        // If already playing this file, toggle or resume
        if (currentPlayingPath == filePath && mediaPlayer != null) {
            try {
                if (mediaPlayer?.isPlaying == true) {
                    pause()
                } else {
                    mediaPlayer?.start()
                    startProgressPolling(onProgress)
                }
            } catch (e: Exception) {
                Log.e("AudioPlayer", "Unable to resume playback", e)
                stop()
            }
            return
        }

        stop()

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
                start()
                currentPlayingPath = filePath
                setOnCompletionListener {
                    stopProgressPolling()
                    currentPlayingPath = null
                    onCompletion()
                }
            }
            startProgressPolling(onProgress)
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Error playing audio", e)
            stop()
        }
    }

    fun pause() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Pause error", e)
        }
    }

    fun resume() {
        try {
            if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
                mediaPlayer?.start()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Resume error", e)
        }
    }

    fun stop() {
        stopProgressPolling()
        try {
            mediaPlayer?.apply {
                if (isPlaying) stop()
                reset()
                release()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayer", "Stop error", e)
        } finally {
            mediaPlayer = null
            currentPlayingPath = null
        }
    }

    fun isPlayingPath(filePath: String): Boolean {
        return currentPlayingPath == filePath && mediaPlayer?.isPlaying == true
    }

    private fun startProgressPolling(onProgress: (Int, Int) -> Unit) {
        stopProgressPolling()
        progressRunnable = object : Runnable {
            override fun run() {
                val mp = mediaPlayer
                try {
                    if (mp != null && mp.isPlaying) {
                        val current = mp.currentPosition
                        val total = mp.duration
                        onProgress(current, total)
                        handler.postDelayed(this, 100)
                    }
                } catch (e: Exception) {
                    Log.w("AudioPlayer", "Playback progress polling stopped", e)
                    stopProgressPolling()
                }
            }
        }
        handler.post(progressRunnable!!)
    }

    private fun stopProgressPolling() {
        progressRunnable?.let { handler.removeCallbacks(it) }
        progressRunnable = null
    }

    fun release() {
        stop()
    }
}
