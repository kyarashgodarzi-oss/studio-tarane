package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.ui.MainViewModel
import com.example.ads.TapsellAds
import com.example.billing.BazaarBillingManager
import com.example.ui.localization.LocalStudioStrings
import com.example.ui.localization.getStudioStrings
import com.example.ui.screens.BackupExportScreen
import com.example.ui.screens.MainHomeScreen
import com.example.ui.screens.NewSongDialog
import com.example.ui.screens.RhythmMetronomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SongEditorScreen
import com.example.ui.screens.SongListScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TrashScreen
import com.example.ui.screens.VipScreen
import com.example.ui.screens.VoiceStudioScreen
import com.example.ui.theme.StudioTaranehTheme

sealed class Screen {
    object Splash : Screen()
    object Home : Screen()
    object AllSongs : Screen()
    object FavoriteSongs : Screen()
    object RecentSongs : Screen()
    data class SongEditor(val songId: Long) : Screen()
    object VoiceStudio : Screen()
    object RhythmMetronome : Screen()
    object Trash : Screen()
    object Backup : Screen()
    object Settings : Screen()
    object Vip : Screen()
}

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var billingManager: BazaarBillingManager

    override fun attachBaseContext(newBase: android.content.Context) {
        val prefs = newBase.getSharedPreferences("studio_taraneh_prefs", android.content.Context.MODE_PRIVATE)
        val hasUserSelected = prefs.getBoolean("has_user_selected_language", false)
        val langCode = if (hasUserSelected) prefs.getString("app_language", "fa") ?: "fa" else "fa"
        val locale = when (langCode.lowercase()) {
            "fa", "persian" -> java.util.Locale("fa", "IR")
            "ar", "arabic" -> java.util.Locale("ar")
            "tr", "turkish" -> java.util.Locale("tr")
            "es", "spanish" -> java.util.Locale("es")
            "fr", "french" -> java.util.Locale("fr")
            "de", "german" -> java.util.Locale("de")
            "ru", "russian" -> java.util.Locale("ru")
            "en", "english" -> java.util.Locale.ENGLISH
            else -> java.util.Locale("fa", "IR")
        }
        java.util.Locale.setDefault(locale)
        val config = android.content.res.Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TapsellAds.initialize(this)
        billingManager = BazaarBillingManager(
            context = this,
            onVipChanged = { isVip -> viewModel.preferences.setVip(isVip) },
            onMessage = { message ->
                android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
            }
        )
        billingManager.connect()

        java.util.Locale.setDefault(java.util.Locale("fa", "IR"))
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.preferences.themeMode.collectAsState()
            val dayNight by viewModel.preferences.dayNight.collectAsState()
            val fontSize by viewModel.preferences.fontSize.collectAsState()
            val language by viewModel.preferences.language.collectAsState()

            androidx.compose.runtime.LaunchedEffect(language) {
                val locale = when (language) {
                    com.example.data.preferences.AppLanguage.PERSIAN -> java.util.Locale("fa", "IR")
                    com.example.data.preferences.AppLanguage.ARABIC -> java.util.Locale("ar")
                    com.example.data.preferences.AppLanguage.TURKISH -> java.util.Locale("tr")
                    com.example.data.preferences.AppLanguage.SPANISH -> java.util.Locale("es")
                    com.example.data.preferences.AppLanguage.FRENCH -> java.util.Locale("fr")
                    com.example.data.preferences.AppLanguage.GERMAN -> java.util.Locale("de")
                    com.example.data.preferences.AppLanguage.RUSSIAN -> java.util.Locale("ru")
                    com.example.data.preferences.AppLanguage.ENGLISH -> java.util.Locale.ENGLISH
                }
                java.util.Locale.setDefault(locale)
            }

            StudioTaranehTheme(
                themeMode = themeMode,
                dayNightOption = dayNight,
                fontSize = fontSize
            ) {
                val currentStrings = getStudioStrings(language)
                val layoutDir = if (language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

                CompositionLocalProvider(
                    LocalLayoutDirection provides layoutDir,
                    LocalStudioStrings provides currentStrings
                ) {
                    StudioTaranehApp(viewModel = viewModel, billingManager = billingManager, activity = this@MainActivity)
                }
            }
        }
    }

    override fun onDestroy() {
        if (::billingManager.isInitialized) billingManager.disconnect()
        super.onDestroy()
    }
}

