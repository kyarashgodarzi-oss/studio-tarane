package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "song_versions",
    foreignKeys = [
        ForeignKey(
            entity = Song::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["songId"])]
)
data class SongVersion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val songId: Long = 0,
    val versionName: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val sectionsSnapshotJson: String = "",
    val notes: String = ""
)
