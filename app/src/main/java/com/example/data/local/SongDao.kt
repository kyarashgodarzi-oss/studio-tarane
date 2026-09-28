package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.Song
import com.example.data.model.SongSection
import com.example.data.model.SongVersion
import com.example.data.model.SongWithSections
import com.example.data.model.VoiceRecording
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {

    @Query("SELECT * FROM songs WHERE isTrash = 0 ORDER BY lastModifiedTimestamp DESC")
    fun getAllActiveSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE isTrash = 0 AND isFavorite = 1 ORDER BY lastModifiedTimestamp DESC")
    fun getFavoriteSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE isTrash = 0 ORDER BY lastModifiedTimestamp DESC LIMIT 20")
    fun getRecentSongs(): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE isTrash = 1 ORDER BY trashTimestamp DESC")
    fun getTrashSongs(): Flow<List<Song>>

    @Transaction
    @Query("SELECT * FROM songs WHERE id = :songId")
    fun getSongWithSections(songId: Long): Flow<SongWithSections?>

    @Transaction
    @Query("SELECT * FROM songs WHERE id = :songId")
    suspend fun getSongWithSectionsDirect(songId: Long): SongWithSections?

    @Transaction
    @Query("SELECT * FROM songs WHERE isTrash = 0")
    suspend fun getAllSongsWithSectionsDirect(): List<SongWithSections>

    @Query("""
        SELECT DISTINCT songs.* FROM songs 
        LEFT JOIN song_sections ON songs.id = song_sections.songId
        WHERE songs.isTrash = 0 AND (
            songs.title LIKE '%' || :query || '%' OR 
            songs.artist LIKE '%' || :query || '%' OR 
            songs.tags LIKE '%' || :query || '%' OR 
            songs.genre LIKE '%' || :query || '%' OR 
            songs.subgenre LIKE '%' || :query || '%' OR 
            song_sections.content LIKE '%' || :query || '%'
        )
        ORDER BY songs.lastModifiedTimestamp DESC
    """)
    fun searchSongs(query: String): Flow<List<Song>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song): Long

    @Update
    suspend fun updateSong(song: Song)

    @Query("UPDATE songs SET isFavorite = :isFavorite WHERE id = :songId")
    suspend fun updateFavoriteStatus(songId: Long, isFavorite: Boolean)

    @Query("UPDATE songs SET isTrash = 1, trashTimestamp = :timestamp WHERE id = :songId")
    suspend fun moveToTrash(songId: Long, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE songs SET isTrash = 0, trashTimestamp = NULL WHERE id = :songId")
    suspend fun restoreFromTrash(songId: Long)

    @Query("DELETE FROM songs WHERE id = :songId")
    suspend fun permanentlyDeleteSong(songId: Long)

    @Query("DELETE FROM songs WHERE isTrash = 1")
    suspend fun emptyTrash()

    // Sections
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSection(section: SongSection): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSections(sections: List<SongSection>)

    @Update
    suspend fun updateSection(section: SongSection)

    @Query("DELETE FROM song_sections WHERE id = :sectionId")
    suspend fun deleteSection(sectionId: Long)

    @Query("DELETE FROM song_sections WHERE songId = :songId")
    suspend fun deleteSectionsForSong(songId: Long)

    // Versions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVersion(version: SongVersion): Long

    @Query("SELECT * FROM song_versions WHERE songId = :songId ORDER BY timestamp DESC")
    fun getVersionsForSong(songId: Long): Flow<List<SongVersion>>

    // Voice Recordings
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoiceRecording(recording: VoiceRecording): Long

    @Query("SELECT * FROM voice_recordings ORDER BY timestamp DESC")
    fun getAllVoiceRecordings(): Flow<List<VoiceRecording>>

    @Query("SELECT * FROM voice_recordings WHERE songId = :songId ORDER BY timestamp DESC")
    fun getVoiceRecordingsForSong(songId: Long): Flow<List<VoiceRecording>>

    @Query("DELETE FROM voice_recordings WHERE id = :id")
    suspend fun deleteVoiceRecording(id: Long)

    @Query("UPDATE voice_recordings SET title = :title WHERE id = :id")
    suspend fun updateVoiceRecordingTitle(id: Long, title: String)
}
