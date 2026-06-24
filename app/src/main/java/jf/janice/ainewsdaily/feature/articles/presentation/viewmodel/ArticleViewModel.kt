package jf.janice.ainewsdaily.feature.articles.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jf.janice.ainewsdaily.feature.ai.presentation.model.AiSummary
import jf.janice.ainewsdaily.feature.articles.presentation.model.ArticleErrorType
import jf.janice.ainewsdaily.feature.articles.presentation.model.ArticleUiEvent
import jf.janice.ainewsdaily.feature.articles.presentation.model.ArticleUiState
import jf.janice.ainewsdaily.core.util.isNetworkError
import jf.janice.ainewsdaily.feature.ai.presentation.model.AiRepository
import jf.janice.ainewsdaily.feature.articles.presentation.ArticleRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val aiRepository: AiRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ArticleUiState>(ArticleUiState.Loading)
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<ArticleUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    private var fetchJob: Job? = null
    private var currentPage = 1
    private var hasMorePages = true

    init {
        loadArticles()
    }

    fun loadArticles(isRefresh: Boolean = false) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {

            if(isRefresh && _uiState.value is ArticleUiState.Success) {
                emitState(
                    ArticleUiState.Success(
                        articles = (uiState.value as ArticleUiState.Success).articles,
                        isRefreshing = true,
                    )
                )
            } else {
                emitState(ArticleUiState.Loading)
            }

            try {
                currentPage = 1
                val articles = articleRepository.getArticles(
                    page = currentPage
                )

                hasMorePages = articles.isNotEmpty()
                emitState(
                    ArticleUiState.Success(
                        articles = articles,
                        isLoadingMore = false,
                        isRefreshing = false
                    ),
                )
            } catch (e: Exception) {
                val isNetwork = e.isNetworkError()
                Timber.e(e, "Failed to load articles")
                val stateOnError = _uiState.value

                if(isRefresh && stateOnError is ArticleUiState.Success) {
                    emitState(
                       stateOnError.copy(isRefreshing = false)
                    )

                    val message = if(isNetwork) "Network refresh failed" else "Something went wrong during refresh"
                    _uiEvent.send(ArticleUiEvent.ShowSnackBar(message))
                } else {
                    val errorType = if (isNetwork) {
                        ArticleErrorType.Network
                    } else {
                        ArticleErrorType.Generic
                    }
                    emitState(ArticleUiState.Error(type = errorType))
                }
            }
        }
    }

    fun loadNextPage() {

        val currentState = _uiState.value as? ArticleUiState.Success ?: return
//        if (currentState !is ArticleUiState.Success || currentState.articles.isEmpty()) return

        if (fetchJob?.isActive == true || !hasMorePages || currentState.isRefreshing || currentState.isLoadingMore) return

        fetchJob = viewModelScope.launch {
            emitState(currentState.copy(isLoadingMore = true))
            try {
                val nextPage = currentPage + 1
                val nextArticles = articleRepository.getArticles(
                    page = nextPage
                )
                currentPage = nextPage
                hasMorePages = nextArticles.isNotEmpty()

                emitState(
                    currentState.copy(
                        articles = currentState.articles + nextArticles,
                        isLoadingMore = false,
                    )
                )

            } catch (e: Exception) {
                Timber.e(e, "Failed to load next page: %d", currentPage + 1)
                emitState(currentState.copy(isLoadingMore = false))
            }
        }
    }

    fun getAiSummary() {
        val currentState = _uiState.value as? ArticleUiState.Success ?: return
        if (currentState.isAiSummaryLoading) return

        viewModelScope.launch {
            emitState(currentState.copy(isAiSummaryLoading = true, aiSummary = null))
            try {
                val articleTitles = currentState.articles.mapNotNull { it.title }
                val summaryData = aiRepository.getAiNewsSummary(articleTitles)
                emitState(
                    currentState.copy(
                        isAiSummaryLoading = false,
                        aiSummary = AiSummary(summary = summaryData.summary)
                    )
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to get AI summary")
                emitState(currentState.copy(isAiSummaryLoading = false))
                _uiEvent.send(ArticleUiEvent.ShowSnackBar("Failed to get AI summary"))
            }
        }
    }

    fun dismissAiSummary() {
        val currentState = _uiState.value as? ArticleUiState.Success ?: return
        emitState(currentState.copy(aiSummary = null))
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
                    "Emitting ArticleUiState.Success with %d articles, loadingMore=%s: %s",
                    state.articles.size,
                    state.isLoadingMore,
                    state.articles,
                )
            }

            is ArticleUiState.Error -> {
                Timber.d(
                    "Emitting ArticleUiState.Error with type: %s",
                    state.type,
                )
            }
        }
    }
}