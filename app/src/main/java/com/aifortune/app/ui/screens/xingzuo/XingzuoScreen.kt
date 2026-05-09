package com.aifortune.app.ui.screens.xingzuo

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aifortune.app.ui.components.AnimatedGradientButton
import com.aifortune.app.ui.components.LoadingAnimation
import com.aifortune.app.ui.theme.*

data class ZodiacSign(val name: String, val dates: String, val emoji: String)

val zodiacSigns = listOf(
    ZodiacSign("白羊座", "3.21-4.19", "♈"),
    ZodiacSign("金牛座", "4.20-5.20", "♉"),
    ZodiacSign("双子座", "5.21-6.21", "♊"),
    ZodiacSign("巨蟹座", "6.22-7.22", "♋"),
    ZodiacSign("狮子座", "7.23-8.22", "♌"),
    ZodiacSign("处女座", "8.23-9.22", "♍"),
    ZodiacSign("天秤座", "9.23-10.23", "♎"),
    ZodiacSign("天蝎座", "10.24-11.22", "♏"),
    ZodiacSign("射手座", "11.23-12.21", "♐"),
    ZodiacSign("摩羯座", "12.22-1.19", "♑"),
    ZodiacSign("水瓶座", "1.20-2.18", "♒"),
    ZodiacSign("双鱼座", "2.19-3.20", "♓")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XingzuoScreen(
    viewModel: XingzuoViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedSign by remember { mutableStateOf<ZodiacSign?>(null) }
    var questionType by remember { mutableStateOf("综合") }
    var showTypeMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("星座分析", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "返回") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp).verticalScroll(rememberScrollState())) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = PrimaryIndigo.copy(alpha = 0.1f))) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(brush = Brush.linearGradient(colors = listOf(PrimaryIndigo, PrimaryViolet))), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Star, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column { Text("星座分析", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold); Text("探索星座奥秘", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("选择星座", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(columns = GridCells.Fixed(4), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.height(240.dp)) {
                items(zodiacSigns) { sign ->
                    Card(
                        modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(12.dp)).clickable { selectedSign = sign },
                        colors = CardDefaults.cardColors(containerColor = if (selectedSign == sign) PrimaryIndigo else MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(sign.emoji, style = MaterialTheme.typography.headlineSmall)
                                Text(sign.name, style = MaterialTheme.typography.labelSmall, color = if (selectedSign == sign) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ExposedDropdownMenuBox(expanded = showTypeMenu, onExpandedChange = { showTypeMenu = it }) {
                OutlinedTextField(value = questionType, onValueChange = {}, readOnly = true, label = { Text("问题类型") }, modifier = Modifier.fillMaxWidth().menuAnchor(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showTypeMenu) })
                ExposedDropdownMenu(expanded = showTypeMenu, onDismissRequest = { showTypeMenu = false }) {
                    listOf("综合", "事业", "爱情", "财运", "健康").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { questionType = it; showTypeMenu = false }) }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedGradientButton(text = if (uiState.isLoading) "分析中..." else "开始分析", onClick = { selectedSign?.let { viewModel.queryXingzuo(it.name, questionType) } }, modifier = Modifier.fillMaxWidth(), icon = Icons.Default.AutoAwesome, enabled = selectedSign != null && !uiState.isLoading)

            if (uiState.isLoading) { Spacer(modifier = Modifier.height(24.dp)); Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { LoadingAnimation() } }

            uiState.error?.let { error -> Spacer(modifier = Modifier.height(16.dp)); Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), shape = RoundedCornerShape(12.dp)) { Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error); Spacer(modifier = Modifier.width(8.dp)); Text(error, color = MaterialTheme.colorScheme.error) } } }

            uiState.result?.let { result -> Spacer(modifier = Modifier.height(24.dp)); Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) { Column(modifier = Modifier.padding(20.dp)) { Text(result.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(12.dp)); Text(result.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
