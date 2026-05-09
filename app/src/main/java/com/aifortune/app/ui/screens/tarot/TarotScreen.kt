package com.aifortune.app.ui.screens.tarot

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aifortune.app.ui.components.AnimatedGradientButton
import com.aifortune.app.ui.components.LoadingAnimation
import com.aifortune.app.ui.theme.*

data class TarotCardData(val id: Int, val name: String, val nameEn: String, val meaning: String)

val tarotCards = listOf(
    TarotCardData(0, "愚者", "The Fool", "新的开始、自由、冒险"),
    TarotCardData(1, "魔术师", "The Magician", "创造力、意志力、技能"),
    TarotCardData(2, "女祭司", "The High Priestess", "直觉、智慧、神秘"),
    TarotCardData(3, "皇后", "The Empress", "丰盛、母性、自然"),
    TarotCardData(4, "皇帝", "The Emperor", "权威、稳定、领导力"),
    TarotCardData(5, "教皇", "The Hierophant", "传统、教导、信仰"),
    TarotCardData(6, "恋人", "The Lovers", "爱情、和谐、选择"),
    TarotCardData(7, "战车", "The Chariot", "胜利、意志、决心"),
    TarotCardData(8, "力量", "Strength", "勇气、耐心、内在力量"),
    TarotCardData(9, "隐士", "The Hermit", "反思、内在寻求智慧"),
    TarotCardData(10, "命运之轮", "Wheel of Fortune", "命运、变化、周期"),
    TarotCardData(11, "正义", "Justice", "平衡、真相、公正"),
    TarotCardData(12, "倒吊人", "The Hanged Man", "牺牲、换位思考、新视角"),
    TarotCardData(13, "死亡", "Death", "转变、终结、新生"),
    TarotCardData(14, "节制", "Temperance", "平衡、耐心、moderation"),
    TarotCardData(15, "恶魔", "The Devil", "束缚、欲望、阴影"),
    TarotCardData(16, "塔", "The Tower", "剧变、启示、解放"),
    TarotCardData(17, "星星", "The Star", "希望、灵感、平静"),
    TarotCardData(18, "月亮", "The Moon", "幻觉、直觉、情绪"),
    TarotCardData(19, "太阳", "The Sun", "成功、活力、快乐"),
    TarotCardData(20, "审判", "Judgement", "复活、觉醒、原谅"),
    TarotCardData(21, "世界", "The World", "完成、成就、整合")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarotScreen(
    viewModel: TarotViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedCards by remember { mutableStateOf<List<TarotCardData>>(emptyList()) }
    var isDrawing by remember { mutableStateOf(false) }
    var showCards by remember { mutableStateOf(false) }

    val shuffleAnimation by rememberInfiniteTransition(label = "shuffle").animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Restart), label = "rotation"
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("塔罗牌", fontWeight = FontWeight.SemiBold) }, navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "返回") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background))
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = PrimaryIndigo.copy(alpha = 0.1f))) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(brush = Brush.linearGradient(colors = listOf(PrimaryIndigo, PrimaryViolet))), contentAlignment = Alignment.Center) { Icon(Icons.Default.Psychology, null, tint = Color.White, modifier = Modifier.size(24.dp)) }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column { Text("塔罗牌解读", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold); Text("神秘塔罗牌阵解读", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (!showCards) {
                // Card back display
                Box(modifier = Modifier.size(120.dp).scale(if (isDrawing) 1.1f else 1f).rotate(if (isDrawing) shuffleAnimation else 0f).clip(RoundedCornerShape(16.dp)).background(brush = Brush.linearGradient(colors = listOf(PrimaryIndigo.copy(alpha = 0.8f), PrimaryViolet.copy(alpha = 0.8f)))).clickable(enabled = !isDrawing) {
                    isDrawing = true
                }, contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("点击抽牌", color = Color.White, style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("点击上方卡牌抽取3张牌", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                // Show drawn cards
                Text("你抽取的牌", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(columns = GridCells.Fixed(3), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.height(200.dp)) {
                    items(selectedCards) { card ->
                        Card(modifier = Modifier.aspectRatio(0.7f).clip(RoundedCornerShape(12.dp)), colors = CardDefaults.cardColors(containerColor = PrimaryIndigo)) {
                            Box(modifier = Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(card.name, color = Color.White, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
                                    Text(card.nameEn, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier = Modifier.fillMaxWidth().clickable { showCards = false; selectedCards = emptyList() }, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), shape = RoundedCornerShape(12.dp)) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Icon(Icons.Default.Refresh, null, tint = PrimaryIndigo, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("重新抽牌", color = PrimaryIndigo)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (showCards && selectedCards.isNotEmpty()) {
                AnimatedGradientButton(text = if (uiState.isLoading) "解读中..." else "开始解读", onClick = { viewModel.queryTarot(selectedCards) }, modifier = Modifier.fillMaxWidth(), icon = Icons.Default.AutoAwesome, enabled = !uiState.isLoading)
            }

            if (uiState.isLoading) { Spacer(modifier = Modifier.height(24.dp)); Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { LoadingAnimation() } }

            uiState.error?.let { error -> Spacer(modifier = Modifier.height(16.dp)); Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), shape = RoundedCornerShape(12.dp)) { Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error); Spacer(modifier = Modifier.width(8.dp)); Text(error, color = MaterialTheme.colorScheme.error) } } }

            uiState.result?.let { result -> Spacer(modifier = Modifier.height(24.dp)); Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) { Column(modifier = Modifier.padding(20.dp)) { Text(result.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(12.dp)); Text(result.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }

            Spacer(modifier = Modifier.height(32.dp))

            // Trigger card draw
            LaunchedEffect(isDrawing) {
                if (isDrawing) {
                    kotlinx.coroutines.delay(1500)
                    selectedCards = tarotCards.shuffled().take(3)
                    isDrawing = false
                    showCards = true
                }
            }
        }
    }
}
