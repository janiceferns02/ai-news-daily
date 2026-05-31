package jf.janice.ainewsdaily.feature.articles.data.repository

import jf.janice.ainewsdaily.feature.articles.data.network.ArticleApi
import jf.janice.ainewsdaily.feature.articles.data.model.toArticleData
import jf.janice.ainewsdaily.feature.articles.presentation.model.ArticleData
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val articleApi: ArticleApi,
) {
    suspend fun getArticles(
        page: Int,
    ): List<ArticleData> {
        return articleApi.getArticles(
            page = page
        ).articles.map { article ->
            article.toArticleData()
        }
    }
}