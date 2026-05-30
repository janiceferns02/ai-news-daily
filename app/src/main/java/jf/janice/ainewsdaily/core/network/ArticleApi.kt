package jf.janice.ainewsdaily.core.network

import jf.janice.ainewsdaily.BuildConfig
import jf.janice.ainewsdaily.feature.articles.data.model.ArticlesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {
    @GET("v2/top-headlines")
    suspend fun getArticles(
        @Query("country") country: String = "us",
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
    ): ArticlesResponseDto
}