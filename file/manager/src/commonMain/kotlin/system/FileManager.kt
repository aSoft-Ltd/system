package system

interface FileManager : FileOpener, FileSaver, FileReader {
    fun exists(file: LocalFile): Boolean
    fun info(file: LocalFile): FileInfo
}