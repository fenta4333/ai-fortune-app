package com.aifortune.app.ui.screens.shangye

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShangyeScreen(
    viewModel: ShangyeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var name by remember { mutableStateOf("") }
    var businessIdea by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var riskTolerance by remember { mutableStateOf("中") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("求商建议", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "返回") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp).verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryIndigo.copy(alpha = 0.1f))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(brush = Brush.linearGradient(colors = listOf(PrimaryIndigo, PrimaryViolet))), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.TrendingUp, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("求商建议", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text("商业决策、商业机会分析", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("姓名") }, modifier = Modifier.fillMaxWidth(), singleLine = true, leadingIcon = { Icon(Icons.Default.Person, null) })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = businessIdea, onValueChange = { businessIdea = it }, label = { Text("想做的事情") }, modifier = Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("描述你的商业想法") })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = budget, onValueChange = { budget = it }, label = { Text("预算") }, modifier = Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("如：10万-50万") })
            Spacer(modifier = Modifier.height(16.dp))

            var showMenu by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = showMenu, onExpandedChange = { showMenu = it }) {
                OutlinedTextField(value = riskTolerance, onValueChange = {}, readOnly = true, label = { Text("风险偏好") }, modifier = Modifier.fillMaxWidth().menuAnchor(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showMenu) })
                ExposedDropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    listOf("保守", "中", "激进").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { riskTolerance = it; showMenu = false }) }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedGradientButton(text = if (uiState.isLoading) "分析中..." else "获取建议", onClick = { viewModel.queryShangye(name, businessIdea, budget, riskTolerance) }, modifier = Modifier.fillMaxWidth(), icon = Icons.Default.AutoAwesome, enabled = name.isNotBlank() && businessIdea.isNotBlank() && !uiState.isLoading)

            if (uiState.isLoading) { Spacer(modifier = Modifier.height(24.dp)); Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { LoadingAnimation() } }

            uiState.error?.let { error -> Spacer(modifier = Modifier.height(16.dp)); Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), shape = RoundedCornerShape(12.dp)) { Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error); Spacer(modifier = Modifier.width(8.dp)); Text(error, color = MaterialTheme.colorScheme.error) } } }

            uiState.result?.let { result -> Spacer(modifier = Modifier.height(24.dp)); Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) { Column(modifier = Modifier.padding(20.dp)) { Text(result.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(12.dp)); Text(result.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
