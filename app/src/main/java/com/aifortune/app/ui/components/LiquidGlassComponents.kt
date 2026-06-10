package com.aifortune.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aifortune.app.ui.theme.*
import kotlin.math.*

// ============================================================
// 液态玻璃设计系统 - 组件库
// 丝滑 · 磨砂 · 金属光泽
// ============================================================

/**
 * 液态玻璃卡片
 * 核心：毛玻璃 + 金属边框 + 悬浮动效
 */
@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // 悬浮动效 - 丝滑的弹簧物理
    val animatedOffset by animateFloatAsState(
        targetValue = if (isPressed) 2f else 0f,
        animationSpec = spring(
            stiffness = 200f,
            dampingRatio = 0.8f
        ),
        label = "lift"
    )
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            stiffness = 300f,
            dampingRatio = 0.7f
        ),
        label = "scale"
    )

    // 边框渐变动画
    val infiniteTransition = rememberInfiniteTransition(label = "border")
    val borderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderAlpha"
    )

    Box(
        modifier = modifier
            .offset(y = animatedOffset.dp)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .clip(RoundedCornerShape(24.dp))
            .drawBehind {
                // 金属边框
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MoltenGold.copy(alpha = borderAlpha),
                            Crimson.copy(alpha = borderAlpha * 0.6f),
                            MoltenGold.copy(alpha = borderAlpha)
                        )
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx())
                )
            }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LiquidGlassHighlight.copy(alpha = 0.15f),
                        LiquidGlassBase.copy(alpha = 0.25f),
                        LiquidGlassBase.copy(alpha = 0.35f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            content = content
        )
    }
}

/**
 * 液态玻璃按钮
 */
@Composable
fun LiquidGlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(
            stiffness = 400f,
            dampingRatio = 0.6f
        ),
        label = "buttonScale"
    )
    
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (enabled) {
                    Modifier
                        .drawBehind {
                            // 外发光
                            drawRoundRect(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        MoltenGold.copy(alpha = glowAlpha * 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                            )
                        }
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    MoltenGold,
                                    Amber,
                                    MoltenGold
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                } else {
                    Modifier.background(
                        color = SmokeGray,
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DeepInk,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = DeepInk,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * 浮动光点粒子
 */
@Composable
fun FloatingParticles(
    modifier: Modifier = Modifier,
    particleCount: Int = 8
) {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    
    Box(modifier = modifier) {
        repeat(particleCount) { index ->
            val delay = index * 400
            val size = 2.dp + (index % 3) * 2.dp
            
            val yOffset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 30f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 4000 + (index * 500),
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "particleY$index"
            )
            
            val xOffset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 3000 + (index * 300),
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "particleX$index"
            )
            
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 0.6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 2500 + (index * 200),
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "particleAlpha$index"
            )
            
            val particleColor = when (index % 4) {
                0 -> MoltenGold
                1 -> Amber
                2 -> Crimson
                else -> NebulaPurple
            }
            
            Box(
                modifier = Modifier
                    .offset(
                        x = ((index * 47) % 300).dp + xOffset.dp,
                        y = ((index * 31) % 200).dp + yOffset.dp
                    )
                    .size(size)
                    .background(
                        color = particleColor.copy(alpha = alpha),
                        shape = CircleShape
                    )
            )
        }
    }
}

/**
 * 呼吸光晕效果
 */
@Composable
fun BreathingGlow(
    modifier: Modifier = Modifier,
    color: Color = MoltenGold,
    size: Dp = 200.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breath")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )
    
    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = alpha),
                        Color.Transparent
                    )
                ),
                shape = CircleShape
            )
    )
}

/**
 * 丝绸光泽动效（适用于卡片背景）
 */
@Composable
fun SilkShimmer(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "silk")
    
    val translateAnim by infiniteTransition.animateFloat(
        initialValue = -200f,
        targetValue = 400f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 4000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "silkTranslate"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .drawBehind {
                // 丝绸光泽扫过效果
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            GoldShimmer.copy(alpha = 0.1f),
                            Color.Transparent
                        ),
                        start = Offset(translateAnim, 0f),
                        end = Offset(translateAnim + 200f, size.height)
                    )
                )
            }
    )
}

/**
 * FeatureCard 液态玻璃版
 */
@Composable
fun GlassFeatureCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LiquidGlassCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标区域
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.angularGradient(
                            colors = listOf(
                                MoltenGold.copy(alpha = 0.15f),
                                Crimson.copy(alpha = 0.1f),
                                LiquidGlassBase.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .border(
                        width = 0.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MoltenGold.copy(alpha = 0.5f),
                                Crimson.copy(alpha = 0.3f)
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MoltenGold,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Ivory
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = SmokeGray
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MoltenGold.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * 迷你快捷入口卡片
 */
@Composable
fun GlassQuickCard(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            stiffness = 400f,
            dampingRatio = 0.6f
        ),
        label = "quickScale"
    )
    
    Box(
        modifier = modifier
            .aspectRatio(1.5f)
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
            }
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LiquidGlassHighlight.copy(alpha = 0.2f),
                        LiquidGlassBase.copy(alpha = 0.3f)
                    )
                )
            )
            .border(
                width = 0.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MoltenGold.copy(alpha = 0.4f),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MoltenGold,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Ivory.copy(alpha = 0.9f)
            )
        }
    }
}
