package system

interface FileInfo {
    val file: LocalFile
    fun name(extension: Boolean = true): String
    fun extension(): String

    /**
     * Returns the size of the file.
     *
     * This operation may be expensive, as it may require reading the file's metadata.
     * or reading entire files
     */
    suspend fun size(): MemorySize
}