package jf.janice.ainewsdaily.feature.articles.presentation.model

sealed interface ArticleUiState {
    object Loading : ArticleUiState

    data class Success(
        val articles: List<ArticleData>,
        val isLoadingMore: Boolean = false,
        val isRefreshing: Boolean = false
    ) : ArticleUiState

    data class Error(
        val type: ArticleErrorType,
    ) : ArticleUiState
}

enum class ArticleErrorType {
    Network,
    Generic,
}
