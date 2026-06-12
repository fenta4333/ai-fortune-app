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

// ============================================================
// 向后兼容别名（供 CommonComponents / LiquidGlassComponents 等沿用）
// ============================================================

// CommonComponents
val PrimaryIndigo get() = ThemePalettes[ThemeVariant.DEEP_INK]!!.gradientStart
val PrimaryViolet get() = ThemePalettes[ThemeVariant.DEEP_INK]!!.gradientEnd

// LiquidGlassComponents 颜色常量
val LiquidGlassHighlight get() = ThemePalettes[ThemeVariant.DEEP_INK]!!.gradientStart    // 靛蓝
val LiquidGlassBase get() = ThemePalettes[ThemeVariant.DEEP_INK]!!.surfaceVariant         // 深靛
val MoltenGold get() = ThemePalettes[ThemeVariant.DEEP_INK]!!.primary                     // 流金
val Crimson get() = ThemePalettes[ThemeVariant.DEEP_INK]!!.secondary                      // 朱砂
val Amber get() = ThemePalettes[ThemeVariant.DEEP_INK]!!.tertiary                         // 琥珀
val SmokeGray get() = ThemePalettes[ThemeVariant.DEEP_INK]!!.onSurfaceVariant             // 灰
val DeepInk get() = ThemePalettes[ThemeVariant.DEEP_INK]!!.background                     // 深墨
val NebulaPurple get() = ThemePalettes[ThemeVariant.DEEP_INK]!!.gradientEnd               // 紫
val Ivory get() = ThemePalettes[ThemeVariant.DEEP_INK]!!.onBackground                     // 象牙

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