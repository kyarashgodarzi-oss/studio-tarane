package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.example.R
import com.example.ui.MainViewModel
import com.example.ads.TapsellNativeVideo
import com.example.ui.localization.LocalStudioStrings

data class HomeMenuItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color,
    val badge: String? = null
)

@Composable
fun MainHomeScreen(
    viewModel: MainViewModel,
    onNavigate: (screenRoute: String) -> Unit
) {
    val strings = LocalStudioStrings.current
    val activeSongs by viewModel.activeSongs.collectAsState()
    val recordings by viewModel.voiceRecordings.collectAsState()
    val favorites by viewModel.favoriteSongs.collectAsState()
    val trashSongs by viewModel.trashSongs.collectAsState()
    val isVip by viewModel.preferences.isVip.collectAsState()
    val context = LocalContext.current

    val menuItems = listOf(
        HomeMenuItem(
            id = "my_songs",
            title = strings.mySongs,
            subtitle = strings.mySongsSubtitle,
            icon = Icons.Default.EditNote,
            accentColor = Color(0xFF38BDF8),
            badge = "${activeSongs.size}"
        ),
        HomeMenuItem(
            id = "new_song",
            title = strings.newSong,
            subtitle = strings.newSongSubtitle,
            icon = Icons.Default.AddCircle,
            accentColor = Color(0xFF10B981)
        ),
        HomeMenuItem(
            id = "voice_recording",
            title = strings.voiceStudio,
            subtitle = strings.voiceStudioSubtitle,
            icon = Icons.Default.Mic,
            accentColor = Color(0xFFEC4899),
            badge = if (recordings.isNotEmpty()) "${recordings.size}" else null
        ),
        HomeMenuItem(
            id = "rhythm_maker",
            title = strings.rhythmHub,
            subtitle = strings.rhythmHubSubtitle,
            icon = Icons.Default.MusicNote,
            accentColor = Color(0xFFA855F7)
        ),
        HomeMenuItem(
            id = "favorites",
            title = strings.favorites,
            subtitle = strings.favoritesSubtitle,
            icon = Icons.Default.Star,
            accentColor = Color(0xFFF59E0B),
            badge = if (favorites.isNotEmpty()) "${favorites.size}" else null
        ),
        HomeMenuItem(
            id = "recent",
            title = strings.recent,
            subtitle = strings.recentSubtitle,
            icon = Icons.Default.History,
            accentColor = Color(0xFF06B6D4)
        ),
        HomeMenuItem(
            id = "trash",
            title = strings.trash,
            subtitle = strings.trashSubtitle,
            icon = Icons.Default.Delete,
            accentColor = Color(0xFFEF4444),
            badge = if (trashSongs.isNotEmpty()) "${trashSongs.size}" else null
        ),
        HomeMenuItem(
            id = "backup",
            title = strings.backupExport,
            subtitle = strings.backupSubtitle,
            icon = Icons.Default.Backup,
            accentColor = Color(0xFF6366F1)
        ),
        HomeMenuItem(
            id = "vip",
            title = strings.vipHubTitle,
            subtitle = if (isVip) strings.vipActiveBadge else strings.vipHubSubtitle,
            icon = Icons.Default.AutoAwesome,
            accentColor = Color(0xFFFFD700),
            badge = if (isVip) "VIP" else null
        ),
        HomeMenuItem(
            id = "settings",
            title = strings.settingsTitle,
            subtitle = strings.settingsSubtitle,
            icon = Icons.Default.Settings,
            accentColor = Color(0xFF94A3B8)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // Studio Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_studio_logo),
                            contentDescription = strings.appName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = strings.appName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${strings.versionName} • ${strings.creatorName}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (isVip) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFD700).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "👑 VIP",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
            }
        }

        // Section Title
        Text(
            text = strings.mainSectionsTitle,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        // 2-Column Clean Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .weight(1f)
                .testTag("main_menu_grid")
        ) {
            items(menuItems) { item ->
                StudioMenuCard(
                    item = item,
                    onClick = { onNavigate(item.id) }
                )
            }

            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                TapsellNativeVideo(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                )
            }
        }

    }
}

@Composable
fun StudioMenuCard(
    item: HomeMenuItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = item.accentColor.copy(alpha = 0.35f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
            .testTag("menu_card_${item.id}")
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(item.accentColor.copy(alpha = 0.18f), RoundedCornerShape(12.dp))
                        .border(1.dp, item.accentColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = item.accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (item.badge != null) {
                    Box(
                        modifier = Modifier
                            .background(item.accentColor.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = item.badge,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = item.accentColor
                        )
                    }
                }
            }

            Column {
                Text(
                    text = item.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}
