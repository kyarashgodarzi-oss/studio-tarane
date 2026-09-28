package com.example.data.repository

import com.example.data.local.SongDao
import com.example.data.model.Song
import com.example.data.model.SongSection
import com.example.data.model.SongVersion
import com.example.data.model.SongWithSections
import com.example.data.model.VoiceRecording
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

class SongRepository(private val songDao: SongDao) {

    val activeSongs: Flow<List<Song>> = songDao.getAllActiveSongs()
    val favoriteSongs: Flow<List<Song>> = songDao.getFavoriteSongs()
    val recentSongs: Flow<List<Song>> = songDao.getRecentSongs()
    val trashSongs: Flow<List<Song>> = songDao.getTrashSongs()
    val allVoiceRecordings: Flow<List<VoiceRecording>> = songDao.getAllVoiceRecordings()

    fun searchSongs(query: String): Flow<List<Song>> {
        return songDao.searchSongs(query)
    }

    fun getSongWithSections(songId: Long): Flow<SongWithSections?> {
        return songDao.getSongWithSections(songId)
    }

    suspend fun getSongWithSectionsDirect(songId: Long): SongWithSections? {
        return songDao.getSongWithSectionsDirect(songId)
    }

    suspend fun createNewSong(
        title: String,
        artist: String,
        genre: String,
        subgenre: String,
        bpm: Int = 120,
        timeSignature: String = "4/4",
        description: String = "",
        tags: String = ""
    ): Long {
        val song = Song(
            title = title.ifBlank { "ترانه بدون عنوان" },
            artist = artist,
            genre = genre,
            subgenre = subgenre,
            bpm = bpm,
            timeSignature = timeSignature,
            description = description,
            tags = tags,
            creationTimestamp = System.currentTimeMillis(),
            lastModifiedTimestamp = System.currentTimeMillis()
        )
        val songId = songDao.insertSong(song)

        // Add a default intro, verse, and chorus for immediate writing
        val initialSections = listOf(
            SongSection(
                songId = songId,
                sectionType = "Verse",
                customTitle = "بند اول (Verse 1)",
                content = "",
                colorHex = "#38BDF8",
                orderIndex = 0
            ),
            SongSection(
                songId = songId,
                sectionType = "Chorus",
                customTitle = "هم‌خوان (Chorus)",
                content = "",
                colorHex = "#EC4899",
                orderIndex = 1
            )
        )
        songDao.insertSections(initialSections)
        return songId
    }

    suspend fun updateSong(song: Song) {
        songDao.updateSong(song.copy(lastModifiedTimestamp = System.currentTimeMillis()))
    }

    suspend fun updateSections(songId: Long, sections: List<SongSection>) {
        songDao.deleteSectionsForSong(songId)
        val reordered = sections.mapIndexed { index, section ->
            section.copy(songId = songId, orderIndex = index)
        }
        songDao.insertSections(reordered)
        val song = songDao.getSongWithSectionsDirect(songId)?.song
        if (song != null) {
            songDao.updateSong(song.copy(lastModifiedTimestamp = System.currentTimeMillis()))
        }
    }

    suspend fun addSectionToSong(songId: Long, section: SongSection): Long {
        return songDao.insertSection(section.copy(songId = songId))
    }

    suspend fun deleteSection(sectionId: Long) {
        songDao.deleteSection(sectionId)
    }

    suspend fun toggleFavorite(songId: Long, current: Boolean) {
        songDao.updateFavoriteStatus(songId, !current)
    }

    suspend fun moveToTrash(songId: Long) {
        songDao.moveToTrash(songId)
    }

    suspend fun restoreFromTrash(songId: Long) {
        songDao.restoreFromTrash(songId)
    }

    suspend fun permanentlyDeleteSong(songId: Long) {
        songDao.permanentlyDeleteSong(songId)
    }

    suspend fun emptyTrash() {
        songDao.emptyTrash()
    }

    suspend fun saveVersionSnapshot(songId: Long, versionName: String, note: String = "") {
        val songWithSections = songDao.getSongWithSectionsDirect(songId) ?: return
        val jsonArray = JSONArray()
        for (sec in songWithSections.sections.sortedBy { it.orderIndex }) {
            val obj = JSONObject()
            obj.put("type", sec.sectionType)
            obj.put("title", sec.getDisplayName())
            obj.put("content", sec.content)
            obj.put("color", sec.colorHex)
            jsonArray.put(obj)
        }
        val version = SongVersion(
            songId = songId,
            versionName = versionName.ifBlank { "نسخه پیش‌نویس ${System.currentTimeMillis() % 1000}" },
            sectionsSnapshotJson = jsonArray.toString(),
            notes = note
        )
        songDao.insertVersion(version)
    }

