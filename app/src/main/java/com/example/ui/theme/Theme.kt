package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.preferences.AppThemeMode
import com.example.data.preferences.DayNightOption
import com.example.data.preferences.EditorFontSize

fun getThemeColorScheme(themeMode: AppThemeMode, isDark: Boolean): ColorScheme {
    if (!isDark) {
        return lightColorScheme(
            primary = when (themeMode) {
                AppThemeMode.NEON_STUDIO -> NeonStudioPrimary
                AppThemeMode.MIDNIGHT -> MidnightPrimary
                AppThemeMode.GRAPHITE -> Color(0xFF475569)
                AppThemeMode.PURPLE_NIGHT -> PurpleNightSecondary
                AppThemeMode.SILVER_STUDIO -> Color(0xFF334155)
                AppThemeMode.AMOLED -> Color(0xFF6B21A8)
            },
            secondary = when (themeMode) {
                AppThemeMode.NEON_STUDIO -> NeonStudioSecondary
                AppThemeMode.MIDNIGHT -> MidnightSecondary
                AppThemeMode.GRAPHITE -> GraphitePrimary
                AppThemeMode.PURPLE_NIGHT -> PurpleNightPrimary
                AppThemeMode.SILVER_STUDIO -> SilverStudioSecondary
                AppThemeMode.AMOLED -> AmoledSecondary
            },
            tertiary = NeonStudioTertiary,
            background = LightModeBackground,
            surface = LightModeSurface,
            surfaceVariant = LightModeSurfaceVariant,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = LightModeOnBackground,
            onSurface = LightModeOnBackground,
            onSurfaceVariant = Color(0xFF334155)
        )
    }

    return when (themeMode) {
        AppThemeMode.NEON_STUDIO -> darkColorScheme(
            primary = NeonStudioPrimary,
            secondary = NeonStudioSecondary,
            tertiary = NeonStudioTertiary,
            background = NeonStudioBackground,
            surface = NeonStudioSurface,
            surfaceVariant = NeonStudioSurfaceVariant,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFFF3F4F6),
            onSurface = Color(0xFFF3F4F6),
            onSurfaceVariant = Color(0xFF9CA3AF)
        )
        AppThemeMode.MIDNIGHT -> darkColorScheme(
            primary = MidnightPrimary,
            secondary = MidnightSecondary,
            tertiary = MidnightTertiary,
            background = MidnightBackground,
            surface = MidnightSurface,
            surfaceVariant = MidnightSurfaceVariant,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFFF1F5F9),
            onSurface = Color(0xFFF1F5F9),
            onSurfaceVariant = Color(0xFF94A3B8)
        )
        AppThemeMode.GRAPHITE -> darkColorScheme(
            primary = GraphitePrimary,
            secondary = GraphiteSecondary,
            tertiary = GraphiteTertiary,
            background = GraphiteBackground,
            surface = GraphiteSurface,
            surfaceVariant = GraphiteSurfaceVariant,
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onBackground = Color(0xFFE2E8F0),
            onSurface = Color(0xFFE2E8F0),
            onSurfaceVariant = Color(0xFF94A3B8)
        )
        AppThemeMode.PURPLE_NIGHT -> darkColorScheme(
            primary = PurpleNightPrimary,
            secondary = PurpleNightSecondary,
            tertiary = PurpleNightTertiary,
            background = PurpleNightBackground,
            surface = PurpleNightSurface,
            surfaceVariant = PurpleNightSurfaceVariant,
            onPrimary = Color.Black,
            onSecondary = Color.White,
            onBackground = Color(0xFFFAF5FF),
            onSurface = Color(0xFFFAF5FF),
            onSurfaceVariant = Color(0xFFD8B4FE)
        )
        AppThemeMode.SILVER_STUDIO -> darkColorScheme(
            primary = SilverStudioPrimary,
            secondary = SilverStudioSecondary,
            tertiary = SilverStudioTertiary,
            background = SilverStudioBackground,
            surface = SilverStudioSurface,
            surfaceVariant = SilverStudioSurfaceVariant,
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onBackground = Color(0xFFF8FAFC),
            onSurface = Color(0xFFF8FAFC),
            onSurfaceVariant = Color(0xFFA1A1AA)
        )
        AppThemeMode.AMOLED -> darkColorScheme(
            primary = AmoledPrimary,
            secondary = AmoledSecondary,
            tertiary = AmoledTertiary,
            background = AmoledBackground,
            surface = AmoledSurface,
            surfaceVariant = AmoledSurfaceVariant,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceVariant = Color(0xFFA3A3A3)
        )
    }
}

@Composable
fun StudioTaranehTheme(
    themeMode: AppThemeMode = AppThemeMode.NEON_STUDIO,
    dayNightOption: DayNightOption = DayNightOption.DARK,
    fontSize: EditorFontSize = EditorFontSize.MEDIUM,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (dayNightOption) {
        DayNightOption.DARK -> true
        DayNightOption.LIGHT -> false
        DayNightOption.SYSTEM -> isSystemDark
    }

    val colorScheme = getThemeColorScheme(themeMode, isDark)
    val typography = getStudioTypography(fontSize)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