@Composable
fun StudioTaranehApp(
    viewModel: MainViewModel,
    billingManager: BazaarBillingManager,
    activity: ComponentActivity
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }
    var screenStack by remember { mutableStateOf(listOf<Screen>()) }
    var showNewSongDialog by remember { mutableStateOf(false) }

    fun navigateTo(screen: Screen) {
        screenStack = screenStack + currentScreen
        currentScreen = screen
    }
    fun requestRewardedAccess(featureName: String, onGranted: () -> Unit) {
        if (viewModel.preferences.isVip.value) {
            onGranted()
            return
        }
        TapsellAds.showRewarded(
            activity = activity,
            onRewarded = {
                Toast.makeText(activity, "تبلیغ کامل شد؛ $featureName فعال شد", Toast.LENGTH_SHORT).show()
                onGranted()
            },
            onError = { _ ->
                Toast.makeText(activity, "برای استفاده از $featureName باید تبلیغ جایزه‌ای در دسترس باشد", Toast.LENGTH_LONG).show()
            }
        )
    }

    fun navigateBack() {
        if (screenStack.isNotEmpty()) {
            val last = screenStack.last()
            screenStack = screenStack.dropLast(1)
            currentScreen = last
        } else {
            currentScreen = Screen.Home
        }
    }

    fun navigateBackWithInterstitial() {
        navigateBack()
        if (!viewModel.preferences.isVip.value) {
            TapsellAds.showInterstitial(activity)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val screen = currentScreen) {
            is Screen.Splash -> {
                SplashScreen(
                    onSplashFinished = {
                        currentScreen = Screen.Home
                    }
                )
            }
            is Screen.Home -> {
                MainHomeScreen(
                    viewModel = viewModel,
                    onNavigate = { route ->
                        when (route) {
                            "my_songs" -> requestRewardedAccess("ترانه‌های من") { navigateTo(Screen.AllSongs) }
                            "new_song" -> requestRewardedAccess("ایجاد ترانه جدید") { showNewSongDialog = true }
                            "voice_recording" -> requestRewardedAccess("استودیو ضبط صدا") { navigateTo(Screen.VoiceStudio) }
                            "rhythm_maker" -> requestRewardedAccess("ساخت ریتم") { navigateTo(Screen.RhythmMetronome) }
                            "favorites" -> requestRewardedAccess("ترانه‌های موردعلاقه") { navigateTo(Screen.FavoriteSongs) }
                            "recent" -> requestRewardedAccess("ترانه‌های اخیر") { navigateTo(Screen.RecentSongs) }
                            "trash" -> requestRewardedAccess("سطل زباله") { navigateTo(Screen.Trash) }
                            "backup" -> requestRewardedAccess("پشتیبان‌گیری") { navigateTo(Screen.Backup) }
                            "vip" -> navigateTo(Screen.Vip)
                            "settings" -> navigateTo(Screen.Settings)
                        }
                    }
                )
            }
            is Screen.AllSongs -> {
                SongListScreen(
                    viewModel = viewModel,
                    mode = "ALL",
                    onOpenSong = { songId ->
                        requestRewardedAccess("ویرایش ترانه") {
                            viewModel.openSong(songId)
                            navigateTo(Screen.SongEditor(songId))
                        }
                    },
                    onNewSong = { requestRewardedAccess("ایجاد ترانه جدید") { showNewSongDialog = true } },
                    onBack = { navigateBack() }
                )
            }
            is Screen.FavoriteSongs -> {
                SongListScreen(
                    viewModel = viewModel,
                    mode = "FAVORITES",
                    onOpenSong = { songId ->
                        requestRewardedAccess("ویرایش ترانه") {
                            viewModel.openSong(songId)
                            navigateTo(Screen.SongEditor(songId))
                        }
                    },
                    onNewSong = { requestRewardedAccess("ایجاد ترانه جدید") { showNewSongDialog = true } },
                    onBack = { navigateBack() }
                )
            }
            is Screen.RecentSongs -> {
                SongListScreen(
                    viewModel = viewModel,
                    mode = "RECENT",
                    onOpenSong = { songId ->
                        requestRewardedAccess("ویرایش ترانه") {
                            viewModel.openSong(songId)
                            navigateTo(Screen.SongEditor(songId))
                        }
                    },
                    onNewSong = { requestRewardedAccess("ایجاد ترانه جدید") { showNewSongDialog = true } },
                    onBack = { navigateBack() }
                )
            }
            is Screen.SongEditor -> {
                SongEditorScreen(
                    viewModel = viewModel,
                    songId = screen.songId,
                    onBack = { navigateBackWithInterstitial() }
                )
            }
            is Screen.VoiceStudio -> {
                VoiceStudioScreen(
                    viewModel = viewModel,
                    onBack = { navigateBack() }
                )
            }
            is Screen.RhythmMetronome -> {
                RhythmMetronomeScreen(
                    viewModel = viewModel,
                    onBack = { navigateBack() }
                )
            }
            is Screen.Trash -> {
                TrashScreen(
                    viewModel = viewModel,
                    onBack = { navigateBack() }
                )
            }
            is Screen.Backup -> {
                BackupExportScreen(
                    viewModel = viewModel,
                    onBack = { navigateBack() }
                )
            }
            is Screen.Settings -> {
                SettingsScreen(
                    viewModel = viewModel,
                    onNavigateVip = { navigateTo(Screen.Vip) },
                    onBack = { navigateBack() }
                )
            }
            is Screen.Vip -> {
                VipScreen(
                    viewModel = viewModel,
                    billingManager = billingManager,
                    activity = activity,
                    onBack = { navigateBack() }
                )
            }
        }

        if (showNewSongDialog) {
            NewSongDialog(
                onDismiss = { showNewSongDialog = false },
                onCreate = { title, artist, genre, subgenre, bpm, timeSignature, description, tags ->
                    showNewSongDialog = false
                    viewModel.createNewSong(
                        title = title,
                        artist = artist,
                        genre = genre,
                        subgenre = subgenre,
                        bpm = bpm,
                        timeSignature = timeSignature,
                        description = description,
                        tags = tags
                    ) { newSongId ->
                        viewModel.openSong(newSongId)
                        navigateTo(Screen.SongEditor(newSongId))
                    }
                }
            )
        }
    }
}
