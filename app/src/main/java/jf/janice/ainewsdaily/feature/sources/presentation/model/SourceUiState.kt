package jf.janice.ainewsdaily.feature.sources.presentation.model

sealed interface SourceUiState {
    object Loading : SourceUiState

    data class Success(
        val sources: List<SourceData>,
    ) : SourceUiState

    data class Error(
        val type: SourceErrorType,
    ) : SourceUiState
}

enum class SourceErrorType {
    Network,
    Generic,
}
