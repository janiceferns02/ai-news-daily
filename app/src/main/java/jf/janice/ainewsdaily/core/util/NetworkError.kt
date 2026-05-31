package jf.janice.ainewsdaily.core.util

import java.io.IOException

fun Throwable.isNetworkError(): Boolean {
    var cause: Throwable? = this
    while (cause != null) {
        if (cause is IOException) {
            return true
        }
        cause = cause.cause
    }
    return false
}
