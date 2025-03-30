package system

import koncurrent.Later

interface FileSaver {
    fun save(url: String, name: String? = null) : Later<String>
}