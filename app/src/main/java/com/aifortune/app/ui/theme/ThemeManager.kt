package com.aifortune.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ============================================================
// AI Fortune - 多配色主题系统
// 支持 6 种配色方案：深邃墨黑(默认) | 琥珀暖阳 | 翡翠绿意
//            苍蓝之海 | 玫瑰星云 | 鎏金岁月
// ============================================================

// ---- 配色方案枚举 ----
enum class ThemeVariant(val label: String, val labelEn: String, val icon: String) {
    DEEP_INK("深邃墨黑", "Deep Ink", "🌌"),
    AMBER_SUN("琥珀暖阳", "Amber Sun", "☀️"),
    JADE_GREEN("翡翠绿意", "Jade Green", "🌿"),
    OCEAN_BLUE("苍蓝之海", "Ocean Blue", "🌊"),
    ROSE_NEBULA("玫瑰星云", "Rose Nebula", "🌸"),
    GOLDEN_AGE("鎏金岁月", "Golden Age", "✨")
}

// ---- 各配色核心色板 ----
data class ThemePalette(
    val name: ThemeVariant,
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val onPrimary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,
    val gradientStart: Color,
    val gradientEnd: Color,
    val accent: Color,
    val cardGradientStart: Color,
    val cardGradientEnd: Color
)

// ---- 所有配色方案定义 ----
val ThemePalettes = mapOf(
    ThemeVariant.DEEP_INK to ThemePalette(
        name = ThemeVariant.DEEP_INK,
        primary = Color(0xFFD4AF37),        // 流金
        secondary = Color(0xFFC41E3A),      // 朱砂
        tertiary = Color(0xFFF5A623),       // 琥珀
        background = Color(0xFF0A0E1A),     // 深墨
        surface = Color(0xFF1A1A2E),        // 玻璃底
        surfaceVariant = Color(0xFF2A2A4E), // 玻璃高光
        onPrimary = Color(0xFF0A0E1A),
        onBackground = Color(0xFFF5F5DC),   // 象牙
        onSurface = Color(0xFFF5F5DC),
        onSurfaceVariant = Color(0xFF8B8B8B),
        gradientStart = Color(0xFF5B67CA),  // 靛蓝
        gradientEnd = Color(0xFF8B5CF6),    // 紫
        accent = Color(0xFFB76E79),         // 玫瑰金
        cardGradientStart = Color(0xFF2A2A4E),
        cardGradientEnd = Color(0xFF1A1A2E)
    ),
    ThemeVariant.AMBER_SUN to ThemePalette(
        name = ThemeVariant.AMBER_SUN,
        primary = Color(0xFFE8841A),        // 橙金
        secondary = Color(0xFFD35400),      // 深橙
        tertiary = Color(0xFFF39C12),       // 亮橙
        background = Color(0xFF1C150D),     // 暖黑
        surface = Color(0xFF2C2215),        // 暖褐
        surfaceVariant = Color(0xFF3D3020),
        onPrimary = Color(0xFFFFFFFF),
        onBackground = Color(0xFFFDF2E9),
        onSurface = Color(0xFFFDF2E9),
        onSurfaceVariant = Color(0xFFA09080),
        gradientStart = Color(0xFFE8841A),
        gradientEnd = Color(0xFFD35400),
        accent = Color(0xFFF1C40F),
        cardGradientStart = Color(0xFF3D3020),
        cardGradientEnd = Color(0xFF2C2215)
    ),
    ThemeVariant.JADE_GREEN to ThemePalette(
        name = ThemeVariant.JADE_GREEN,
        primary = Color(0xFF2ECC71),        // 翡翠
        secondary = Color(0xFF1ABC9C),      // 青绿
        tertiary = Color(0xFF27AE60),       // 深绿
        background = Color(0xFF0D1A12),     // 墨绿底
        surface = Color(0xFF1A2E20),
        surfaceVariant = Color(0xFF264033),
        onPrimary = Color(0xFF0D1A12),
        onBackground = Color(0xFFE8F8F0),
        onSurface = Color(0xFFE8F8F0),
        onSurfaceVariant = Color(0xFF7D9B8A),
        gradientStart = Color(0xFF2ECC71),
        gradientEnd = Color(0xFF1ABC9C),
        accent = Color(0xFFA8E6CF),
        cardGradientStart = Color(0xFF264033),
        cardGradientEnd = Color(0xFF1A2E20)
    ),
    ThemeVariant.OCEAN_BLUE to ThemePalette(
        name = ThemeVariant.OCEAN_BLUE,
        primary = Color(0xFF3498DB),        // 海蓝
        secondary = Color(0xFF2980B9),      // 深蓝
        tertiary = Color(0xFF5DADE2),       // 天蓝
        background = Color(0xFF0D141F),     // 深海
        surface = Color(0xFF162032),
        surfaceVariant = Color(0xFF1E2D45),
        onPrimary = Color(0xFFFFFFFF),
        onBackground = Color(0xFFEBF5FB),
        onSurface = Color(0xFFEBF5FB),
        onSurfaceVariant = Color(0xFF7F8FA0),
        gradientStart = Color(0xFF3498DB),
        gradientEnd = Color(0xFF2C3E50),
        accent = Color(0xFF85C1E9),
        cardGradientStart = Color(0xFF1E2D45),
        cardGradientEnd = Color(0xFF162032)
    ),
    ThemeVariant.ROSE_NEBULA to ThemePalette(
        name = ThemeVariant.ROSE_NEBULA,
        primary = Color(0xFFE91E90),        // 玫瑰粉
        secondary = Color(0xFF9B59B6),      // 紫罗兰
        tertiary = Color(0xFFE74C3C),       // 珊瑚红
        background = Color(0xFF1A0F1A),     // 紫黑
        surface = Color(0xFF2A1A2E),
        surfaceVariant = Color(0xFF3D2545),
        onPrimary = Color(0xFFFFFFFF),
        onBackground = Color(0xFFFDE8F5),
        onSurface = Color(0xFFFDE8F5),
        onSurfaceVariant = Color(0xFFA08098),
        gradientStart = Color(0xFFE91E90),
        gradientEnd = Color(0xFF9B59B6),
        accent = Color(0xFFF39C12),
        cardGradientStart = Color(0xFF3D2545),
        cardGradientEnd = Color(0xFF2A1A2E)
    ),
    ThemeVariant.GOLDEN_AGE to ThemePalette(
        name = ThemeVariant.GOLDEN_AGE,
        primary = Color(0xFFFFD700),        // 金色
        secondary = Color(0xFFDAA520),      // 暗金
        tertiary = Color(0xFFFFA500),       // 橙色
        background = Color(0xFF1A1408),     // 金黑
        surface = Color(0xFF2A1F10),
        surfaceVariant = Color(0xFF3D2D18),
        onPrimary = Color(0xFF1A1408),
        onBackground = Color(0xFFFFF8E1),
        onSurface = Color(0xFFFFF8E1),
        onSurfaceVariant = Color(0xFFA09070),
        gradientStart = Color(0xFFFFD700),
        gradientEnd = Color(0xFFDAA520),
        accent = Color(0xFFFF6B35),
        cardGradientStart = Color(0xFF3D2D18),
        cardGradientEnd = Color(0xFF2A1F10)
    )
)

