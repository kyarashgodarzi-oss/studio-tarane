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

    // Preferences can outlive app versions. Handle unknown enum names and values stored
    // under the wrong type so corrupt/legacy settings cannot crash ViewModel creation.
    private inline fun <reified T : Enum<T>> readEnumPreference(
        key: String,
        defaultValue: T
    ): T {
        val storedValue = try {
            prefs.getString(key, null)
        } catch (_: ClassCastException) {
            null
        }
        return storedValue?.let { value ->
            enumValues<T>().firstOrNull { it.name == value }
        } ?: defaultValue
    }

    private val _themeMode = MutableStateFlow(
        readEnumPreference("theme_mode", AppThemeMode.NEON_STUDIO)
    )
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _dayNight = MutableStateFlow(
        readEnumPreference("day_night", DayNightOption.DARK)
    )
    val dayNight: StateFlow<DayNightOption> = _dayNight.asStateFlow()

    private val _fontSize = MutableStateFlow(
        readEnumPreference("font_size", EditorFontSize.MEDIUM)
    )
    val fontSize: StateFlow<EditorFontSize> = _fontSize.asStateFlow()

    // Default language is Persian until the user explicitly selects another language.
    private fun readBooleanPreference(key: String, defaultValue: Boolean): Boolean {
        return try {
            prefs.getBoolean(key, defaultValue)
        } catch (_: ClassCastException) {
            defaultValue
        }
    }

    private fun readStringPreference(key: String, defaultValue: String): String {
        return try {
            prefs.getString(key, defaultValue) ?: defaultValue
        } catch (_: ClassCastException) {
            defaultValue
        }
    }

    private val _language = MutableStateFlow(
        if (!readBooleanPreference("has_user_selected_language", false)) {
            AppLanguage.PERSIAN
        } else {
            AppLanguage.fromCode(readStringPreference("app_language", "fa"))
        }
    )
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _isVip = MutableStateFlow(readBooleanPreference("is_vip_user", false))
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
