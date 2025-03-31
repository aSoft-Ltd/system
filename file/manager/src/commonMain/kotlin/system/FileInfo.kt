package system

interface FileInfo {
    val file: LocalFile
    fun name(extension: Boolean = true): String
    fun extension(): String
    fun size(): MemorySize
}