// ---- 获取配色对应的 Material3 ColorScheme ----
fun getColorScheme(palette: ThemePalette, isDark: Boolean) = if (isDark) {
    darkColorScheme(
        primary = palette.primary,
        secondary = palette.secondary,
        tertiary = palette.tertiary,
        background = palette.background,
        surface = palette.surface,
        surfaceVariant = palette.surfaceVariant,
        onPrimary = palette.onPrimary,
        onBackground = palette.onBackground,
        onSurface = palette.onSurface,
        onSurfaceVariant = palette.onSurfaceVariant
    )
} else {
    // 亮色版本使用反向颜色
    lightColorScheme(
        primary = palette.secondary,
        secondary = palette.primary,
        tertiary = palette.tertiary,
        background = Color(0xFFFAFAFA),
        surface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFFF1F5F9),
        onPrimary = Color(0xFFFFFFFF),
        onBackground = palette.onBackground,
        onSurface = palette.background,
        onSurfaceVariant = Color(0xFF64748B)
    )
}

// ---- CompositionLocal 提供当前配色 ----
val LocalThemePalette = staticCompositionLocalOf { ThemePalettes[ThemeVariant.DEEP_INK]!! }

// ---- 主题配置数据类（可持久化） ----
data class ThemeConfig(
    val variant: ThemeVariant = ThemeVariant.DEEP_INK,
    val isDark: Boolean = true,
    val backgroundImagePath: String? = null,
    val blurRadius: Float = 0f,
    val dimAlpha: Float = 0.4f,
    val useGradientBackground: Boolean = true
)