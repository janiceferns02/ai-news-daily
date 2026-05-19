package jf.janice.ainewsdaily.feature.articles.presentation.util

import android.text.format.DateUtils
import java.time.Instant

fun formatArticleDate(isoDate: String?): String {
    if (isoDate.isNullOrBlank()) return ""
    return try {
        val publishedAtMillis = Instant.parse(isoDate).toEpochMilli()
        DateUtils.getRelativeTimeSpanString(
            publishedAtMillis,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
        ).toString()
    } catch (_: Exception) {
        isoDate
    }
}
