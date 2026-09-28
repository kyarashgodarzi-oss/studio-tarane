package com.example.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class SongWithSections(
    @Embedded val song: Song,
    @Relation(
        parentColumn = "id",
        entityColumn = "songId"
    )
    val sections: List<SongSection> = emptyList()
) {
    fun getFullLyricsText(): String {
        val sortedSections = sections.sortedBy { it.orderIndex }
        val builder = StringBuilder()
        builder.append("🎵 ").append(song.title)
        if (song.artist.isNotBlank()) {
            builder.append(" - ").append(song.artist)
        }
        builder.append("\n")
        if (song.genre.isNotBlank() || song.subgenre.isNotBlank()) {
            builder.append("سبک: ").append(song.genre).append(" (").append(song.subgenre).append(")\n")
        }
        builder.append("BPM: ").append(song.bpm).append(" | میزان: ").append(song.timeSignature).append("\n\n")

        for (section in sortedSections) {
            builder.append("━━━━━━━━━━━━━━━━━━━\n")
            builder.append("【 ").append(section.getDisplayName()).append(" 】\n")
            builder.append(section.content.trim()).append("\n\n")
        }
        return builder.toString().trim()
    }
}
