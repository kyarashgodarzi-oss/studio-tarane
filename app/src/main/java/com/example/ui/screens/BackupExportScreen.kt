package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.example.ui.MainViewModel
import com.example.ui.localization.LocalStudioStrings
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BackupExportScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val strings = LocalStudioStrings.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val activeSongs by viewModel.activeSongs.collectAsState()
    var showRestoreDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .verticalScroll(rememberScrollState())
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.cancel)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = strings.backupTitle,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = strings.backupSubtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        // Summary Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Column {
                Text(
                    text = "وضعیت دفترچه محلی:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "تعداد کل ترانه‌های فعال: ${activeSongs.size} قطعه",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "اطلاعات به صورت ۱۰۰٪ آفلاین و محلی بر روی حافظه دستگاه ذخیره می‌شوند. برای جلوگیری از فقدان اطلاعات، نسخه پشتیبان تهیه کنید.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "عملیات فایل و پشتیبان",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Export .taraneh project
            BackupActionTile(
                title = strings.exportTaraneh,
                subtitle = strings.backupSubtitle,
                icon = Icons.Default.FolderZip,
                accentColor = Color(0xFFA855F7),
                onClick = {
                    scope.launch {
                        val json = viewModel.exportBackupJson()
                        val fileName = "StudioTaraneh_Backup_${SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())}.taraneh"
                        val file = File(context.filesDir, fileName)
                        file.writeText(json)

                        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/json"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            putExtra(Intent.EXTRA_SUBJECT, strings.exportTaraneh)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(sendIntent, strings.exportTaraneh))
                    }
                }
            )

            // 2. Export all lyrics to plain text (.txt)
            BackupActionTile(
                title = strings.exportTxt,
                subtitle = strings.exportTxt,
                icon = Icons.Default.Description,
                accentColor = Color(0xFF38BDF8),
                onClick = {
                    scope.launch {
                        val builder = StringBuilder()
                        builder.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
                        builder.append("   ${strings.appName}\n")
                        builder.append("   ${strings.creatorName}\n")
                        builder.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n")

                        for (song in activeSongs) {
                            val full = com.example.data.local.AppDatabase.getDatabase(context).songDao().getSongWithSectionsDirect(song.id)
                            if (full != null) {
                                builder.append(full.getFullLyricsText()).append("\n\n")
                                builder.append("════════════════════════════\n\n")
                            }
                        }

                        val fileName = "StudioTaraneh_Lyrics_${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())}.txt"
                        val file = File(context.filesDir, fileName)
                        file.writeText(builder.toString())

                        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            putExtra(Intent.EXTRA_SUBJECT, strings.exportTxt)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(sendIntent, strings.exportTxt))
                    }
                }
            )

            // 3. Share direct backup JSON code
            BackupActionTile(
                title = strings.shareProjectJson,
                subtitle = strings.shareProjectJson,
                icon = Icons.Default.Share,
                accentColor = Color(0xFFEC4899),
                onClick = {
                    scope.launch {
                        val json = viewModel.exportBackupJson()
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, json)
                            putExtra(Intent.EXTRA_SUBJECT, strings.shareProjectJson)
                        }
                        context.startActivity(Intent.createChooser(sendIntent, strings.shareProjectJson))
                    }
                }
            )

            // 4. Restore from .taraneh / JSON
            BackupActionTile(
                title = strings.restoreProject,
                subtitle = strings.restoreProject,
                icon = Icons.Default.CloudDownload,
                accentColor = Color(0xFF10B981),
                onClick = { showRestoreDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))
    }

    if (showRestoreDialog) {
        var restoreText by remember { mutableStateOf("") }
        Dialog(onDismissRequest = { showRestoreDialog = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "وارد کردن پروژه یا متن پشتیبان",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "محتوای فایل .taraneh یا JSON پشتیبان را اینجا جای‌گذاری (Paste) کنید:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = restoreText,
                        onValueChange = { restoreText = it },
                        placeholder = { Text("متن پشتیبان را اینجا وارد کنید...") },
                        maxLines = 6,
                        modifier = Modifier.fillMaxWidth().height(140.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    val count = viewModel.importBackupJson(restoreText)
                                    showRestoreDialog = false
                                    if (count > 0) {
                                        Toast.makeText(context, "$count ترانه با موفقیت بازیابی شد", Toast.LENGTH_LONG).show()
                                    } else {
                                        Toast.makeText(context, "خطا در قالب فایل پشتیبان", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        ) {
                            Text("شروع بازیابی")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BackupActionTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = accentColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
