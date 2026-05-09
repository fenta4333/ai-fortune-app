package com.aifortune.app.ui.screens.namegen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aifortune.app.ui.components.AnimatedGradientButton
import com.aifortune.app.ui.components.LoadingAnimation
import com.aifortune.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameGenScreen(
    viewModel: NameGenViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var surname by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("待定") }
    var showGenderMenu by remember { mutableStateOf(false) }
    var birthYear by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("起名字", fontWeight = FontWeight.SemiBold) }, navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "返回") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background))
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp).verticalScroll(rememberScrollState())) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = PrimaryIndigo.copy(alpha = 0.1f))) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(brush = Brush.linearGradient(colors = listOf(PrimaryIndigo, PrimaryViolet))), contentAlignment = Alignment.Center) { Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(24.dp)) }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column { Text("AI起名", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold); Text("智能生成好名字", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(value = surname, onValueChange = { surname = it }, label = { Text("姓氏") }, modifier = Modifier.fillMaxWidth(), singleLine = true, leadingIcon = { Icon(Icons.Default.Person, null) })

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(expanded = showGenderMenu, onExpandedChange = { showGenderMenu = it }) {
                OutlinedTextField(value = gender, onValueChange = {}, readOnly = true, label = { Text("性别") }, modifier = Modifier.fillMaxWidth().menuAnchor(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showGenderMenu) })
                ExposedDropdownMenu(expanded = showGenderMenu, onDismissRequest = { showGenderMenu = false }) {
                    listOf("待定", "男孩", "女孩").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { gender = it; showGenderMenu = false }) }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = birthYear, onValueChange = { birthYear = it }, label = { Text("出生年份（可选）") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), placeholder = { Text("如：2024") })

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedGradientButton(text = if (uiState.isLoading) "生成中..." else "生成名字", onClick = { viewModel.queryNameGen(surname, gender, birthYear) }, modifier = Modifier.fillMaxWidth(), icon = Icons.Default.AutoAwesome, enabled = surname.isNotBlank() && !uiState.isLoading)

            if (uiState.isLoading) { Spacer(modifier = Modifier.height(24.dp)); Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { LoadingAnimation() } }

            uiState.error?.let { error -> Spacer(modifier = Modifier.height(16.dp)); Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), shape = RoundedCornerShape(12.dp)) { Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error); Spacer(modifier = Modifier.width(8.dp)); Text(error, color = MaterialTheme.colorScheme.error) } } }

            uiState.result?.let { result -> Spacer(modifier = Modifier.height(24.dp)); Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) { Column(modifier = Modifier.padding(20.dp)) { Text(result.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(12.dp)); Text(result.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
