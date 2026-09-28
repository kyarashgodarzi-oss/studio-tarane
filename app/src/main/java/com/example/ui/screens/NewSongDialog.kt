package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.GenreCatalog
import com.example.ui.localization.LocalStudioStrings

@Composable
fun NewSongDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, artist: String, genre: String, subgenre: String, bpm: Int, timeSignature: String, description: String, tags: String) -> Unit
) {
    val strings = LocalStudioStrings.current
    var title by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("Pop") }
    var selectedSubgenre by remember { mutableStateOf("پاپ احساسی (Emotional)") }
    var bpm by remember { mutableIntStateOf(120) }
    var timeSignature by remember { mutableStateOf("4/4") }
    var tags by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val currentSubgenres = when (selectedGenre) {
        "Rap" -> GenreCatalog.rapSubgenres
        "Pop" -> GenreCatalog.popSubgenres
        "Rock" -> listOf("آلترناتیو", "هارد راک", "پاپ راک")
        "Traditional" -> listOf("تلفیقی", "کلاسیک ایرانی", "عرفانی")
        else -> listOf("دلخواه")
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "✍️ ${strings.newSong}",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = strings.newSongSub,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(strings.songTitle) },
                    placeholder = { Text(strings.songTitle) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Artist
                OutlinedTextField(
                    value = artist,
                    onValueChange = { artist = it },
                    label = { Text(strings.artistName) },
                    placeholder = { Text(strings.artistName) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Genre selection
                Text(
                    text = "${strings.genreLabel}:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    contentPadding = PaddingValues(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val genres = listOf(
                        "Pop" to "🎤 پاپ",
                        "Rap" to "🎤 رپ و هیپ‌هاپ",
                        "Rock" to "🎸 راک",
                        "Traditional" to "🪕 سنتی/تلفیقی",
                        "Custom" to "✨ دلخواه"
                    )
                    items(genres) { (key, label) ->
                        val isSel = (selectedGenre == key)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSel) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .clickable {
                                    selectedGenre = key
                                    selectedSubgenre = when (key) {
                                        "Rap" -> GenreCatalog.rapSubgenres.first()
                                        "Pop" -> GenreCatalog.popSubgenres.first()
                                        else -> "عمومی"
                                    }
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
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

                Spacer(modifier = Modifier.height(12.dp))

                // Subgenre selection
                Text(
                    text = "زیرسبک (Subgenre):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    contentPadding = PaddingValues(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(currentSubgenres) { sub ->
                        val isSubSel = (selectedSubgenre == sub)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSubSel) MaterialTheme.colorScheme.secondary
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                )
                                .border(
                                    1.dp,
                                    if (isSubSel) MaterialTheme.colorScheme.secondary else Color.Transparent,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedSubgenre = sub }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = sub,
                                fontSize = 11.sp,
                                color = if (isSubSel) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // BPM and Time signature row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${strings.bpmLabel}: $bpm BPM",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("4/4", "3/4", "2/4", "6/8").forEach { sig ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (timeSignature == sig) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { timeSignature = sig }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = sig,
                                    fontSize = 11.sp,
                                    color = if (timeSignature == sig) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                Slider(
                    value = bpm.toFloat(),
                    onValueChange = { bpm = it.toInt() },
                    valueRange = 60f..200f,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tags
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text(strings.tagsLabel) },
                    placeholder = { Text(strings.tagsLabel) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text(strings.actionCancel)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            onCreate(
                                title.ifBlank { strings.newSong },
                                artist,
                                selectedGenre,
                                selectedSubgenre,
                                bpm,
                                timeSignature,
                                description,
                                tags
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(strings.actionCreate)
                    }
                }
            }
        }
    }
}
