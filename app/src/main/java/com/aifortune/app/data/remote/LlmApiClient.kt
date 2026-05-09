package com.aifortune.app.data.remote

import com.aifortune.app.domain.model.ApiConfig
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

// Request/Response models
data class ChatMessage(
    val role: String = "user",
    val content: String
)

data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val max_tokens: Int = 2048,
    val temperature: Float = 0.7f
)

data class ChatResponse(
    val id: String?,
    val choices: List<Choice>?,
    val usage: Usage?,
    val error: ErrorResponse?
)

data class Choice(
    val index: Int?,
    val message: ChatMessage?,
    val finish_reason: String?
)

data class Usage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int?,
    @SerializedName("completion_tokens")
    val completionTokens: Int?,
    @SerializedName("total_tokens")
    val totalTokens: Int?
)

data class ErrorResponse(
    val message: String?,
    val type: String?,
    val code: String?
)

interface LlmApiService {
    @POST
    suspend fun chat(@Url url: String, @Body request: ChatRequest): ChatResponse
}

@Singleton
class LlmApiClient @Inject constructor() {
    
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private fun createService(baseUrl: String): LlmApiService {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl(baseUrl))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LlmApiService::class.java)
    }

    private fun getBaseUrl(url: String): String {
        return if (url.startsWith("http")) {
            val uri = java.net.URI(url)
            "${uri.scheme}://${uri.host}/"
        } else {
            "https://$url/"
        }
    }

    suspend fun chat(config: ApiConfig, userMessage: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val systemPrompt = config.systemPrompt.ifEmpty { getDefaultSystemPrompt() }
            
            val request = ChatRequest(
                model = config.model,
                messages = listOf(
                    ChatMessage(role = "system", content = systemPrompt),
                    ChatMessage(role = "user", content = userMessage)
                ),
                max_tokens = config.maxTokens,
                temperature = config.temperature
            )

            val service = createService(config.url)
            val response = service.chat(config.url, request)

            if (response.error != null) {
                Result.failure(Exception(response.error.message ?: "API Error"))
            } else {
                val content = response.choices?.firstOrNull()?.message?.content ?: "无返回内容"
                Result.success(content)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getDefaultSystemPrompt(): String {
        return """你是一位专业的命理大师，精通八字命理、星座分析、姓名学、塔罗牌等传统玄学。
请用温暖、专业、易懂的语言为用户解答。
回答要简洁有深度，不要过于冗长。
如果不确定某事，请坦诚说明。"""
    }
}
