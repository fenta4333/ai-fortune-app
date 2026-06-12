package com.aifortune.app.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.aifortune.app.ui.theme.LocalThemePalette
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 背景容器组件
 * 支持：自定义图片背景 + 渐变叠加层 + 模糊效果
 * 内存优化：通过 remember 缓存 Bitmap 引用，Coil 自动管理内存
 */
@Composable
fun BackgroundContainer(
    backgroundImagePath: String?,
    blurRadius: Float = 0f,
    dimAlpha: Float = 0.4f,
    useGradient: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val palette = LocalThemePalette.current

    // 异步加载本地图片
    var bgBitmap by remember(backgroundImagePath) {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }

    LaunchedEffect(backgroundImagePath) {
        if (!backgroundImagePath.isNullOrBlank()) {
            withContext(Dispatchers.IO) {
                try {
                    bgBitmap = BitmapFactory.decodeFile(backgroundImagePath)
                } catch (_: Exception) {
                    bgBitmap = null
                }
            }
        } else {
            bgBitmap = null
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 层级1: 背景图片（带模糊）
        if (bgBitmap != null) {
            Image(
                bitmap = bgBitmap!!.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (blurRadius > 0f) Modifier.blur(radiusX = blurRadius.toBlurDp()) else Modifier
                    )
            )
        }

        // 层级2: 渐变蒙层
        val bgModifier = when {
            useGradient && bgBitmap != null -> {
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        colors = listOf(
                            palette.background.copy(alpha = dimAlpha + 0.3f),
                            palette.background.copy(alpha = dimAlpha)
                        )
                    )
                )
            }
            useGradient -> {
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        colors = listOf(
                            palette.background,
                            palette.background.copy(alpha = 0.95f),
                            palette.surface
                        )
                    )
                )
            }
            else -> {
                Modifier.fillMaxSize().background(palette.background)
            }
        }
        Box(modifier = bgModifier)

        // 层级3: 前景内容
        content()
    }
}

private fun Float.toBlurDp(): androidx.compose.ui.unit.Dp {
    return (this.coerceIn(0f, 25f)).dp
}