# AI Fortune 算命 App 开发工作流

## 项目概述

- **项目名称**: AI Fortune (AI算命)
- **类型**: Android 原生应用
- **技术栈**: Kotlin + Jetpack Compose + Material 3 + MVVM + Hilt
- **核心功能**: 集成多种传统命理分析工具，支持用户自定义大模型API

---

## 开发环境搭建

### 1. 安装必要工具

```bash
# 安装 JDK 17+
sudo apt install openjdk-17-jdk

# 下载 Android SDK Command Line Tools
mkdir -p $ANDROID_HOME/cmdline-tools
cd $ANDROID_HOME/cmdline-tools
wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
unzip commandlinetools-linux-11076708_latest.zip
mv cmdline-tools latest

# 接受许可并安装 SDK
yes | sdkmanager --licenses
sdkmanager "platforms;android-34" "build-tools;34.0.0" "platform-tools"
```

### 2. 环境变量配置

```bash
# 添加到 ~/.bashrc 或 ~/.zshrc
export ANDROID_HOME=/opt/android-sdk
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools
```

---

## 项目创建流程

### 步骤 1: 创建项目结构

```
ai-fortune-app/
├── SPEC.md                    # 规格文档
├── build.gradle              # 根构建配置
├── settings.gradle           # 项目设置
├── gradle.properties         # Gradle配置
├── gradlew                  # Gradle wrapper
└── app/
    ├── build.gradle          # App模块配置
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml
        ├── res/              # 资源文件
        └── java/com/aifortune/app/
            ├── AIFortuneApp.kt
            ├── MainActivity.kt
            ├── di/           # 依赖注入模块
            ├── domain/       # 领域层
            │   ├── model/   # 数据模型
            │   └── repository/
            ├── data/        # 数据层
            │   ├── local/   # DataStore本地存储
            │   ├── remote/  # Retrofit API调用
            │   └── repository/
            └── ui/          # UI层
                ├── theme/    # Compose主题
                ├── components/
                ├── navigation/
                └── screens/  # 各功能页面
```

### 步骤 2: 配置文件

**build.gradle (根目录)**
```groovy
plugins {
    id 'com.android.application' version '8.2.2' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.22' apply false
    id 'com.google.dagger.hilt.android' version '2.50' apply false
    id 'org.jetbrains.kotlin.plugin.serialization' version '1.9.22' apply false
}
```

**settings.gradle**
```groovy
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
rootProject.name = 'AIFortune'
include ':app'
```

### 步骤 3: 核心代码实现

1. **数据模型** (`domain/model/Models.kt`)
   - ApiConfig: API配置
   - FortuneInput: 算命输入
   - FortuneResult: 算命结果
   - FortuneType: 枚举（八字、星座、塔罗等）

2. **本地存储** (`data/local/LocalDataStore.kt`)
   - 使用 DataStore 存储 API 配置
   - 支持保存、删除、读取配置

3. **远程API** (`data/remote/LlmApiClient.kt`)
   - Retrofit + OkHttp
   - 通用 LLM API 调用

4. **仓库层** (`data/repository/FortuneRepositoryImpl.kt`)
   - 构建各功能的 prompt
   - 调用 LLM API 获取结果

5. **UI层**
   - 各功能 Screen + ViewModel
   - 使用 Hilt 依赖注入

---

## 功能模块说明

| 模块 | 描述 | 核心输入 |
|------|------|----------|
| 八字命理 | 深度分析命理格局 | 姓名、出生时间、性别 |
| 求学建议 | 学业规划、备考策略 | 年级、目标学校 |
| 求商建议 | 商业决策分析 | 商业想法、预算、风险偏好 |
| 星座分析 | 12星座运势 | 星座、问题类型 |
| 姓名分析 | 姓名含义五行 | 姓名、出生年份 |
| 塔罗牌 | 神秘塔罗解读 | 抽取的牌 |
| 起名字 | AI生成名字 | 姓氏、性别 |

---

## 构建与运行

### 构建 Debug APK
```bash
cd ai-fortune-app
./gradlew assembleDebug
```

### 输出位置
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 后续迭代建议

1. **接入企业级API**: 在 ApiPanelScreen 添加统一配置入口
2. **添加更多算命类型**: 相术、风水、奇门遁甲等
3. **美化塔罗牌界面**: 添加真实塔罗牌图片
4. **历史记录功能**: 保存查询历史
5. **分享功能**: 将结果分享到社交平台

---

## 技术债务

- [ ] 添加单元测试
- [ ] 添加 UI 测试
- [ ] 优化网络请求错误处理
- [ ] 添加加载动画
- [ ] 添加结果缓存

---

*文档创建时间: 2026-05-09*
*维护者: Caro*
