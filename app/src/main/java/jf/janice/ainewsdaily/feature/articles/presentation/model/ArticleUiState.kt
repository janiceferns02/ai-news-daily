package jf.janice.ainewsdaily.feature.articles.presentation.model

sealed interface ArticleUiState {
    object Loading : ArticleUiState

    data class Success(
        val articles: List<ArticleData>,
    ) : ArticleUiState

    data class Error(
        val message: String,
    ) : ArticleUiState
}
