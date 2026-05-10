# AI Fortune App 构建指南

## 环境要求

- Android Studio Arctic Fox 或更高版本
- JDK 17
- Android SDK (API 34)
- Gradle 8.5

## 本地构建

```bash
# 1. 克隆项目
git clone https://github.com/fenta4333/ai-fortune-app.git
cd ai-fortune-app

# 2. 打开项目
# 用 Android Studio 打开项目目录

# 3. 构建 Debug APK
./gradlew assembleDebug

# 输出: app/build/outputs/apk/debug/app-debug.apk
```

## GitHub Actions 自动构建

项目已配置 GitHub Actions，每次 push 会自动构建。

### 触发构建

```bash
gh workflow run build.yml
```

### 下载 APK

1. 访问 https://github.com/fenta4333/ai-fortune-app/actions
2. 点击最新的 workflow run
3. 下载 Artifacts 中的 app-debug.apk

---

## 修复记录

### 1. 图标缺失问题
- **问题**: mipmap-* 目录缺少 PNG 图标
- **解决**: 生成各尺寸图标并添加到对应目录

### 2. Gradle Wrapper 问题
- **问题**: gradle-wrapper.jar 缺失
- **解决**: 从官方重新下载并提交到仓库

### 3. 插件下载失败
- **问题**: settings.gradle 缺少 pluginManagement
- **解决**: 添加 pluginManagement 块

```groovy
// settings.gradle
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
```

### 4. 编译错误修复

#### 4.1 dp 引用问题
```kotlin
// 错误
tonalElevation = androidx.compose.ui.unit.dp.times(8)
// 正确
tonalElevation = 8.dp
```

#### 4.2 Float 类型
```kotlin
// 错误
val temperature: Float = 0.7
// 正确
val temperature: Float = 0.7f
```

#### 4.3 缺少 Import
- `Color` - 需要 `import androidx.compose.ui.graphics.Color`
- `verticalScroll` - 需要 `import androidx.compose.foundation.verticalScroll`
- `tween` - 需要 `import androidx.compose.animation.core.tween`

#### 4.4 if 表达式
```kotlin
// 错误
trailing?.invoke() ?: if (onClick != null) { ... }
// 正确
trailing?.invoke()
if (trailing == null && onClick != null) { ... }
```

#### 4.5 Experimental API
```groovy
// app/build.gradle
kotlinOptions {
    jvmTarget = '17'
    freeCompilerArgs += [
        '-opt-in=androidx.compose.material3.ExperimentalMaterial3Api',
        '-opt-in=androidx.compose.foundation.ExperimentalFoundationApi'
    ]
}
```

### 5. API 401 认证问题
- **问题**: API 密钥没有添加到请求头
- **解决**: 添加 Authorization header

```kotlin
private fun createService(baseUrl: String, apiKey: String): LlmApiService {
    val client = okHttpClient.newBuilder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $apiKey")
                .build()
            chain.proceed(request)
        }
        .build()
    // ...
}
```

---

## API 配置示例

### DeepSeek
- **URL:** `https://api.deepseek.com/v1/chat/completions`
- **模型:** `deepseek-chat`

### OpenAI
- **URL:** `https://api.openai.com/v1/chat/completions`
- **模型:** `gpt-3.5-turbo`

### Claude (Anthropic)
- **URL:** `https://api.anthropic.com/v1/messages`
- **模型:** `claude-3-sonnet-20240229`

---

## 项目结构

```
ai-fortune-app/
├── app/
│   ├── src/main/
│   │   ├── java/com/aifortune/app/
│   │   │   ├── data/          # 数据层
│   │   │   │   ├── local/     # DataStore 本地存储
│   │   │   │   ├── remote/   # API 客户端
│   │   │   │   └── repository/# 仓库实现
│   │   │   ├── di/            # Hilt 依赖注入
│   │   │   ├── domain/        # 领域模型
│   │   │   ├── ui/            # UI 层
│   │   │   │   ├── screens/   # 各个页面
│   │   │   │   ├── navigation/# 导航
│   │   │   │   └── theme/     # 主题
│   │   │   ├── AIFortuneApp.kt
│   │   │   └── MainActivity.kt
│   │   └── res/               # 资源文件
│   └── build.gradle
├── gradle/wrapper/            # Gradle Wrapper
├── build.gradle               # 根构建文件
└── settings.gradle            # 项目设置
```

---

## 技术栈

- **UI**: Jetpack Compose + Material 3
- **架构**: MVVM + Clean Architecture
- **DI**: Hilt
- **网络**: Retrofit + OkHttp
- **存储**: DataStore Preferences
- **异步**: Kotlin Coroutines + Flow
