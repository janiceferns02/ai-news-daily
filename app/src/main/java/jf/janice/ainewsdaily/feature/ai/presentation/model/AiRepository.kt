package jf.janice.ainewsdaily.feature.ai.presentation.model

interface AiRepository {
    suspend fun getAiNewsSummary(articleTitles : List<String>): AiSummary
}