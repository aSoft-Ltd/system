package epsilon.internal

internal fun String.filename(): String? {
    if (!contains("/")) return null
    return substringBefore("?").substringAfterLast("/")
}