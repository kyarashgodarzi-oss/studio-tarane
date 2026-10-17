package com.example.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File
import java.io.IOException

class AudioRecorderManager(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    private var currentOutputFile: File? = null
    private var startTimeMillis: Long = 0L
    private var isRecording: Boolean = false
    private var isPaused: Boolean = false

    fun startRecording(prefix: String = "taraneh_rec"): File? {
        stopRecording() // ensure any previous is stopped

        val recordingDir = File(context.filesDir, "audio_recordings").apply {
            if (!exists()) mkdirs()
        }
        val file = File(recordingDir, "${prefix}_${System.currentTimeMillis()}.m4a")
        currentOutputFile = file

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            try {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(file.absolutePath)
                prepare()
                start()
                isRecording = true
                isPaused = false
                startTimeMillis = System.currentTimeMillis()
            } catch (e: IOException) {
                Log.e("AudioRecorder", "Recording prepare failed", e)
                try { release() } catch (_: Exception) {}
                file.delete()
                currentOutputFile = null
                isRecording = false
                isPaused = false
                return null
            } catch (e: Exception) {
                Log.e("AudioRecorder", "Recording start failed", e)
                try { release() } catch (_: Exception) {}
                file.delete()
                currentOutputFile = null
                isRecording = false
                isPaused = false
                return null
            }
        }

        return file
    }

    fun pauseRecording() {
        if (isRecording && !isPaused && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                mediaRecorder?.pause()
                isPaused = true
            } catch (e: Exception) {
                Log.e("AudioRecorder", "Pause failed", e)
            }
        }
    }

    fun resumeRecording() {
        if (isRecording && isPaused && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                mediaRecorder?.resume()
                isPaused = false
            } catch (e: Exception) {
                Log.e("AudioRecorder", "Resume failed", e)
            }
        }
    }

    fun stopRecording(): Long {
        if (!isRecording) return 0L
        val duration = System.currentTimeMillis() - startTimeMillis
        val recorder = mediaRecorder
        try {
            recorder?.stop()
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Stop recording failed", e)
        } finally {
            try { recorder?.reset() } catch (_: Exception) {}
            try { recorder?.release() } catch (_: Exception) {}
            mediaRecorder = null
            isRecording = false
            isPaused = false
        }
        return duration
    }

    fun isCurrentlyRecording(): Boolean = isRecording
    fun isCurrentlyPaused(): Boolean = isPaused
    fun getCurrentFile(): File? = currentOutputFile
}
