package jf.janice.ainewsdaily.feature.sources.data.model

import com.squareup.moshi.JsonClass
import jf.janice.ainewsdaily.feature.sources.presentation.model.SourceData

@JsonClass(generateAdapter = true)
data class SourcesResponseDto(
    val status: String,
    val sources: List<SourceDto>,
)

@JsonClass(generateAdapter = true)
data class SourceDto(
    val id: String,
    val name: String,
    val description: String?,
    val url: String?
)

fun SourceDto.toSourceData(): SourceData {
    return SourceData(
        id = id,
        name = name,
        description = description,
        url = url
    )
}
