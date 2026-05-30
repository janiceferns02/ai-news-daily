package jf.janice.ainewsdaily.feature.articles.presentation.model

sealed interface ArticleUiEvent {
    data class ShowSnackBar(val message: String) : ArticleUiEvent
}