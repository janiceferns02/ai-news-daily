package jf.janice.ainewsdaily.feature.sources.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jf.janice.ainewsdaily.core.util.isNetworkError
import jf.janice.ainewsdaily.feature.sources.data.repository.SourceRepository
import jf.janice.ainewsdaily.feature.sources.presentation.model.SourceErrorType
import jf.janice.ainewsdaily.feature.sources.presentation.model.SourceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SourceViewModel @Inject constructor(
    private val sourceRepository: SourceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SourceUiState>(SourceUiState.Loading)
    val uiState: StateFlow<SourceUiState> = _uiState.asStateFlow()

    init {
        loadSources()
    }

    fun loadSources() {
        viewModelScope.launch {

           emitState(SourceUiState.Loading)

            try {
                val sources = sourceRepository.getSources()
                emitState(
                    SourceUiState.Success(
                        sources = sources,
                    ),
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to load sources")

                val errorType = if (e.isNetworkError()) {
                    SourceErrorType.Network
                } else {
                    SourceErrorType.Generic
                }
                emitState(SourceUiState.Error(type = errorType))
            }
        }
    }

    private fun emitState(state: SourceUiState) {
        logState(state)
        _uiState.value = state
    }

    private fun logState(state: SourceUiState) {
        when (state) {
            is SourceUiState.Loading -> {
                Timber.d("Emitting SourceUiState.Loading")
            }

            is SourceUiState.Success -> {
                Timber.d(
                    "Emitting SourceUiState.Success with %d sources: %s",
                    state.sources.size,
                    state.sources,
                )
            }

            is SourceUiState.Error -> {
                Timber.d(
                    "Emitting SourceUiState.Error with type: %s",
                    state.type,
                )
            }
        }
    }
}
