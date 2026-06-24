package jf.janice.ainewsdaily.feature.articles.data.repository

import jf.janice.ainewsdaily.feature.articles.data.network.ArticleApi
import jf.janice.ainewsdaily.feature.articles.data.model.toArticleData
import jf.janice.ainewsdaily.feature.articles.presentation.ArticleRepository
import jf.janice.ainewsdaily.feature.articles.presentation.model.ArticleData
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleApi: ArticleApi,
): ArticleRepository {
    override suspend fun getArticles(
        page: Int,
    ): List<ArticleData> {
        return articleApi.getArticles(
            page = page
        ).articles.map { article ->
            article.toArticleData()
        }
    }
}