package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val artist: String = "",
    val genre: String = "Pop",
    val subgenre: String = "پاپ احساسی",
    val creationTimestamp: Long = System.currentTimeMillis(),
    val lastModifiedTimestamp: Long = System.currentTimeMillis(),
    val description: String = "",
    val tags: String = "",
    val status: String = "پیش‌نویس", // پیش‌نویس, در حال تکمیل, کامل شده, ضبط شده, منتشر شده
    val bpm: Int = 120,
    val timeSignature: String = "4/4",
    val isFavorite: Boolean = false,
    val isTrash: Boolean = false,
    val trashTimestamp: Long? = null,
    val notes: String = ""
)
