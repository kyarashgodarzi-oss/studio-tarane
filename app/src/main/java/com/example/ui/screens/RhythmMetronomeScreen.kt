package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.localization.LocalStudioStrings

@Composable
fun RhythmMetronomeScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    BackHandler {
        if (viewModel.isRhythmPlaying.value) {
            viewModel.toggleRhythmPlay()
        }
        onBack()
    }

    val strings = LocalStudioStrings.current
    val bpm by viewModel.rhythmBpm.collectAsState()
    val isPlaying by viewModel.isRhythmPlaying.collectAsState()
    val activeStep by viewModel.currentRhythmStep.collectAsState()
    val timeSignature by viewModel.rhythmTimeSignature.collectAsState()

    // Beat Pulse Animation
    val beatScale = remember { Animatable(1f) }
    LaunchedEffect(activeStep) {
        if (isPlaying && activeStep % 4 == 0) {
            beatScale.snapTo(1.22f)
            beatScale.animateTo(1f, tween(150))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .verticalScroll(rememberScrollState())
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (isPlaying) viewModel.toggleRhythmPlay()
                onBack()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.cancel)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = strings.rhythmMakerTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = strings.rhythmMakerSubtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        // Metronome Hero Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f), RoundedCornerShape(24.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Time signature indicator
                Text(
                    text = "میزان: $timeSignature",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Pulsing BPM Display
                Box(
                    modifier = Modifier
                        .scale(beatScale.value)
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = if (isPlaying) 0.35f else 0.15f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .border(
                            2.dp,
                            if (isPlaying) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$bpm",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "BPM",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // BPM +/- adjustments
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        onClick = { viewModel.setRhythmBpm(bpm - 5) },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "کاهش ۵ BPM")
                    }

                    IconButton(
                        onClick = { viewModel.setRhythmBpm(bpm - 1) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text("-۱", fontWeight = FontWeight.Bold)
                    }

                    // Main Start / Stop Button
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                if (isPlaying) Color.Red else MaterialTheme.colorScheme.primary
                            )
                            .clickable { viewModel.toggleRhythmPlay() }
                            .testTag("rhythm_play_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) strings.actionPause else strings.actionPlay,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.setRhythmBpm(bpm + 1) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Text("+1", fontWeight = FontWeight.Bold)
                    }

                    IconButton(
                        onClick = { viewModel.setRhythmBpm(bpm + 5) },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Smooth BPM Slider
                Slider(
                    value = bpm.toFloat(),
                    onValueChange = { viewModel.setRhythmBpm(it.toInt()) },
                    valueRange = 40f..240f,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )

                // Tap Tempo & Vibration row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { viewModel.tapTempo() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.testTag("tap_tempo_button")
                    ) {
                        Icon(Icons.Default.TouchApp, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.tapTempoBtn)
                    }

                    Button(
                        onClick = {
                            viewModel.rhythmSynth.enableVibration = !viewModel.rhythmSynth.enableVibration
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (viewModel.rhythmSynth.enableVibration) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Vibration, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (viewModel.rhythmSynth.enableVibration) strings.hapticOn else strings.hapticOff, fontSize = 11.sp)
                    }
                }
            }
        }

        // Time Signature selector
        Text(
            text = "${strings.timeSignatureLabel}:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val signatures = listOf("4/4 (Pop/Rap)", "3/4 (Waltz)", "2/4 (March)", "6/8 (Slow/Traditional)", "12/8 (Blues)")
            val keys = listOf("4/4", "3/4", "2/4", "6/8", "12/8")
            items(signatures.indices.toList()) { idx ->
                val k = keys[idx]
                val label = signatures[idx]
                val isSel = (timeSignature == k)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { viewModel.setRhythmTimeSignature(k) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step Sequencer Drum Grid
        Text(
            text = strings.stepSequencerTitle,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        val trackNames = listOf("Kick (طبل)", "Snare (اسنیر)", "Hi-Hat (سنج)", "Clap (کِلپ)")
        val trackColors = listOf(Color(0xFF38BDF8), Color(0xFFEC4899), Color(0xFFF59E0B), Color(0xFFA855F7))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(18.dp))
                .padding(12.dp)
        ) {
            trackNames.forEachIndexed { trackIndex, trackName ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = trackName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = trackColors[trackIndex]
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(16) { step ->
                            val isActiveStep = (isPlaying && activeStep == step)
                            val isTriggered = viewModel.rhythmSynth.stepGrid[trackIndex][step]
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        when {
                                            isTriggered -> trackColors[trackIndex]
                                            isActiveStep -> trackColors[trackIndex].copy(alpha = 0.4f)
                                            step % 4 == 0 -> MaterialTheme.colorScheme.surfaceVariant
                                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                        }
                                    )
                                    .border(
                                        width = if (isActiveStep) 2.dp else 1.dp,
                                        color = if (isActiveStep) Color.White else Color.Transparent,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable {
                                        viewModel.toggleSequencerStep(trackIndex, step)
                                    }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sound Effects Pad Library
        Text(
            text = strings.soundEffectsTitle,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        val soundEffects = listOf(
            "Kick" to "کیک (Kick)",
            "Snare" to "اسنیر (Snare)",
            "HiHat" to "های‌هت (Hi-Hat)",
            "Clap" to "دست (Clap)",
            "Bass" to "بیس ۸۰۸ (Bass)",
            "Percussion" to "پرکاشن (Perc)",
            "Click" to "کلیک (Click)",
            "Metronome" to "تیک مترونوم"
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            items(soundEffects) { (key, label) ->
                Box(
                    modifier = Modifier
                        .height(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .clickable { viewModel.previewDrumSound(key) }
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
