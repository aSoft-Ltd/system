package system

import koncurrent.Later

interface FileSaver {
    fun save(file: LocalFile, name: String? = null): Later<String>
}