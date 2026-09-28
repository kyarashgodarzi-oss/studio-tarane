package com.example.ui.screens

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.data.model.GenreCatalog
import com.example.data.model.Song
import com.example.data.model.SongSection
import com.example.data.model.SongVersion
import com.example.ui.MainViewModel
import com.example.ui.localization.LocalStudioStrings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongEditorScreen(
    viewModel: MainViewModel,
    songId: Long,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val strings = LocalStudioStrings.current
    val context = LocalContext.current
    val songWithSections by viewModel.currentSongWithSections.collectAsState()
    val canUndo by viewModel.canUndo.collectAsState()
    val canRedo by viewModel.canRedo.collectAsState()
    val recordingSectionId by viewModel.recordingSectionId.collectAsState()
    val currentPlayingPath by viewModel.currentPlayingPath.collectAsState()
    val playbackProgress by viewModel.playbackProgress.collectAsState()
    val versions by viewModel.currentSongVersions.collectAsState()

    var showMetadataSheet by remember { mutableStateOf(false) }
    var showVersionHistorySheet by remember { mutableStateOf(false) }
    var showColorPickerForSectionId by remember { mutableStateOf<Long?>(null) }
    var showAddCustomSectionDialog by remember { mutableStateOf(false) }

    // Audio recording permission launcher
    var pendingRecordSectionId by remember { mutableStateOf<Long?>(null) }
    val recordPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            pendingRecordSectionId?.let { secId ->
                viewModel.startSectionRecording(secId)
            }
        } else {
            Toast.makeText(context, "مجوز ضبط صدا برای این بخش نیاز است", Toast.LENGTH_SHORT).show()
        }
        pendingRecordSectionId = null
    }

    fun requestStartRecording(sectionId: Long) {
        val permission = Manifest.permission.RECORD_AUDIO
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            viewModel.startSectionRecording(sectionId)
        } else {
            pendingRecordSectionId = sectionId
            recordPermissionLauncher.launch(permission)
        }
    }

    if (songWithSections == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("در حال بارگذاری ترانه...", color = MaterialTheme.colorScheme.onBackground)
        }
        return
    }

    val song = songWithSections!!.song
    val sections = songWithSections!!.sections.sortedBy { it.orderIndex }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // Top Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.actionBack)
                }
                Column {
                    Text(
                        text = song.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1
                    )
                    Text(
                        text = "${song.genre} • ${song.bpm} BPM",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Undo / Redo & Tools
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.undo() },
                    enabled = canUndo
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Undo,
                        contentDescription = strings.actionUndo,
                        tint = if (canUndo) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    )
                }

                IconButton(
                    onClick = { viewModel.redo() },
                    enabled = canRedo
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Redo,
                        contentDescription = strings.actionRedo,
                        tint = if (canRedo) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    )
                }

                IconButton(onClick = {
                    val fullLyrics = songWithSections!!.getFullLyricsText()
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("Lyrics", fullLyrics))
                    Toast.makeText(context, strings.actionCopyAll, Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Default.ContentCopy, contentDescription = strings.actionCopyAll)
                }

                IconButton(onClick = {
                    val fullLyrics = songWithSections!!.getFullLyricsText()
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, fullLyrics)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(intent, strings.actionShare))
                }) {
                    Icon(Icons.Default.Share, contentDescription = strings.actionShare)
                }

                IconButton(onClick = { showVersionHistorySheet = true }) {
                    Icon(Icons.Default.History, contentDescription = strings.menuRecent)
                }

                IconButton(onClick = { showMetadataSheet = true }) {
                    Icon(Icons.Default.Info, contentDescription = strings.songTitle)
                }
            }
        }

        // Quick Section Adder Dock
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .padding(vertical = 6.dp)
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val sectionButtons = listOf(
                    "Verse" to "＋ ${strings.sectionVerse}",
                    "Chorus" to "＋ ${strings.sectionChorus}",
                    "Refrain" to "＋ ${strings.sectionRefrain}",
                    "PreChorus" to "＋ ${strings.sectionPreChorus}",
                    "Bridge" to "＋ ${strings.sectionBridge}",
                    "Hook" to "＋ ${strings.sectionHook}",
                    "Intro" to "＋ ${strings.sectionIntro}",
                    "Outro" to "＋ ${strings.sectionOutro}",
                    "Custom" to "＋ ${strings.sectionCustom}"
                )
                items(sectionButtons) { (type, label) ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                            .clickable {
                                if (type == "Custom") {
                                    showAddCustomSectionDialog = true
                                } else {
                                    viewModel.addSection(type)
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Sections List
        LazyColumn(
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("sections_list")
        ) {
            itemsIndexed(sections, key = { _, sec -> sec.id }) { index, section ->
                SectionEditorCard(
                    section = section,
                    isFirst = (index == 0),
                    isLast = (index == sections.size - 1),
                    isRecording = (recordingSectionId == section.id),
                    isPlaying = (currentPlayingPath == section.audioFilePath),
                    playbackProgress = playbackProgress,
                    onContentChange = { newText ->
                        viewModel.updateSectionContent(section.id, newText)
                    },
                    onMoveUp = { viewModel.moveSection(index, -1) },
                    onMoveDown = { viewModel.moveSection(index, 1) },
                    onDelete = { viewModel.removeSection(section.id) },
                    onOpenColorPicker = { showColorPickerForSectionId = section.id },
                    onToggleBold = { viewModel.updateSectionStyling(section.id, isBold = !section.isBold) },
                    onToggleItalic = { viewModel.updateSectionStyling(section.id, isItalic = !section.isItalic) },
                    onToggleAlign = {
                        val nextAlign = when (section.textAlign) {
                            "Right" -> "Center"
                            "Center" -> "Left"
                            else -> "Right"
                        }
                        viewModel.updateSectionStyling(section.id, textAlign = nextAlign)
                    },
                    onCopySection = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText(section.getDisplayName(), section.content))
                        Toast.makeText(context, "بخش ${section.getDisplayName()} کپی شد", Toast.LENGTH_SHORT).show()
                    },
                    onStartRecord = { requestStartRecording(section.id) },
                    onStopRecord = { viewModel.stopSectionRecording(section.id) },
                    onPlayAudio = { section.audioFilePath?.let { viewModel.playAudio(it) } },
                    onDeleteAudio = { viewModel.deleteSectionAudio(section.id) }
                )
            }
        }
    }

    // Color Picker Sheet
    if (showColorPickerForSectionId != null) {
        val targetSecId = showColorPickerForSectionId!!
        Dialog(onDismissRequest = { showColorPickerForSectionId = null }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "انتخاب رنگ تم این بخش",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(GenreCatalog.availableColors) { hex ->
                            val color = runCatching { Color(android.graphics.Color.parseColor(hex)) }.getOrDefault(Color.White)
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                                    .clickable {
                                        viewModel.updateSectionStyling(targetSecId, colorHex = hex)
                                        showColorPickerForSectionId = null
                                    }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Custom Section Dialog
    if (showAddCustomSectionDialog) {
        var customName by remember { mutableStateOf("") }
        Dialog(onDismissRequest = { showAddCustomSectionDialog = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "افزودن بخش دلخواه",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = customName,
                        onValueChange = { customName = it },
                        placeholder = { Text("مثال: ورس مهمان، دکلمه، هم‌آوایی...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(onClick = { showAddCustomSectionDialog = false }) {
                            Text("انصراف")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            viewModel.addSection("Custom", customTitle = customName.ifBlank { "بخش دلخواه" })
                            showAddCustomSectionDialog = false
                        }) {
                            Text("افزودن")
                        }
                    }
                }
            }
        }
    }

    // Version History Sheet
    if (showVersionHistorySheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showVersionHistorySheet = false },
            sheetState = sheetState
        ) {
            VersionHistorySheetContent(
                versions = versions,
                onSaveCurrentVersion = { name, note ->
                    viewModel.saveVersionSnapshot(name, note)
                },
                onRestoreVersion = { ver ->
                    viewModel.restoreVersion(ver)
                    showVersionHistorySheet = false
                    Toast.makeText(context, "نسخه ${ver.versionName} بازیابی شد", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    // Song Metadata Sheet
    if (showMetadataSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showMetadataSheet = false },
            sheetState = sheetState
        ) {
            SongMetadataSheetContent(
                song = song,
                onSave = { updated ->
                    viewModel.updateSongMetadata(updated)
                    showMetadataSheet = false
                    Toast.makeText(context, "اطلاعات پروژه ذخیره شد", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

@Composable
fun SectionEditorCard(
    section: SongSection,
    isFirst: Boolean,
    isLast: Boolean,
    isRecording: Boolean,
    isPlaying: Boolean,
    playbackProgress: Float,
    onContentChange: (String) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit,
    onOpenColorPicker: () -> Unit,
    onToggleBold: () -> Unit,
    onToggleItalic: () -> Unit,
    onToggleAlign: () -> Unit,
    onCopySection: () -> Unit,
    onStartRecord: () -> Unit,
    onStopRecord: () -> Unit,
    onPlayAudio: () -> Unit,
    onDeleteAudio: () -> Unit
) {
    val strings = LocalStudioStrings.current
    val secColor = runCatching {
        Color(android.graphics.Color.parseColor(section.colorHex))
    }.getOrDefault(MaterialTheme.colorScheme.primary)

    val textAlign = when (section.textAlign) {
        "Center" -> TextAlign.Center
        "Left" -> TextAlign.Start
        else -> TextAlign.End // RTL default
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, secColor.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            .padding(12.dp)
            .testTag("section_card_${section.id}")
    ) {
        Column {
            // Section Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Section Title badge with color
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(secColor.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                            .border(1.dp, secColor, RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = section.getDisplayName(strings),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = secColor
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = onOpenColorPicker, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.ColorLens, contentDescription = null, tint = secColor, modifier = Modifier.size(18.dp))
                    }
                }

                // Controls row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleBold, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.FormatBold,
                            contentDescription = null,
                            tint = if (section.isBold) secColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(onClick = onToggleItalic, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.FormatItalic,
                            contentDescription = null,
                            tint = if (section.isItalic) secColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(onClick = onToggleAlign, modifier = Modifier.size(32.dp)) {
                        Icon(
                            if (section.textAlign == "Center") Icons.Default.FormatAlignCenter else Icons.Default.FormatAlignRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    if (!isFirst) {
                        IconButton(onClick = onMoveUp, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }

                    if (!isLast) {
                        IconButton(onClick = onMoveDown, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.ArrowDownward, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }

                    IconButton(onClick = onCopySection, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.ContentCopy, contentDescription = strings.actionCopySection, modifier = Modifier.size(18.dp))
                    }

                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = strings.actionDelete, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lyrics Text Field
            OutlinedTextField(
                value = section.content,
                onValueChange = onContentChange,
                placeholder = {
                    Text(
                        "${strings.notesLabel}...",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = if (section.isBold) FontWeight.Bold else FontWeight.Normal,
                    fontStyle = if (section.isItalic) FontStyle.Italic else FontStyle.Normal,
                    textAlign = textAlign,
                    color = secColor
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
                    focusedBorderColor = secColor.copy(alpha = 0.4f),
                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("section_input_${section.id}")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Embedded Voice Memo for this specific section (Requirement 16 & 17)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (section.audioFilePath == null) {
                        // Not recorded yet
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isRecording) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = strings.recordingActive,
                                    fontSize = 12.sp,
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = "🎙 ${strings.voiceStudioTitle}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (isRecording) {
                            Button(
                                onClick = onStopRecord,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                shape = RoundedCornerShape(10.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.Stop, contentDescription = strings.actionStopRecord, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(strings.actionStopRecord, fontSize = 11.sp)
                            }
                        } else {
                            IconButton(onClick = onStartRecord) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = strings.actionRecord,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    } else {
                        // Audio recorded
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            IconButton(onClick = onPlayAudio) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) strings.actionPause else strings.actionPlay,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Column(modifier = Modifier.weight(1f).padding(horizontal = 6.dp)) {
                                Text(
                                    text = if (isPlaying) strings.actionPlay else strings.voiceStudioTitle,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (isPlaying) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    LinearProgressIndicator(
                                        progress = { playbackProgress },
                                        modifier = Modifier.fillMaxWidth().height(4.dp)
                                    )
                                }
                            }
                        }

                        IconButton(onClick = onDeleteAudio) {
                            Icon(Icons.Default.Delete, contentDescription = strings.actionDelete, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VersionHistorySheetContent(
    versions: List<SongVersion>,
    onSaveCurrentVersion: (name: String, note: String) -> Unit,
    onRestoreVersion: (SongVersion) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()) }
    var versionName by remember { mutableStateOf("") }
    var versionNote by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "🕒 تاریخچه نسخه‌های ترانه",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "می‌توانید در هر مرحله نسخه‌ای ذخیره کرده و در صورت نیاز به آن برگردید",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Save current snapshot
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = versionName,
                onValueChange = { versionName = it },
                placeholder = { Text("نام نسخه (مثال: نسخه اولیه)", fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    onSaveCurrentVersion(versionName.ifBlank { "نسخه ${versions.size + 1}" }, versionNote)
                    versionName = ""
                    versionNote = ""
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("ثبت نسخه")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (versions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("هنوز نسخه‌ای ثبت نشده است", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().height(260.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(versions) { ver ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = ver.versionName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = dateFormat.format(Date(ver.timestamp)),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            OutlinedButton(
                                onClick = { onRestoreVersion(ver) },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("بازیابی این نسخه", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SongMetadataSheetContent(
    song: Song,
    onSave: (Song) -> Unit
) {
    var title by remember { mutableStateOf(song.title) }
    var artist by remember { mutableStateOf(song.artist) }
    var genre by remember { mutableStateOf(song.genre) }
    var subgenre by remember { mutableStateOf(song.subgenre) }
    var status by remember { mutableStateOf(song.status) }
    var bpm by remember { mutableIntStateOf(song.bpm) }
    var timeSignature by remember { mutableStateOf(song.timeSignature) }
    var tags by remember { mutableStateOf(song.tags) }
    var notes by remember { mutableStateOf(song.notes) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Text(
            text = "ℹ️ شناسنامه و مشخصات قطعه",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("عنوان ترانه") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = artist,
            onValueChange = { artist = it },
            label = { Text("نام خواننده یا آهنگساز") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = genre,
                onValueChange = { genre = it },
                label = { Text("سبک") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = subgenre,
                onValueChange = { subgenre = it },
                label = { Text("زیرسبک") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = bpm.toString(),
                onValueChange = { bpm = it.toIntOrNull() ?: 120 },
                label = { Text("BPM") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = timeSignature,
                onValueChange = { timeSignature = it },
                label = { Text("میزان‌نما") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("برچسب‌ها (با کاما)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("یادداشت‌های تنظیم و ملودی") },
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSave(
                    song.copy(
                        title = title,
                        artist = artist,
                        genre = genre,
                        subgenre = subgenre,
                        status = status,
                        bpm = bpm,
                        timeSignature = timeSignature,
                        tags = tags,
                        notes = notes
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("ذخیره تغییرات مشخصات")
        }
    }
}
