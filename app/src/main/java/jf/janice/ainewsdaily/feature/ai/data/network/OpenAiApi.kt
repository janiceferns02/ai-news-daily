package jf.janice.ainewsdaily.feature.ai.data.network

import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApi {
    @POST("v1/responses")
    suspend fun summarizeNews(
        @Body request: OpenAiRequest,
        @Header("Authorization") token: String,
    ) : OpenAiResponse
}

@JsonClass(generateAdapter = true)
data class OpenAiRequest(
    val model: String,
    val input: String
)

@JsonClass(generateAdapter = true)
data class SummaryResponse(
    val summary: String
)

@JsonClass(generateAdapter = true)
data class OpenAiResponse(
    val output: List<Output>,
)

@JsonClass(generateAdapter = true)
data class Output(
    val content: List<Content>,
    val type: String,
)

@JsonClass(generateAdapter = true)
data class Content(
    val text: String? = null
)