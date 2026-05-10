package com.aifortune.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiConfig(
    val id: String = "",
    val name: String = "",
    val url: String = "",
    val model: String = "",
    val apiKey: String = "",
    val maxTokens: Int = 2048,
    val temperature: Float = 0.7f,
    val systemPrompt: String = "",
    val isDefault: Boolean = false
)

@Serializable
data class FortuneInput(
    val name: String = "",
    val birthYear: Int = 2000,
    val birthMonth: Int = 1,
    val birthDay: Int = 1,
    val birthHour: Int = 0,
    val gender: String = "男",
    val type: FortuneType = FortuneType.BAZI
)

enum class FortuneType {
    BAZI,        // 八字命理
    XUEYE,       // 求学建议
    SHANGYE,     // 求商建议
    XINGZUO,     // 星座分析
    NAME,        // 姓名分析
    TAROT,       // 塔罗牌
    NAMEGEN      // 起名字
}

data class FortuneResult(
    val title: String,
    val content: String,
    val type: FortuneType,
    val timestamp: Long = System.currentTimeMillis()
)

data class TarotCard(
    val id: Int,
    val name: String,
    val nameEn: String,
    val meaning: String,
    val imageRes: Int = 0
)

data class AstrologySign(
    val name: String,
    val dateRange: String,
    val element: String,  // 火、土、风、水
    val quality: String    // 基本、固定、变动
)

@Serializable
data class HistoryItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val type: FortuneType,
    val title: String,
    val content: String,
    val input: String = "",  // 用户输入的摘要
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toShareText(): String = """
【${type.nameCn()}】$title

$content

——来自 AI Fortune App
    """.trimIndent()
}

fun FortuneType.nameCn(): String = when (this) {
    FortuneType.BAZI -> "八字命理"
    FortuneType.XUEYE -> "求学建议"
    FortuneType.SHANGYE -> "求商建议"
    FortuneType.XINGZUO -> "星座分析"
    FortuneType.NAME -> "姓名分析"
    FortuneType.TAROT -> "塔罗牌"
    FortuneType.NAMEGEN -> "起名字"
}
