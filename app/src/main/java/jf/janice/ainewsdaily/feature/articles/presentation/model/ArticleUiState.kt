package jf.janice.ainewsdaily.feature.articles.presentation.model

import jf.janice.ainewsdaily.feature.ai.presentation.model.AiSummary

sealed interface ArticleUiState {
    object Loading : ArticleUiState

    data class Success(
        val articles: List<ArticleData>,
        val isLoadingMore: Boolean = false,
        val isRefreshing: Boolean = false,
        val aiSummary: AiSummary? = null,
        val isAiSummaryLoading: Boolean = false
    ) : ArticleUiState

    data class Error(
        val type: ArticleErrorType,
    ) : ArticleUiState
}

enum class ArticleErrorType {
    Network,
    Generic,
}
