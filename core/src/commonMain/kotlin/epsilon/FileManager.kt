package epsilon

interface FileManager : FileOpener, FileSaver, FileReader, FileDownloader {
    val create: FileCreator
}