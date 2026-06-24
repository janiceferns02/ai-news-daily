package jf.janice.ainewsdaily.feature.sources.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import jf.janice.ainewsdaily.feature.sources.presentation.model.SourceData
import jf.janice.ainewsdaily.feature.sources.presentation.model.SourceErrorType
import jf.janice.ainewsdaily.feature.sources.presentation.model.SourceUiState
import jf.janice.ainewsdaily.feature.sources.presentation.viewmodel.SourceViewModel
import jf.janice.ainewsdaily.ui.theme.AINewsDailyTheme
import java.net.URI

@Composable
fun SourcesScreen(
    modifier: Modifier = Modifier,
    viewModel: SourceViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SourcesScreenContent(
        uiState = uiState,
        onRetry = viewModel::loadSources,
        modifier = modifier,
    )
}

@Composable
fun SourcesScreenContent(
    modifier: Modifier = Modifier,
    uiState: SourceUiState,
    onRetry: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.sources),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
        )

        when (uiState) {
            is SourceUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is SourceUiState.Error -> {
                SourceErrorContent(
                    errorType = uiState.type,
                    onRetry = onRetry,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is SourceUiState.Success -> {

                if (uiState.sources.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No sources available",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        itemsIndexed(uiState.sources) { index, source ->
                            SourceListItem(source = source)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SourceListItem(
    source: SourceData,
    modifier: Modifier = Modifier,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        onClick = {},

    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {

            val domain = remember(source.url) {
                try {
                    URI(source.url).host?.removePrefix("www.") ?: ""
                } catch (e: Exception) {
                    ""
                }
            }

            if(domain.isNotEmpty()) {
                val sourceLogoUrl = "https://icons.duckduckgo.com/ip3/$domain.ico"

                AsyncImage(
                    model = sourceLogoUrl,
                    contentDescription = "${source.name} logo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(modifier = modifier
                .fillMaxWidth()
            ) {
                Text(
                    text = source.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                source.description?.takeIf { it.isNotBlank() }?.let { description ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun SourceErrorContent(
    errorType: SourceErrorType,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val messageResId = when (errorType) {
        SourceErrorType.Network -> R.string.error_network
        SourceErrorType.Generic -> R.string.error_generic
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

            if (errorType == SourceErrorType.Network) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onRetry) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SourcesScreenContentPreview() {
    val sampleSources = listOf(
        SourceData(
            id = "abc-news",
            name = "ABC News",
            description = "Your trusted source for breaking news, analysis, and exclusive interviews.",
            url = ""
        ),
        SourceData(
            id = "bbc-news",
            name = "BBC News",
            description = "Use BBC News for up-to-the-minute news, breaking news, video, audio and feature stories.",
            url = ""
        ),
    )

    AINewsDailyTheme {
        SourcesScreenContent(
            uiState = SourceUiState.Success(sampleSources),
        )
    }
}
