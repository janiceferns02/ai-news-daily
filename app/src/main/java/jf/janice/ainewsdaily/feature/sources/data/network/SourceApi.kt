package jf.janice.ainewsdaily.feature.sources.data.network

import jf.janice.ainewsdaily.BuildConfig
import jf.janice.ainewsdaily.feature.sources.data.model.SourcesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SourceApi {
    @GET("v2/top-headlines/sources")
    suspend fun getSources(
        @Query("country") country: String = "us",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
    ): SourcesResponseDto
}