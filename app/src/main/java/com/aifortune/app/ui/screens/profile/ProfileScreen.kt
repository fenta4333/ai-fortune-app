package com.aifortune.app.ui.screens.profile

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aifortune.app.ui.theme.*
import com.aifortune.app.ui.util.AppIcons
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHistory: () -> Unit = {},
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val themeConfig by themeViewModel.themeConfig.collectAsState()
    val palette = ThemePalettes[themeConfig.variant] ?: ThemePalettes[ThemeVariant.DEEP_INK]!!

    // ---- 自定义背景图选择器 ----
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // 将 URI 转为文件路径（复制到缓存目录避免权限丢失）
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val cacheFile = java.io.File(context.cacheDir, "custom_bg_${System.currentTimeMillis()}.jpg")
                inputStream?.use { input ->
                    cacheFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                themeViewModel.setBackgroundImagePath(cacheFile.absolutePath)
            } catch (_: Exception) { }
        }
    }

    // ---- 动画状态 ----
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // ---- 用户卡片（带渐入动画） ----
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn() + slideInVertically { it / 2 }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = palette.surfaceVariant.copy(alpha = 0.7f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 头像（动态配色渐变）
                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(palette.gradientStart, palette.gradientEnd)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "命理探索者",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = palette.onBackground
                            )
                            Text(
                                text = "探索命运，洞悉未来",
                                style = MaterialTheme.typography.bodyMedium,
                                color = palette.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ============================================
            // 配色主题选择器（横向滑动）
            // ============================================
            SectionTitle("配色主题", "Color Theme")
            Spacer(modifier = Modifier.height(8.dp))

            val pagerState = rememberPagerState(
                initialPage = ThemeVariant.entries.indexOf(themeConfig.variant),
                pageCount = { ThemeVariant.entries.size }
            )

            // 主题预览卡片
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                pageSpacing = 12.dp
            ) { page ->
                val variant = ThemeVariant.entries[page]
                val previewPalette = ThemePalettes[variant]!!
                val isSelected = variant == themeConfig.variant
                val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                Card(
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = lerp(0.7f, 1f, 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))
                            scaleX = lerp(0.9f, 1f, 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))
                            scaleY = lerp(0.9f, 1f, 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))
                        }
                        .clickable { themeViewModel.updateVariant(variant) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = previewPalette.surfaceVariant
                    ),
                    border = if (isSelected) {
                        androidx.compose.foundation.BorderStroke(2.dp, previewPalette.primary)
                    } else null
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // 配色预览色块
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                previewPalette.primary,
                                previewPalette.secondary,
                                previewPalette.tertiary,
                                previewPalette.gradientStart,
                                previewPalette.gradientEnd
                            ).forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = variant.label,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = previewPalette.onBackground
                        )

                        if (isSelected) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = previewPalette.primary
                            ) {
                                Text(
                                    text = "使用中",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = previewPalette.onPrimary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 小圆点指示器
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ThemeVariant.entries.forEachIndexed { index, _ ->
                    val isCurrentPage = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (isCurrentPage) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCurrentPage) palette.primary
                                else palette.onSurfaceVariant.copy(alpha = 0.4f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ============================================
            // 背景图设置
            // ============================================
            SectionTitle("背景图片", "Background Image")
            Spacer(modifier = Modifier.height(8.dp))

            // 背景图片预览
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = palette.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // 当前背景图预览
                    if (themeConfig.backgroundImagePath != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(themeConfig.backgroundImagePath)
                                .crossfade(true)
                                .build(),
                            contentDescription = "背景预览",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .then(
                                    if (themeConfig.blurRadius > 0f)
                                        Modifier.blur(radiusX = themeConfig.blurRadius.dp, radiusY = themeConfig.blurRadius.dp)
                                    else Modifier
                                )
                        )
                    }

                    // 背景操作按钮
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (themeConfig.backgroundImagePath == null) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = palette.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "点击选择自定义背景",
                                style = MaterialTheme.typography.bodyMedium,
                                color = palette.onSurfaceVariant
                            )
                        }
                    }

                    // 按钮组
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = palette.primary
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("选图", style = MaterialTheme.typography.labelLarge)
                        }

                        if (themeConfig.backgroundImagePath != null) {
                            OutlinedButton(
                                onClick = { themeViewModel.clearBackground() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = palette.secondary
                                )
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("移除", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }

            // 背景强度调节
            if (themeConfig.backgroundImagePath != null) {
                Spacer(modifier = Modifier.height(12.dp))

                // 模糊度滑块
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "模糊",
                        style = MaterialTheme.typography.bodySmall,
                        color = palette.onSurfaceVariant,
                        modifier = Modifier.width(32.dp)
                    )
                    Slider(
                        value = themeConfig.blurRadius,
                        onValueChange = { themeViewModel.setBlurRadius(it) },
                        valueRange = 0f..24f,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = palette.primary,
                            activeTrackColor = palette.primary
                        )
                    )
                    Text(
                        text = "${themeConfig.blurRadius.toInt()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = palette.onSurfaceVariant
                    )
                }

                // 暗度滑块
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "暗度",
                        style = MaterialTheme.typography.bodySmall,
                        color = palette.onSurfaceVariant,
                        modifier = Modifier.width(32.dp)
                    )
                    Slider(
                        value = themeConfig.dimAlpha,
                        onValueChange = { themeViewModel.setDimAlpha(it) },
                        valueRange = 0.1f..0.8f,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = palette.primary,
                            activeTrackColor = palette.primary
                        )
                    )
                    Text(
                        text = "${(themeConfig.dimAlpha * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = palette.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================
            // 设置菜单项
            // ============================================
            SectionTitle("设置", "Settings")
            Spacer(modifier = Modifier.height(8.dp))

            // 深色模式
            MenuItem(
                icon = AppIcons.DarkMode,
                title = "深色模式",
                palette = palette,
                trailing = {
                    Switch(
                        checked = themeConfig.isDark,
                        onCheckedChange = { themeViewModel.toggleDarkMode() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = palette.primary,
                            checkedTrackColor = palette.primary.copy(alpha = 0.3f)
                        )
                    )
                }
            )

            // 渐变背景开关
            MenuItem(
                icon = AppIcons.AutoAwesome,
                title = "渐变背景",
                palette = palette,
                trailing = {
                    Switch(
                        checked = themeConfig.useGradientBackground,
                        onCheckedChange = { themeViewModel.setUseGradientBackground(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = palette.primary,
                            checkedTrackColor = palette.primary.copy(alpha = 0.3f)
                        )
                    )
                }
            )

            // 历史记录
            MenuItem(
                icon = AppIcons.History,
                title = "历史记录",
                palette = palette,
                onClick = onNavigateToHistory
            )

            // 关于
            MenuItem(
                icon = Icons.Default.Info,
                title = "关于",
                palette = palette,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ---- 版本号 ----
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AI Fortune v1.1",
                    style = MaterialTheme.typography.bodySmall,
                    color = palette.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ---- 分区标题 ----
@Composable
private fun SectionTitle(
    title: String,
    subtitle: String? = null
) {
    val palette = LocalThemePalette.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = palette.onBackground
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = palette.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

// ---- 菜单项 ----
@Composable
private fun MenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    palette: com.aifortune.app.ui.theme.ThemePalette,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = palette.surfaceVariant.copy(alpha = 0.6f)
        ),
        onClick = onClick ?: {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = palette.gradientStart,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = palette.onBackground,
                modifier = Modifier.weight(1f)
            )
            trailing?.invoke()
            if (trailing == null && onClick != null) {
                Icon(
                    AppIcons.ChevronRight,
                    contentDescription = null,
                    tint = palette.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// 自定义 blur 扩展（API 31+）
private fun Modifier.blur(radiusX: androidx.compose.ui.unit.Dp, radiusY: androidx.compose.ui.unit.Dp): Modifier {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && radiusX > 0.dp) {
        @Suppress("NewApi")
        this.blur(radiusX = radiusX, radiusY = radiusY)
    } else {
        this
    }
}