package jf.janice.ainewsdaily.feature.articles.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import jf.janice.ainewsdaily.R
import jf.janice.ainewsdaily.feature.articles.presentation.model.ArticleData
import jf.janice.ainewsdaily.feature.articles.presentation.model.ArticleErrorType
import jf.janice.ainewsdaily.feature.articles.presentation.model.ArticleUiState
import jf.janice.ainewsdaily.feature.articles.presentation.util.formatArticleDate
import jf.janice.ainewsdaily.feature.articles.presentation.viewmodel.ArticleViewModel
import jf.janice.ainewsdaily.ui.theme.AINewsDailyTheme

@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    viewModel: ArticleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArticleScreenContent(
        uiState = uiState,
        onRetry = viewModel::loadArticles,
        modifier = modifier,
    )
}

@Composable
fun ArticleScreenContent(
    modifier: Modifier = Modifier,
    uiState: ArticleUiState,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.top_stories),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
        )

        when (uiState) {
            is ArticleUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is ArticleUiState.Error -> {
                ArticleErrorContent(
                    errorType = uiState.type,
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is ArticleUiState.Success -> {
                if (uiState.articles.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No articles available",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        itemsIndexed(uiState.articles) { index, article ->
                            if (index == 0) {
                                FeaturedArticleItem(article = article)
                            } else {
                                CompactArticleItem(article = article)
                            }
                            if (index < uiState.articles.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 16.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ArticleErrorContent(
    errorType: ArticleErrorType,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val messageResId = when (errorType) {
        ArticleErrorType.Network -> R.string.error_network
        ArticleErrorType.Generic -> R.string.error_generic
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(messageResId),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )

            if (errorType == ArticleErrorType.Network) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }
    }
}

@Composable
private fun FeaturedArticleItem(
    article: ArticleData,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ArticleImage(
            imageUrl = article.imageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp)),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = article.title.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        article.description?.takeIf { it.isNotBlank() }?.let { description ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        ArticleDateRow(date = article.date)
    }
}

@Composable
private fun CompactArticleItem(
    article: ArticleData,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
            ) {
                Text(
                    text = article.title.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                article.description?.takeIf { it.isNotBlank() }?.let { description ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            ArticleImage(
                imageUrl = article.imageUrl,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }

        ArticleDateRow(date = article.date)
    }
}

@Composable
private fun ArticleDateRow(
    date: String?,
    modifier: Modifier = Modifier,
) {
    val formattedDate = formatArticleDate(date)
    if (formattedDate.isBlank()) return

    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = formattedDate,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.End,
    )
}

@Composable
private fun ArticleImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    if (imageUrl.isNullOrBlank()) {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        )
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.article_image_content_description),
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleScreenContentPreview() {
    val sampleArticles = listOf(
        ArticleData(
            author = "Author",
            title = "Powerhouse storm could unleash heavy snow and wild temperature swings",
            description = "A major storm system is expected to bring severe weather across multiple regions this week.",
            imageUrl = null,
            date = "2026-05-19T10:00:00Z",
        ),
        ArticleData(
            author = "Author",
            title = "Tech giants announce new AI partnership",
            description = "Companies are teaming up to accelerate research and product development.",
            imageUrl = null,
            date = "2026-05-19T08:00:00Z",
        ),
    )

    AINewsDailyTheme {
        ArticleScreenContent(
            uiState = ArticleUiState.Success(sampleArticles),
        )
    }
}
