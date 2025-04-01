package system.file.mime;

interface Mime {
    val name: String
    val text: String

    fun matches(extension: String): Boolean = when {
        text.endsWith("/*") -> true
        else -> {
            val ext = text.split("/").last()
            ext == extension
        }
    }
}