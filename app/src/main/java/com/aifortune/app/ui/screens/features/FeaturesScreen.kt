package com.aifortune.app.ui.screens.features

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aifortune.app.ui.components.FeatureCard
import com.aifortune.app.ui.theme.PrimaryIndigo
import com.aifortune.app.ui.theme.PrimaryViolet

data class FeatureItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: String
)

val features = listOf(
    FeatureItem("八字命理", "深度分析命理格局、运势预测", Icons.Default.AccountBalance, "bazi"),
    FeatureItem("求学建议", "学业规划、备考策略指导", Icons.Default.School, "xueye"),
    FeatureItem("求商建议", "商业决策、商业机会分析", Icons.Default.TrendingUp, "shangye"),
    FeatureItem("星座分析", "星座性格、运势解读", Icons.Default.Star, "xingzuo"),
    FeatureItem("姓名分析", "姓名含义、五行运势", Icons.Default.Badge, "name"),
    FeatureItem("塔罗牌", "神秘塔罗牌阵解读", Icons.Default.Psychology, "tarot"),
    FeatureItem("起名字", "AI智能生成好名字", Icons.Default.Edit, "namegen")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToBazi: () -> Unit,
    onNavigateToXueye: () -> Unit,
    onNavigateToShangye: () -> Unit,
    onNavigateToXingzuo: () -> Unit,
    onNavigateToName: () -> Unit,
    onNavigateToTarot: () -> Unit,
    onNavigateToNameGen: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
    }

    val navigateMap = mapOf(
        "bazi" to onNavigateToBazi,
        "xueye" to onNavigateToXueye,
        "shangye" to onNavigateToShangye,
        "xingzuo" to onNavigateToXingzuo,
        "name" to onNavigateToName,
        "tarot" to onNavigateToTarot,
        "namegen" to onNavigateToNameGen
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "功能服务",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            itemsIndexed(features) { index, feature ->
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(300, delayMillis = index * 50)) + 
                            slideInHorizontally { -30 }
                ) {
                    FeatureCard(
                        title = feature.title,
                        description = feature.description,
                        icon = feature.icon,
                        onClick = { navigateMap[feature.route]?.invoke() }
                    )
                }
            }
        }
    }
}
