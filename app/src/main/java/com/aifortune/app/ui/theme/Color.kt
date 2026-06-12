package com.aifortune.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ============================================================
// AI Fortune - 主题入口
// 使用 ThemeManager 的动态配色方案
// ============================================================

@Composable
fun AIFortuneTheme(
    config: ThemeConfig = ThemeConfig(),
    content: @Composable () -> Unit
) {
    val palette = ThemePalettes[config.variant] ?: ThemePalettes[ThemeVariant.DEEP_INK]!!
    val colorScheme = getColorScheme(palette, config.isDark)
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = palette.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    CompositionLocalProvider(LocalThemePalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}