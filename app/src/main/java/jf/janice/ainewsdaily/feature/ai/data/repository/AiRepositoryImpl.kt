package jf.janice.ainewsdaily.feature.ai.data.repository

import com.squareup.moshi.Moshi
import jf.janice.ainewsdaily.BuildConfig
import jf.janice.ainewsdaily.feature.ai.data.network.OpenAiApi
import jf.janice.ainewsdaily.feature.ai.data.network.OpenAiRequest
import jf.janice.ainewsdaily.feature.ai.data.network.SummaryResponse
import jf.janice.ainewsdaily.feature.ai.presentation.model.AiRepository
import jf.janice.ainewsdaily.feature.ai.presentation.model.AiSummary
import javax.inject.Inject

class AiRepositoryImpl @Inject constructor(
    private val gptApi: OpenAiApi,
    private val moshi: Moshi
): AiRepository {

    override suspend fun getAiNewsSummary(
        articleTitles : List<String>,
    ) : AiSummary {

        if(articleTitles.isEmpty())
            return AiSummary("No news is available")

        val prompt = buildPrompt(articleTitles)

        val response = gptApi.summarizeNews(
            OpenAiRequest(
                model = "gpt-5-mini",
                input = prompt
            ),
            token = BuildConfig.OPEN_API_KEY
        )

        val responseText = response.output
            .firstOrNull { it.type == "message" }
            ?.content
            ?.firstOrNull()
            ?.text
            .orEmpty()

        val summary = try {
            moshi.adapter(SummaryResponse::class.java)
                .fromJson(responseText)
                ?.summary
                .orEmpty()
        } catch (e: Exception) {
            responseText
        }

        return AiSummary(summary)
    }

    private fun buildPrompt(articleTitles: List<String>): String {

        val articleTitlesText = articleTitles.mapIndexed { index, articleTitle ->
            "${index+1}. $articleTitle"
        }.joinToString("\n")

        return """
            You are a professional news editor.
            
            Based only on the following headlines, generate a concise summary of the day's news.

            Requirements:
            - Exactly 2 sentences
            - Maximum 25 words per sentence
            - Focus only on the most important developments.
            - Neutral and factual tone
            - Do not invent information
            - Only use information implied by the headlines
            
            Return ONLY valid JSON:

            {
              "summary": ""
            }
            
            Headlines:
            
            $articleTitlesText
        """.trimIndent()

    }

}