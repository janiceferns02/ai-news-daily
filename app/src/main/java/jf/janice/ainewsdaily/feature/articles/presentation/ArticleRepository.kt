package jf.janice.ainewsdaily.feature.articles.presentation

import jf.janice.ainewsdaily.feature.articles.presentation.model.ArticleData

interface ArticleRepository {
    suspend fun getArticles(page: Int) : List<ArticleData>
}