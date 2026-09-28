package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

class RhythmSynthesizer(private val context: Context) {

    private val sampleRate = 44100
    private var isPlaying = false
    private var loopJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    var bpm: Int = 120
    var timeSignature: String = "4/4"
    var enableVibration: Boolean = true
    var currentStep: Int = 0

    // Step Sequencer grid: 4 tracks x 16 steps
    // Track 0: Kick, Track 1: Snare, Track 2: Hi-Hat, Track 3: Clap
    val stepGrid = Array(4) { BooleanArray(16) }

    init {
        // Default 4/4 Pop/Rap beat preset
        // Kick on 0, 8 (or 0, 6, 10 for trap/drill)
        stepGrid[0][0] = true
        stepGrid[0][8] = true
        // Snare on 4, 12
        stepGrid[1][4] = true
        stepGrid[1][12] = true
        // Hi-Hat on 0, 2, 4, 6, 8, 10, 12, 14 (8th notes)
        for (i in 0 until 16 step 2) {
            stepGrid[2][i] = true
        }
    }

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    // Tap tempo tracker
    private val tapTimestamps = mutableListOf<Long>()

    fun recordTapTempo(): Int {
        val now = System.currentTimeMillis()
        if (tapTimestamps.isNotEmpty() && now - tapTimestamps.last() > 2500) {
            tapTimestamps.clear()
        }
        tapTimestamps.add(now)
        if (tapTimestamps.size > 5) {
            tapTimestamps.removeAt(0)
        }

        if (tapTimestamps.size >= 2) {
            val intervals = mutableListOf<Long>()
            for (i in 1 until tapTimestamps.size) {
                intervals.add(tapTimestamps[i] - tapTimestamps[i - 1])
            }
            val avgInterval = intervals.average()
            if (avgInterval > 100) {
                val calculatedBpm = (60000.0 / avgInterval).toInt().coerceIn(40, 240)
                this.bpm = calculatedBpm
                return calculatedBpm
            }
        }
        return bpm
    }

    fun start(onStep: (step: Int) -> Unit = {}) {
        if (isPlaying) return
        isPlaying = true

        loopJob = scope.launch {
            var stepIndex = 0
            while (isActive && isPlaying) {
                val beatsPerMeasure = when (timeSignature) {
                    "3/4" -> 3
                    "2/4" -> 2
                    "6/8" -> 6
                    "12/8" -> 12
                    else -> 4
                }

                // 16th note step duration in ms: (60_000 / BPM) / 4
                val stepIntervalMs = (60000L / bpm.coerceAtLeast(40)) / 4L
                val maxSteps = if (timeSignature == "3/4") 12 else 16

                val activeStep = stepIndex % maxSteps
                currentStep = activeStep

                // Sound triggers for active step
                val isAccent = (activeStep % 4 == 0)
                val isMeasureStart = (activeStep == 0)

                // Check step sequencer tracks
                val playKick = stepGrid[0][activeStep % 16]
                val playSnare = stepGrid[1][activeStep % 16]
                val playHiHat = stepGrid[2][activeStep % 16]
                val playClap = stepGrid[3][activeStep % 16]

                if (playKick || playSnare || playHiHat || playClap) {
                    if (playKick) playDrumSound("Kick")
                    if (playSnare) playDrumSound("Snare")
                    if (playHiHat) playDrumSound("HiHat")
                    if (playClap) playDrumSound("Clap")
                } else if (activeStep % 4 == 0) {
                    // Metronome click on beat if sequencer is empty
                    playMetronomeClick(isMeasureStart)
                }

                if (isMeasureStart && enableVibration) {
                    triggerHaptic(true)
                } else if (isAccent && enableVibration) {
                    triggerHaptic(false)
                }

                onStep(activeStep)
                stepIndex++
                delay(stepIntervalMs.coerceAtLeast(20L))
            }
        }
    }

    fun stop() {
        isPlaying = false
        loopJob?.cancel()
        loopJob = null
        currentStep = 0
    }

    fun isMetronomePlaying(): Boolean = isPlaying

