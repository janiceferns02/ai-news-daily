package jf.janice.ainewsdaily.feature.sources.data.repository

import jf.janice.ainewsdaily.feature.sources.data.network.SourceApi
import jf.janice.ainewsdaily.feature.sources.data.model.toSourceData
import jf.janice.ainewsdaily.feature.sources.presentation.SourcesRepository
import jf.janice.ainewsdaily.feature.sources.presentation.model.SourceData
import javax.inject.Inject

class SourceRepositoryImpl @Inject constructor(
    private val sourceApi: SourceApi,
): SourcesRepository {
    override suspend fun getSources(): List<SourceData> {
        return sourceApi.getSources().sources.map { source ->
            source.toSourceData()
        }
    }
}
