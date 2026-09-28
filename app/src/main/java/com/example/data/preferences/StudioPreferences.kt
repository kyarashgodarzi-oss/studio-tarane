package com.example.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppThemeMode {
    NEON_STUDIO,
    MIDNIGHT,
    GRAPHITE,
    PURPLE_NIGHT,
    SILVER_STUDIO,
    AMOLED
}

enum class DayNightOption {
    SYSTEM,
    DARK,
    LIGHT
}

enum class EditorFontSize(
    val scaleFactor: Float,
    val spSize: Float,
    val labelFa: String,
    val labelEn: String
) {
    SMALL(0.85f, 13f, "کوچک", "Small"),
    MEDIUM(1.0f, 16f, "متوسط", "Medium"),
    LARGE(1.2f, 19f, "بزرگ", "Large"),
    EXTRA_LARGE(1.4f, 22f, "خیلی بزرگ", "Extra Large")
}

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val flag: String,
    val isRtl: Boolean
) {
    PERSIAN("fa", "فارسی (Persian)", "🇮🇷", true),
    ENGLISH("en", "English", "🇺🇸", false),
    ARABIC("ar", "العربية (Arabic)", "🇸🇦", true),
    TURKISH("tr", "Türkçe (Turkish)", "🇹🇷", false),
    SPANISH("es", "Español (Spanish)", "🇪🇸", false),
    FRENCH("fr", "Français (French)", "🇫🇷", false),
    GERMAN("de", "Deutsch (German)", "🇩🇪", false),
    RUSSIAN("ru", "Русский (Russian)", "🇷🇺", false);

    companion object {
        fun fromCode(code: String?): AppLanguage {
            if (code.isNullOrBlank()) return PERSIAN
            return entries.firstOrNull {
                it.code.equals(code, ignoreCase = true) || it.name.equals(code, ignoreCase = true)
            } ?: PERSIAN
        }
    }
}

class StudioPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("studio_taraneh_prefs", Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(
        AppThemeMode.valueOf(prefs.getString("theme_mode", AppThemeMode.NEON_STUDIO.name) ?: AppThemeMode.NEON_STUDIO.name)
    )
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _dayNight = MutableStateFlow(
        DayNightOption.valueOf(prefs.getString("day_night", DayNightOption.DARK.name) ?: DayNightOption.DARK.name)
    )
    val dayNight: StateFlow<DayNightOption> = _dayNight.asStateFlow()

    private val _fontSize = MutableStateFlow(
        EditorFontSize.valueOf(prefs.getString("font_size", EditorFontSize.MEDIUM.name) ?: EditorFontSize.MEDIUM.name)
    )
    val fontSize: StateFlow<EditorFontSize> = _fontSize.asStateFlow()

    // Default language is ALWAYS Persian (fa) - Requirement 30 & Mandatory fix
    // Key MUST return "fa", absolutely no fallback to "en"
    private val _language = MutableStateFlow(
        if (!prefs.getBoolean("has_user_selected_language", false)) {
            AppLanguage.PERSIAN
        } else {
            AppLanguage.fromCode(prefs.getString("app_language", "fa"))
        }
    )
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _isVip = MutableStateFlow(prefs.getBoolean("is_vip_user", false))
    val isVip: StateFlow<Boolean> = _isVip.asStateFlow()

    fun setThemeMode(mode: AppThemeMode) {
        prefs.edit().putString("theme_mode", mode.name).apply()
        _themeMode.value = mode
    }

    fun setDayNight(option: DayNightOption) {
        prefs.edit().putString("day_night", option.name).apply()
        _dayNight.value = option
    }

    fun setFontSize(size: EditorFontSize) {
        prefs.edit().putString("font_size", size.name).apply()
        _fontSize.value = size
    }

    fun setLanguage(lang: AppLanguage) {
        prefs.edit()
            .putString("app_language", lang.code)
            .putBoolean("has_user_selected_language", true)
            .apply()
        _language.value = lang
    }

    fun setVip(vip: Boolean) {
        prefs.edit().putBoolean("is_vip_user", vip).apply()
        _isVip.value = vip
    }
}
