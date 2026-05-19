package jf.janice.ainewsdaily.feature.articles.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jf.janice.ainewsdaily.feature.articles.data.repository.ArticleRepository
import jf.janice.ainewsdaily.feature.articles.presentation.model.ArticleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArticleUiState>(ArticleUiState.Loading)
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            emitState(ArticleUiState.Loading)
            try {
                val articles = articleRepository.getArticles()
                emitState(ArticleUiState.Success(articles))
            } catch (e: Exception) {
                Timber.e(e, "Failed to load articles")
                emitState(
                    ArticleUiState.Error(
                        message = e.message ?: "Failed to load articles",
                    ),
                )
            }
        }
    }

    private fun emitState(state: ArticleUiState) {
        logState(state)
        _uiState.value = state
    }

    private fun logState(state: ArticleUiState) {
        when (state) {
            is ArticleUiState.Loading -> {
                Timber.d("Emitting ArticleUiState.Loading")
            }

            is ArticleUiState.Success -> {
                Timber.d(
                    "Emitting ArticleUiState.Success with %d articles: %s",
                    state.articles.size,
                    state.articles,
                )
            }

            is ArticleUiState.Error -> {
                Timber.d(
                    "Emitting ArticleUiState.Error with message: %s",
                    state.message,
                )
            }
        }
    }
}