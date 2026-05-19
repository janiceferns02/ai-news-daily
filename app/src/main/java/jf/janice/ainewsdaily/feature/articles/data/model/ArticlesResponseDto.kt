package jf.janice.ainewsdaily.feature.articles.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import jf.janice.ainewsdaily.feature.articles.presentation.model.ArticleData

@JsonClass(generateAdapter = true)
data class ArticlesResponseDto(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>,
)

@JsonClass(generateAdapter = true)
data class ArticleDto(
    val author: String?,
    val title: String?,
    val description: String?,
    @Json(name = "urlToImage")
    val urlToImage: String?,
    @Json(name = "publishedAt")
    val publishedAt: String?,
)

fun ArticleDto.toArticleData(): ArticleData {
    return ArticleData(
        author = author,
        title = title,
        description = description,
        imageUrl = urlToImage,
        date = publishedAt,
    )
}
