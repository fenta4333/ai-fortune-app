package com.aifortune.app.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aifortune.app.ui.components.*
import com.aifortune.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToFeatures: () -> Unit,
    onNavigateToApi: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepInk,
                        DeepInk.copy(alpha = 0.95f),
                        Color(0xFF0D0D1A)
                    )
                )
            )
    ) {
        // 浮动粒子背景
        FloatingParticles(
            modifier = Modifier.fillMaxSize(),
            particleCount = 12
        )
        
        // 中央呼吸光晕
        BreathingGlow(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 80.dp, y = (-100).dp),
            color = MoltenGold,
            size = 250.dp
        )
        
        BreathingGlow(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-100).dp, y = 150.dp),
            color = Crimson.copy(alpha = 0.5f),
            size = 180.dp
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
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
                    .padding(horizontal = 20.dp)
            ) {
                // Header - 丝滑入场
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        animationSpec = tween(800, easing = FastOutSlowInEasing)
                    ) + slideInVertically(
                        animationSpec = spring(
                            stiffness = 100f,
                            dampingRatio = 0.8f
                        ),
                        initialOffsetY = { -50 }
                    )
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                // 标题带金色渐变
                                Text(
                                    text = "AI Fortune",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MoltenGold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "探索你的命运密码",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = SmokeGray
                                )
                            }
                            
                            // 液态玻璃头像
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                MoltenGold.copy(alpha = 0.3f),
                                                LiquidGlassBase.copy(alpha = 0.5f)
                                            )
                                        )
                                    )
                                    .border(
                                        width = 1.dp,
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                MoltenGold,
                                                Crimson
                                            )
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MoltenGold,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Quick Actions - 弹簧入场
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        animationSpec = tween(600, delayMillis = 200, easing = FastOutSlowInEasing)
                    ) + slideInVertically(
                        animationSpec = spring(
                            stiffness = 120f,
                            dampingRatio = 0.75f
                        ),
                        initialOffsetY = { -30 }
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GlassQuickCard(
                            icon = Icons.Default.Settings,
                            label = "API设置",
                            onClick = onNavigateToApi,
                            modifier = Modifier.weight(1f)
                        )
                        GlassQuickCard(
                            icon = Icons.Default.Person,
                            label = "我的",
                            onClick = onNavigateToProfile,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Section Title
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        animationSpec = tween(600, delayMillis = 350, easing = FastOutSlowInEasing)
                    )
                ) {
                    Text(
                        text = "功能服务",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Ivory.copy(alpha = 0.9f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Feature Cards - 依次丝滑入场
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        animationSpec = tween(600, delayMillis = 400, easing = FastOutSlowInEasing)
                    ) + slideInVertically(
                        animationSpec = spring(
                            stiffness = 100f,
                            dampingRatio = 0.85f
                        ),
                        initialOffsetY = { 40 }
                    )
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        GlassFeatureCard(
                            title = "八字命理",
                            description = "深度分析命理格局",
                            icon = Icons.Default.AccountBalance,
                            onClick = onNavigateToFeatures
                        )
                        GlassFeatureCard(
                            title = "星座分析",
                            description = "探索星座奥秘",
                            icon = Icons.Default.Star,
                            onClick = onNavigateToFeatures
                        )
                        GlassFeatureCard(
                            title = "塔罗牌",
                            description = "神秘塔罗解读",
                            icon = Icons.Default.Psychology,
                            onClick = onNavigateToFeatures
                        )
                        GlassFeatureCard(
                            title = "更多功能",
                            description = "求学、求商、姓名分析...",
                            icon = Icons.Default.Add,
                            onClick = onNavigateToFeatures
                        )
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Bottom hint
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        animationSpec = tween(800, delayMillis = 600, easing = FastOutSlowInEasing)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "点击任意功能开始探索",
                            style = MaterialTheme.typography.bodySmall,
                            color = SmokeGray.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}
