package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioPlayerManager
import com.example.audio.AudioRecorderManager
import com.example.audio.RhythmSynthesizer
import com.example.data.local.AppDatabase
import com.example.data.model.GenreCatalog
import com.example.data.model.Song
import com.example.data.model.SongSection
import com.example.data.model.SongVersion
import com.example.data.model.SongWithSections
import com.example.data.model.VoiceRecording
import com.example.data.preferences.AppThemeMode
import com.example.data.preferences.DayNightOption
import com.example.data.preferences.EditorFontSize
import com.example.data.preferences.StudioPreferences
import com.example.data.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.util.Stack

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = SongRepository(database.songDao())
    val preferences = StudioPreferences(application)

    val audioRecorder = AudioRecorderManager(application)
    val audioPlayer = AudioPlayerManager()
    val rhythmSynth = RhythmSynthesizer(application)

    // Reactive streams from repository
    val activeSongs: StateFlow<List<Song>> = repository.activeSongs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteSongs: StateFlow<List<Song>> = repository.favoriteSongs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentSongs: StateFlow<List<Song>> = repository.recentSongs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trashSongs: StateFlow<List<Song>> = repository.trashSongs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val voiceRecordings: StateFlow<List<VoiceRecording>> = repository.allVoiceRecordings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search query & results
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: StateFlow<List<Song>> = _searchQuery
        .debounce(250)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(emptyList())
            else repository.searchSongs(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Currently opened song in editor
    private val _currentSongWithSections = MutableStateFlow<SongWithSections?>(null)
    val currentSongWithSections: StateFlow<SongWithSections?> = _currentSongWithSections.asStateFlow()

    // Editor Undo / Redo history stacks
    private val undoStack = Stack<List<SongSection>>()
    private val redoStack = Stack<List<SongSection>>()

    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _canRedo = MutableStateFlow(false)
    val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

    // In-editor section recording state
    private val _recordingSectionId = MutableStateFlow<Long?>(null)
    val recordingSectionId: StateFlow<Long?> = _recordingSectionId.asStateFlow()

    private val _isRecordingGlobal = MutableStateFlow(false)
    val isRecordingGlobal: StateFlow<Boolean> = _isRecordingGlobal.asStateFlow()

    // Playback state
    private val _currentPlayingPath = MutableStateFlow<String?>(null)
    val currentPlayingPath: StateFlow<String?> = _currentPlayingPath.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0f)
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()

    // Rhythm Synth state
    private val _rhythmBpm = MutableStateFlow(120)
    val rhythmBpm: StateFlow<Int> = _rhythmBpm.asStateFlow()

    private val _isRhythmPlaying = MutableStateFlow(false)
    val isRhythmPlaying: StateFlow<Boolean> = _isRhythmPlaying.asStateFlow()

    private val _currentRhythmStep = MutableStateFlow(0)
    val currentRhythmStep: StateFlow<Int> = _currentRhythmStep.asStateFlow()

    private val _rhythmTimeSignature = MutableStateFlow("4/4")
    val rhythmTimeSignature: StateFlow<String> = _rhythmTimeSignature.asStateFlow()

    // Song Version history
    private val _currentSongVersions = MutableStateFlow<List<SongVersion>>(emptyList())
    val currentSongVersions: StateFlow<List<SongVersion>> = _currentSongVersions.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openSong(songId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val songWithSections = repository.getSongWithSectionsDirect(songId)
            _currentSongWithSections.value = songWithSections
            undoStack.clear()
            redoStack.clear()
            _canUndo.value = false
            _canRedo.value = false

            // Load versions
            repository.getVersionsForSong(songId).collect { versions ->
                _currentSongVersions.value = versions
            }
        }
    }

    fun createNewSong(
        title: String,
        artist: String,
        genre: String,
        subgenre: String,
        bpm: Int,
        timeSignature: String,
        description: String,
        tags: String,
        onCreated: (Long) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val songId = repository.createNewSong(
                title = title,
                artist = artist,
                genre = genre,
                subgenre = subgenre,
                bpm = bpm,
                timeSignature = timeSignature,
                description = description,
                tags = tags
            )
            onCreated(songId)
        }
    }

    fun updateSongMetadata(updatedSong: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSong(updatedSong)
            _currentSongWithSections.value = _currentSongWithSections.value?.copy(song = updatedSong)
        }
    }

    private fun pushUndoState(currentSections: List<SongSection>) {
        undoStack.push(currentSections.map { it.copy() })
        redoStack.clear()
        _canUndo.value = true
        _canRedo.value = false
    }

    fun updateSectionContent(sectionId: Long, newContent: String) {
        val current = _currentSongWithSections.value ?: return
        pushUndoState(current.sections)
        val updatedSections = current.sections.map { sec ->
            if (sec.id == sectionId) sec.copy(content = newContent) else sec
        }
        _currentSongWithSections.value = current.copy(sections = updatedSections)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSections(current.song.id, updatedSections)
        }
    }

    fun addSection(sectionType: String, customTitle: String = "", colorHex: String? = null) {
        val current = _currentSongWithSections.value ?: return
        pushUndoState(current.sections)
        val hex = colorHex ?: GenreCatalog.getDefaultColorForSection(sectionType)
        val newSection = SongSection(
            songId = current.song.id,
            sectionType = sectionType,
            customTitle = customTitle,
            content = "",
            colorHex = hex,
            orderIndex = current.sections.size
        )
        val updatedSections = current.sections + newSection
        _currentSongWithSections.value = current.copy(sections = updatedSections)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSections(current.song.id, updatedSections)
            // Refresh with generated IDs
            val reloaded = repository.getSongWithSectionsDirect(current.song.id)
            _currentSongWithSections.value = reloaded
        }
    }

    fun removeSection(sectionId: Long) {
        val current = _currentSongWithSections.value ?: return
        pushUndoState(current.sections)
        val updatedSections = current.sections.filterNot { it.id == sectionId }
        _currentSongWithSections.value = current.copy(sections = updatedSections)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSections(current.song.id, updatedSections)
        }
    }

    fun moveSection(index: Int, direction: Int) { // -1 for up, 1 for down
        val current = _currentSongWithSections.value ?: return
        val sections = current.sections.toMutableList()
        val targetIndex = index + direction
        if (targetIndex in 0 until sections.size) {
            pushUndoState(current.sections)
            val temp = sections[index]
            sections[index] = sections[targetIndex]
            sections[targetIndex] = temp
            _currentSongWithSections.value = current.copy(sections = sections)
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateSections(current.song.id, sections)
            }
        }
    }

    fun updateSectionStyling(
        sectionId: Long,
        colorHex: String? = null,
        isBold: Boolean? = null,
        isItalic: Boolean? = null,
        textAlign: String? = null
    ) {
        val current = _currentSongWithSections.value ?: return
        val updatedSections = current.sections.map { sec ->
            if (sec.id == sectionId) {
                sec.copy(
                    colorHex = colorHex ?: sec.colorHex,
                    isBold = isBold ?: sec.isBold,
                    isItalic = isItalic ?: sec.isItalic,
                    textAlign = textAlign ?: sec.textAlign
                )
            } else sec
        }
        _currentSongWithSections.value = current.copy(sections = updatedSections)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSections(current.song.id, updatedSections)
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val current = _currentSongWithSections.value ?: return
            redoStack.push(current.sections.map { it.copy() })
            val previous = undoStack.pop()
            _currentSongWithSections.value = current.copy(sections = previous)
            _canUndo.value = undoStack.isNotEmpty()
            _canRedo.value = true
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateSections(current.song.id, previous)
            }
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val current = _currentSongWithSections.value ?: return
            undoStack.push(current.sections.map { it.copy() })
            val next = redoStack.pop()
            _currentSongWithSections.value = current.copy(sections = next)
            _canUndo.value = true
            _canRedo.value = redoStack.isNotEmpty()
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateSections(current.song.id, next)
            }
        }
    }

    fun toggleFavorite(songId: Long, current: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleFavorite(songId, current)
        }
    }

    fun moveToTrash(songId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.moveToTrash(songId)
        }
    }

    fun restoreFromTrash(songId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.restoreFromTrash(songId)
        }
    }

    fun permanentlyDelete(songId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.permanentlyDeleteSong(songId)
        }
    }

    fun emptyTrash() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.emptyTrash()
        }
    }

    fun saveVersionSnapshot(versionName: String, note: String = "") {
        val current = _currentSongWithSections.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveVersionSnapshot(current.song.id, versionName, note)
        }
    }

    fun restoreVersion(version: SongVersion) {
        val current = _currentSongWithSections.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            repository.restoreVersion(current.song.id, version)
            val reloaded = repository.getSongWithSectionsDirect(current.song.id)
            _currentSongWithSections.value = reloaded
        }
    }

    // Audio recording for a specific section
    fun startSectionRecording(sectionId: Long) {
        val file = audioRecorder.startRecording("sec_${sectionId}")
        if (file != null) {
            _recordingSectionId.value = sectionId
            _isRecordingGlobal.value = true
        }
    }

    fun stopSectionRecording(sectionId: Long) {
        val durationMs = audioRecorder.stopRecording()
        val file = audioRecorder.getCurrentFile()
        _recordingSectionId.value = null
        _isRecordingGlobal.value = false

        if (file != null && file.exists()) {
            val current = _currentSongWithSections.value ?: return
            val updatedSections = current.sections.map { sec ->
                if (sec.id == sectionId) {
                    sec.copy(audioFilePath = file.absolutePath, audioDurationMs = durationMs)
                } else sec
            }
            _currentSongWithSections.value = current.copy(sections = updatedSections)
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateSections(current.song.id, updatedSections)
                // Also add to global voice recordings catalog
                repository.saveVoiceRecording(
                    VoiceRecording(
                        songId = current.song.id,
                        sectionId = sectionId,
                        title = "${current.song.title} - ${updatedSections.find { it.id == sectionId }?.getDisplayName() ?: "بخش"}",
                        filePath = file.absolutePath,
                        durationMs = durationMs
                    )
                )
            }
        }
    }

    fun deleteSectionAudio(sectionId: Long) {
        val current = _currentSongWithSections.value ?: return
        val sec = current.sections.find { it.id == sectionId }
        sec?.audioFilePath?.let { path ->
            try { File(path).delete() } catch (_: Exception) {}
        }
        val updatedSections = current.sections.map { s ->
            if (s.id == sectionId) s.copy(audioFilePath = null, audioDurationMs = 0L) else s
        }
        _currentSongWithSections.value = current.copy(sections = updatedSections)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateSections(current.song.id, updatedSections)
        }
    }

    // Standalone Voice Studio recording
    fun startStandaloneRecording(title: String = "ایده ملودی") {
        val file = audioRecorder.startRecording("memo")
        if (file != null) {
            _isRecordingGlobal.value = true
        }
    }

    fun stopStandaloneRecording(title: String) {
        val durationMs = audioRecorder.stopRecording()
        val file = audioRecorder.getCurrentFile()
        _isRecordingGlobal.value = false
        if (file != null && file.exists()) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.saveVoiceRecording(
                    VoiceRecording(
                        title = title.ifBlank { "ایده ضبط شده ${System.currentTimeMillis() % 1000}" },
                        filePath = file.absolutePath,
                        durationMs = durationMs
                    )
                )
            }
        }
    }

    fun playAudio(filePath: String) {
        if (_currentPlayingPath.value == filePath) {
            audioPlayer.stop()
            _currentPlayingPath.value = null
            _playbackProgress.value = 0f
            return
        }
        _currentPlayingPath.value = filePath
        audioPlayer.play(
            filePath = filePath,
            onProgress = { current, total ->
                if (total > 0) {
                    _playbackProgress.value = current.toFloat() / total.toFloat()
                }
            },
            onCompletion = {
                _currentPlayingPath.value = null
                _playbackProgress.value = 0f
            }
        )
    }

    fun stopAudio() {
        audioPlayer.stop()
        _currentPlayingPath.value = null
        _playbackProgress.value = 0f
    }

    fun deleteVoiceRecording(id: Long, filePath: String) {
        try { File(filePath).delete() } catch (_: Exception) {}
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteVoiceRecording(id)
        }
    }

    // Rhythm Synthesizer operations
    fun setRhythmBpm(bpm: Int) {
        val clamped = bpm.coerceIn(40, 240)
        _rhythmBpm.value = clamped
        rhythmSynth.bpm = clamped
    }

    fun setRhythmTimeSignature(sig: String) {
        _rhythmTimeSignature.value = sig
        rhythmSynth.timeSignature = sig
    }

    fun tapTempo() {
        val newBpm = rhythmSynth.recordTapTempo()
        _rhythmBpm.value = newBpm
    }

    fun toggleRhythmPlay() {
        if (_isRhythmPlaying.value) {
            rhythmSynth.stop()
            _isRhythmPlaying.value = false
            _currentRhythmStep.value = 0
        } else {
            rhythmSynth.bpm = _rhythmBpm.value
            rhythmSynth.timeSignature = _rhythmTimeSignature.value
            rhythmSynth.start { step ->
                _currentRhythmStep.value = step
            }
            _isRhythmPlaying.value = true
        }
    }

    fun toggleSequencerStep(track: Int, step: Int) {
        if (track in 0 until 4 && step in 0 until 16) {
            rhythmSynth.stepGrid[track][step] = !rhythmSynth.stepGrid[track][step]
        }
    }

    fun previewDrumSound(soundName: String) {
        rhythmSynth.playDrumSound(soundName)
    }

    // Export & Import
    suspend fun exportBackupJson(): String {
        return repository.exportFullBackupJson()
    }

    suspend fun importBackupJson(jsonString: String): Int {
        return repository.importBackupJson(jsonString)
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
        audioRecorder.stopRecording()
        rhythmSynth.release()
    }
}
