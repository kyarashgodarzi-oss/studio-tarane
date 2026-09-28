package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voice_recordings")
data class VoiceRecording(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val songId: Long? = null,
    val sectionId: Long? = null,
    val title: String = "",
    val filePath: String = "",
    val durationMs: Long = 0L,
    val timestamp: Long = System.currentTimeMillis()
)
