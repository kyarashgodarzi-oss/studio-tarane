package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NewSongDialog(
    onDismiss: () -> Unit,
    onCreate: (
        title: String,
        artist: String,
        genre: String,
        subgenre: String,
        bpm: Int,
        timeSignature: String,
        description: String,
        tags: String
    ) -> Unit
) {
    var newSongTitle by remember { mutableStateOf("") }
    var newSongArtist by remember { mutableStateOf("") }
    var newSongGenre by remember { mutableStateOf("Pop") }
    var newSongSub by remember { mutableStateOf("Pop") }
    var newSongBpm by remember { mutableStateOf("120") }
    var newSongTimeSignature by remember { mutableStateOf("4/4") }
    var newSongDescription by remember { mutableStateOf("") }
    var newSongTags by remember { mutableStateOf("") }

    val genres = listOf("Pop", "Rap", "Rock", "Traditional", "Custom")
    val subGenres = when (newSongGenre) {
        "Pop" -> listOf(
            "Pop", "Romantic Pop", "Emotional Pop",
            "Sad Pop", "Happy Pop", "Modern Pop", "Classic Pop"
        )
        "Rap" -> listOf(
            "Rap", "Trap", "Drill", "Old School", "Underground",
            "Conscious", "Gangsta", "Melodic Rap", "Freestyle"
        )
        "Rock" -> listOf("Rock", "Hard Rock", "Soft Rock", "Alternative")
        "Traditional" -> listOf("Traditional", "Folk", "Classical")
        else -> listOf("Custom")
    }
    val timeSignatures = listOf("4/4", "3/4", "2/4", "6/8", "12/8")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "ترانه جدید",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newSongTitle,
                    onValueChange = { newSongTitle = it },
                    label = { Text("عنوان ترانه") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newSongArtist,
                    onValueChange = { newSongArtist = it },
                    label = { Text("نام هنرمند") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("سبک اصلی", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    genres.forEach { genre ->
                        FilterChip(
                            selected = genre == newSongGenre,
                            onClick = {
                                newSongGenre = genre
                                newSongSub = when (genre) {
                                    "Pop" -> "Pop"
                                    "Rap" -> "Rap"
                                    "Rock" -> "Rock"
                                    "Traditional" -> "Traditional"
                                    else -> "Custom"
                                }
                            },
                            label = { Text(genre) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("زیرسبک", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    subGenres.forEach { sub ->
                        FilterChip(
                            selected = sub == newSongSub,
                            onClick = { newSongSub = sub },
                            label = { Text(sub) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = newSongBpm,
                        onValueChange = { newSongBpm = it.filter { c -> c.isDigit() } },
                        label = { Text("BPM") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("میزان", style = MaterialTheme.typography.labelMedium)
                        FlowRow {
                            timeSignatures.take(3).forEach { ts ->
                                FilterChip(
                                    selected = ts == newSongTimeSignature,
                                    onClick = { newSongTimeSignature = ts },
                                    label = { Text(ts, style = MaterialTheme.typography.labelSmall) }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newSongTags,
                    onValueChange = { newSongTags = it },
                    label = { Text("برچسب‌ها (با کاما جدا کنید)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newSongDescription,
                    onValueChange = { newSongDescription = it },
                    label = { Text("توضیحات") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("انصراف")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newSongTitle.isNotBlank()) {
                                onCreate(
                                    newSongTitle,
                                    newSongArtist,
                                    newSongGenre,
                                    newSongSub,
                                    newSongBpm.toIntOrNull() ?: 120,
                                    newSongTimeSignature,
                                    newSongDescription,
                                    newSongTags
                                )
                            }
                        }
                    ) {
                        Text("ساخت ترانه")
                    }
                }
            }
        }
    }
}
