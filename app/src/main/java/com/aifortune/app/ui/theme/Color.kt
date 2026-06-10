package com.aifortune.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ============================================================
// AI Fortune - 液态玻璃设计系统
// 高级感 · 东方神秘 · 丝滑动效
// ============================================================

// 核心色彩
val DeepInk = Color(0xFF0A0E1A)           // 深墨背景
val PrimaryIndigo = Color(0xFF5B67CA)     // 主色-靛蓝（兼容旧代码）
val PrimaryViolet = Color(0xFF8B5CF6)     // 主色-紫色（兼容旧代码）
val LiquidGlassBase = Color(0xFF1A1A2E)  // 玻璃底层
val LiquidGlassHighlight = Color(0xFF2A2A4E) // 玻璃高光
val Crimson = Color(0xFFC41E3A)            // 朱砂点缀
val MoltenGold = Color(0xFFD4AF37)         // 流金光泽
val Amber = Color(0xFFF5A623)              // 琥珀暖光
val Ivory = Color(0xFFF5F5DC)             // 象牙白文字
val SmokeGray = Color(0xFF8B8B8B)         // 烟灰次要文字
val NebulaPurple = Color(0xFF6B5B95)      // 星云紫

// 渐变色彩
val GoldShimmer = Color(0xFFE8D5A0)       // 金色微光
val RoseGold = Color(0xFFB76E79)          // 玫瑰金

// 亮色主题（保持原样，专注深色）
val LightBackground = Color(0xFFFAFAFA)
val LightSurface = Color(0xFFFFFFFF)
val LightText = Color(0xFF1E293B)
val LightTextSecondary = Color(0xFF64748B)

// 深色主题 - 液态玻璃版
private val DarkColorScheme = darkColorScheme(
    primary = MoltenGold,
    secondary = Crimson,
    tertiary = Amber,
    background = DeepInk,
    surface = LiquidGlassBase,
    surfaceVariant = LiquidGlassHighlight,
    onPrimary = DeepInk,
    onSecondary = Ivory,
    onTertiary = DeepInk,
    onBackground = Ivory,
    onSurface = Ivory,
    onSurfaceVariant = SmokeGray
)

// 亮色主题（简化）
private val LightColorScheme = lightColorScheme(
    primary = Crimson,
    secondary = NebulaPurple,
    tertiary = MoltenGold,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = Color(0xFFF1F5F9),
    onPrimary = Ivory,
    onSecondary = Ivory,
    onTertiary = DeepInk,
    onBackground = LightText,
    onSurface = LightText,
    onSurfaceVariant = LightTextSecondary
)

@Composable
fun AIFortuneTheme(
    darkTheme: Boolean = true, // 默认深色，配合液态玻璃
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DeepInk.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
