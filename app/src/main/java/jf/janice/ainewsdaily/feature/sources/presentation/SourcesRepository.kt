package jf.janice.ainewsdaily.feature.sources.presentation

import jf.janice.ainewsdaily.feature.sources.presentation.model.SourceData

interface SourcesRepository {
    suspend fun getSources(): List<SourceData>
}