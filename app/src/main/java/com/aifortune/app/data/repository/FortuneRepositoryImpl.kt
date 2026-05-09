package com.aifortune.app.data.repository

import com.aifortune.app.data.local.LocalDataStore
import com.aifortune.app.data.remote.LlmApiClient
import com.aifortune.app.domain.model.*
import com.aifortune.app.domain.repository.FortuneRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FortuneRepositoryImpl @Inject constructor(
    private val localDataStore: LocalDataStore,
    private val llmApiClient: LlmApiClient
) : FortuneRepository {

    override fun getApiConfigs(): Flow<List<ApiConfig>> = localDataStore.apiConfigs

    override fun getDefaultApiId(): Flow<String?> = localDataStore.defaultApiId

    override suspend fun saveApiConfig(config: ApiConfig) {
        localDataStore.saveApiConfig(config)
    }

    override suspend fun deleteApiConfig(id: String) {
        localDataStore.deleteApiConfig(id)
    }

    override suspend fun getApiConfigById(id: String): ApiConfig? {
        return localDataStore.getApiConfigById(id)
    }

    override suspend fun queryFortune(input: FortuneInput, apiConfig: ApiConfig?): Result<FortuneResult> {
        if (apiConfig == null) {
            return Result.failure(Exception("请先配置API"))
        }

        val prompt = buildPrompt(input)
        
        return llmApiClient.chat(apiConfig, prompt).map { response ->
            FortuneResult(
                title = getTitle(input.type),
                content = response,
                type = input.type
            )
        }
    }

    private fun buildPrompt(input: FortuneInput): String {
        return when (input.type) {
            FortuneType.BAZI -> buildBaziPrompt(input)
            FortuneType.XUEYE -> buildXueyePrompt(input)
            FortuneType.SHANGYE -> buildShangyePrompt(input)
            FortuneType.XINGZUO -> buildXingzuoPrompt(input)
            FortuneType.NAME -> buildNamePrompt(input)
            FortuneType.TAROT -> buildTarotPrompt(input)
            FortuneType.NAMEGEN -> buildNameGenPrompt(input)
        }
    }

    private fun buildBaziPrompt(input: FortuneInput): String {
        return """
请为${input.name}分析八字命理。
出生信息：${input.birthYear}年${input.birthMonth}月${input.birthDay}日${input.birthHour}时
性别：${input.gender}

请分析：
1. 八字五行分布
2. 日主强弱
3. 喜用神
4. 性格特点
5. 事业建议
6. 财运分析
7. 健康提醒

用通俗易懂的语言回答""".trimIndent()
    }

    private fun buildXueyePrompt(input: FortuneInput): String {
        return """
请为${input.name}提供求学建议。
出生信息：${input.birthYear}年${input.birthMonth}月${input.birthDay}日
性别：${input.gender}

请分析：
1. 适合的学习方式
2. 优势学科
3. 备考策略
4. 志愿填报建议
5. 未来职业方向

用专业但易懂的语言回答""".trimIndent()
    }

    private fun buildShangyePrompt(input: FortuneInput): String {
        return """
请为${input.name}提供商业决策建议。
出生信息：${input.birthYear}年${input.birthMonth}月${input.birthDay}日
性别：${input.gender}

请分析：
1. 商业天赋分析
2. 适合的商业模式
3. 合作伙伴选择
4. 风险提示
5. 发展时机建议

用专业但易懂的语言回答""".trimIndent()
    }

    private fun buildXingzuoPrompt(input: FortuneInput): String {
        return """
请为${input.name}进行星座分析。
出生月日：${input.birthMonth}月${input.birthDay}日（请推断太阳星座）

请分析：
1. 星座基本性格
2. 事业运势
3. 爱情运势
4. 财运运势
5. 健康提醒
6. 本周/本月建议

用生动有趣的语言回答""".trimIndent()
    }

    private fun buildNamePrompt(input: FortuneInput): String {
        return """
请分析姓名"${input.name}"的含义。
出生信息：${input.birthYear}年${input.birthMonth}月${input.birthDay}日

请分析：
1. 姓名笔画数与五行
2. 姓名含义解析
3. 音韵分析
4. 对运势的影响
5. 建议与注意事项

用专业但易懂的语言回答""".trimIndent()
    }

    private fun buildTarotPrompt(input: FortuneInput): String {
        return """
请为${input.name}进行塔罗牌解读。

请先说明：这是用户自行抽取的牌，请以专业塔罗师的角度解读。

请提供：
1. 牌面解读
2. 对应问题的解答
3. 建议与提醒

用神秘而优雅的语言回答""".trimIndent()
    }

    private fun buildNameGenPrompt(input: FortuneInput): String {
        return """
请为${input.name}生成宝宝名字建议。
姓氏：${input.name}
性别：${input.gender}
出生信息：${input.birthYear}年${input.birthMonth}月${input.birthDay}日

请提供：
1. 5-10个男孩名字建议
2. 5-10个女孩名字建议
3. 每个名字的寓意解释
4. 五行分析

用有文化底蕴的语言回答""".trimIndent()
    }

    private fun getTitle(type: FortuneType): String {
        return when (type) {
            FortuneType.BAZI -> "八字命理分析"
            FortuneType.XUEYE -> "求学建议"
            FortuneType.SHANGYE -> "求商建议"
            FortuneType.XINGZUO -> "星座分析"
            FortuneType.NAME -> "姓名分析"
            FortuneType.TAROT -> "塔罗牌解读"
            FortuneType.NAMEGEN -> "起名建议"
        }
    }
}
