## AI News Daily 📱🤖

**AI News Daily is an Android application designed to aggregate top global headlines and news sources. The application provides users with contextual, instant, AI-generated summaries of the day's events. Built using Modern Android Development guidelines, the project showcases strict separation of concerns, modern concurrency workflows via Kotlin Coroutines/Channels, and a declarative UI utilizing Jetpack Compose and Material 3 design systems.**

## Screenshots

| Top Stories Feed | Publication Sources | AI Summary Sheet |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/f1128b0e-4c5d-4772-a2c2-1f1614907db9" width="250" /> | <img src="https://github.com/user-attachments/assets/b753e16b-dce8-4453-b7ce-ce8dcabfc98b" width="250" /> | <img src="https://github.com/user-attachments/assets/08e0484c-802b-47b9-a8a9-7af9dec249c4" width="250" /> |

## 🚀 Core Features

* 📰 Top Stories Feed: Beautifully structured Material 3 card items rendering major worldwide breaking news with asynchronous image loading and safe caching.

* 🔮 AI Summary: An optimized Floating Action Button (FAB) that passes daily headlines to the OpenAI Chat Responses API (gpt-5o-mini), parsing strict JSON payloads to generate a beautiful, concise summary of the day.

* 🌐 Verified Sources Directory: A decoupled hub tracking global news publishers, integrated with automated favicon/brand-logo resolution via top-level domain parsing.

* 🔄 Infinite Scroll & Refresh: Built-in endless pagination, atomic state handling via Job cancellations, and a PullToRefreshBox.

## 🛠️ Architecture & Tech Stack

This project is built using **Clean Architecture** and **MVVM**, emphasizing a decoupled, reactive, and highly scalable codebase.

* **Core Language:** Kotlin
* **UI Framework:** Jetpack Compose (Declarative layouts with Material 3)
* **Asynchronous Engine:** Kotlin Coroutines & Asynchronous Data Streams (Flow, StateFlow, Channel)
* **Dependency Injection:** Hilt (Interface-driven modules)
* **Network Stack:** Retrofit / OkHttp
* **Serialization:** Moshi (with custom runtime JSON adapter reflection transformations)
* **Image Pipeline:** Coil
* **Logging Engine:** Timber

## ⚙️ Quick Setup & Installation

1. **Clone the project:**
```bash

   git clone [https://github.com/YOUR_GITHUB_USERNAME/ai-news-daily.git](https://github.com/YOUR_GITHUB_USERNAME/ai-news-daily.git)
   cd ai-news-daily

```

2. **Supply your API configuration environment keys:**
   Open your environment secrets configurations or include them directly inside your local `local.properties` file:
   
   ```bash
   
   NEWS_API_KEY="your_news_api_org_credential"
   OPEN_AI_API_KEY="your_openai_secret_token"
   ```

Compile & Run:
Open the file tree inside Android Studio and run the app target on your designated virtual or physical device!

## Author

Janice Fernandes

