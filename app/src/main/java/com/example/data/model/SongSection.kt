package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "song_sections",
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
data class SongSection(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val songId: Long = 0,
    val sectionType: String = "Verse", // Intro, Verse, Chorus, PreChorus, Bridge, Hook, Outro, Refrain, Custom
    val customTitle: String = "",
    val content: String = "",
    val colorHex: String = "#818CF8", // default hex color
    val orderIndex: Int = 0,
    val audioFilePath: String? = null,
    val audioDurationMs: Long = 0L,
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val textAlign: String = "Right" // Right (RTL), Center, Left
) {
    fun getDisplayName(strings: com.example.ui.localization.StudioStrings? = null): String {
        return if (customTitle.isNotBlank()) {
            customTitle
        } else if (strings != null) {
            when (sectionType) {
                "Intro" -> strings.sectionIntro
                "Verse" -> strings.sectionVerse
                "Chorus" -> strings.sectionChorus
                "PreChorus" -> strings.sectionPreChorus
                "Bridge" -> strings.sectionBridge
                "Hook" -> strings.sectionHook
                "Outro" -> strings.sectionOutro
                "Refrain" -> strings.sectionRefrain
                else -> strings.sectionCustom
            }
        } else {
            when (sectionType) {
                "Intro" -> "مقدمه (Intro)"
                "Verse" -> "بند / ورس (Verse)"
                "Chorus" -> "هم‌خوان / کروس (Chorus)"
                "PreChorus" -> "پیش‌هم‌خوان (Pre-Chorus)"
                "Bridge" -> "پل (Bridge)"
                "Hook" -> "هوک (Hook)"
                "Outro" -> "بخش پایانی (Outro)"
                "Refrain" -> "ترجیع‌بند (Refrain)"
                else -> "بخش دلخواه"
            }
        }
    }
}