    private fun triggerHaptic(strong: Boolean) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = if (strong) {
                    VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE)
                } else {
                    VibrationEffect.createOneShot(12, 100)
                }
                vibrator?.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(if (strong) 25L else 12L)
            }
        } catch (_: Exception) {}
    }

    fun playMetronomeClick(isAccent: Boolean) {
        scope.launch(Dispatchers.Default) {
            val freq = if (isAccent) 1600.0 else 950.0
            val durationMs = 25
            val numSamples = (durationMs * sampleRate) / 1000
            val buffer = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val time = i.toDouble() / sampleRate
                val envelope = exp(-time * 120.0) // fast decay
                val sample = sin(2.0 * PI * freq * time) * envelope
                buffer[i] = (sample * Short.MAX_VALUE * 0.7).toInt().toShort()
            }
            writeToAudioTrack(buffer)
        }
    }

    fun playDrumSound(soundName: String) {
        scope.launch(Dispatchers.Default) {
            val buffer = generateDrumPcm(soundName)
            writeToAudioTrack(buffer)
        }
    }

    private fun generateDrumPcm(type: String): ShortArray {
        return when (type) {
            "Kick" -> {
                val durationMs = 150
                val numSamples = (durationMs * sampleRate) / 1000
                val buffer = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    // Frequency sweeps from 160Hz down to 45Hz
                    val instantFreq = 45.0 + 115.0 * exp(-time * 45.0)
                    val envelope = exp(-time * 25.0)
                    val sample = sin(2.0 * PI * instantFreq * time) * envelope
                    buffer[i] = (sample * Short.MAX_VALUE * 0.9).toInt().toShort()
                }
                buffer
            }
            "Snare" -> {
                val durationMs = 120
                val numSamples = (durationMs * sampleRate) / 1000
                val buffer = ShortArray(numSamples)
                val random = Random(42)
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    val tone = sin(2.0 * PI * 220.0 * time) * exp(-time * 40.0)
                    val noise = (random.nextDouble() * 2.0 - 1.0) * exp(-time * 30.0)
                    val sample = (tone * 0.4 + noise * 0.6)
                    buffer[i] = (sample * Short.MAX_VALUE * 0.75).toInt().toShort()
                }
                buffer
            }
            "HiHat" -> {
                val durationMs = 45
                val numSamples = (durationMs * sampleRate) / 1000
                val buffer = ShortArray(numSamples)
                val random = Random()
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    val noise = (random.nextDouble() * 2.0 - 1.0)
                    val envelope = exp(-time * 95.0)
                    val sample = noise * envelope
                    buffer[i] = (sample * Short.MAX_VALUE * 0.5).toInt().toShort()
                }
                buffer
            }
            "Clap" -> {
                val durationMs = 130
                val numSamples = (durationMs * sampleRate) / 1000
                val buffer = ShortArray(numSamples)
                val random = Random()
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    // Multi-tap noise spikes
                    val multiTap = if (time < 0.012) 0.8 else if (time < 0.024) 0.9 else if (time < 0.036) 1.0 else 0.0
                    val envelope = if (time >= 0.036) exp(-(time - 0.036) * 35.0) else multiTap
                    val noise = (random.nextDouble() * 2.0 - 1.0) * envelope
                    buffer[i] = (noise * Short.MAX_VALUE * 0.7).toInt().toShort()
                }
                buffer
            }
            "Bass" -> {
                val durationMs = 300
                val numSamples = (durationMs * sampleRate) / 1000
                val buffer = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    val freq = 65.4 // C2 note 808 sub bass
                    val envelope = exp(-time * 8.0)
                    val sample = sin(2.0 * PI * freq * time) * envelope
                    buffer[i] = (sample * Short.MAX_VALUE * 0.85).toInt().toShort()
                }
                buffer
            }
            "Percussion" -> {
                val durationMs = 60
                val numSamples = (durationMs * sampleRate) / 1000
                val buffer = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    val envelope = exp(-time * 75.0)
                    val sample = sin(2.0 * PI * 1150.0 * time) * envelope
                    buffer[i] = (sample * Short.MAX_VALUE * 0.75).toInt().toShort()
                }
                buffer
            }
            "Click" -> {
                val durationMs = 15
                val numSamples = (durationMs * sampleRate) / 1000
                val buffer = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    val sample = sin(2.0 * PI * 2200.0 * time) * exp(-time * 200.0)
                    buffer[i] = (sample * Short.MAX_VALUE * 0.8).toInt().toShort()
                }
                buffer
            }
            else -> { // Metronome
                val durationMs = 20
                val numSamples = (durationMs * sampleRate) / 1000
                val buffer = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    val sample = sin(2.0 * PI * 1300.0 * time) * exp(-time * 150.0)
                    buffer[i] = (sample * Short.MAX_VALUE * 0.75).toInt().toShort()
                }
                buffer
            }
        }
    }

    private fun writeToAudioTrack(buffer: ShortArray) {
        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            scope.launch {
                delay((buffer.size * 1000L / sampleRate) + 50L)
                try {
                    audioTrack.stop()
                    audioTrack.release()
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}
    }

    fun release() {
        stop()
    }
}
