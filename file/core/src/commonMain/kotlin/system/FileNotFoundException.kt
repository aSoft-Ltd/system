package system

open class FileNotFoundException(val path: String) : IllegalArgumentException("File with path=$path is not found")