    fun getVersionsForSong(songId: Long): Flow<List<SongVersion>> {
        return songDao.getVersionsForSong(songId)
    }

    suspend fun restoreVersion(songId: Long, version: SongVersion) {
        try {
            val jsonArray = JSONArray(version.sectionsSnapshotJson)
            val restoredSections = mutableListOf<SongSection>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                restoredSections.add(
                    SongSection(
                        songId = songId,
                        sectionType = obj.optString("type", "Verse"),
                        customTitle = obj.optString("title", ""),
                        content = obj.optString("content", ""),
                        colorHex = obj.optString("color", "#38BDF8"),
                        orderIndex = i
                    )
                )
            }
            if (restoredSections.isNotEmpty()) {
                updateSections(songId, restoredSections)
            }
        } catch (_: Exception) {
            // handle parse safely
        }
    }

    suspend fun saveVoiceRecording(recording: VoiceRecording): Long {
        return songDao.insertVoiceRecording(recording)
    }

    suspend fun deleteVoiceRecording(id: Long) {
        songDao.deleteVoiceRecording(id)
    }

    suspend fun updateVoiceRecordingTitle(id: Long, title: String) {
        songDao.updateVoiceRecordingTitle(id, title)
    }

    suspend fun exportFullBackupJson(): String {
        val allSongsWithSections = songDao.getAllSongsWithSectionsDirect()
        val root = JSONObject()
        root.put("app", "Studio Taraneh")
        root.put("version", "1.0.0")
        root.put("exportedAt", System.currentTimeMillis())

        val songsArray = JSONArray()
        for (item in allSongsWithSections) {
            val sObj = JSONObject()
            sObj.put("title", item.song.title)
            sObj.put("artist", item.song.artist)
            sObj.put("genre", item.song.genre)
            sObj.put("subgenre", item.song.subgenre)
            sObj.put("bpm", item.song.bpm)
            sObj.put("timeSignature", item.song.timeSignature)
            sObj.put("status", item.song.status)
            sObj.put("tags", item.song.tags)
            sObj.put("description", item.song.description)
            sObj.put("notes", item.song.notes)

            val secArray = JSONArray()
            for (sec in item.sections.sortedBy { it.orderIndex }) {
                val secObj = JSONObject()
                secObj.put("type", sec.sectionType)
                secObj.put("customTitle", sec.customTitle)
                secObj.put("content", sec.content)
                secObj.put("colorHex", sec.colorHex)
                secArray.put(secObj)
            }
            sObj.put("sections", secArray)
            songsArray.put(sObj)
        }
        root.put("songs", songsArray)
        return root.toString(2)
    }

    suspend fun importBackupJson(jsonString: String): Int {
        var importedCount = 0
        try {
            val root = JSONObject(jsonString)
            val songsArray = root.optJSONArray("songs") ?: return 0
            for (i in 0 until songsArray.length()) {
                val sObj = songsArray.getJSONObject(i)
                val song = Song(
                    title = sObj.optString("title", "ترانه وارد شده"),
                    artist = sObj.optString("artist", ""),
                    genre = sObj.optString("genre", "Pop"),
                    subgenre = sObj.optString("subgenre", "عمومی"),
                    bpm = sObj.optInt("bpm", 120),
                    timeSignature = sObj.optString("timeSignature", "4/4"),
                    status = sObj.optString("status", "پیش‌نویس"),
                    tags = sObj.optString("tags", ""),
                    description = sObj.optString("description", ""),
                    notes = sObj.optString("notes", "")
                )
                val newId = songDao.insertSong(song)
                val secArray = sObj.optJSONArray("sections")
                if (secArray != null) {
                    val sections = mutableListOf<SongSection>()
                    for (j in 0 until secArray.length()) {
                        val secObj = secArray.getJSONObject(j)
                        sections.add(
                            SongSection(
                                songId = newId,
                                sectionType = secObj.optString("type", "Verse"),
                                customTitle = secObj.optString("customTitle", ""),
                                content = secObj.optString("content", ""),
                                colorHex = secObj.optString("colorHex", "#38BDF8"),
                                orderIndex = j
                            )
                        )
                    }
                    songDao.insertSections(sections)
                }
                importedCount++
            }
        } catch (_: Exception) {
            return 0
        }
        return importedCount
    }
}
