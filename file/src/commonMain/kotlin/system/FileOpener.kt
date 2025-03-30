package system

import koncurrent.Later

interface FileOpener {
    fun open(url: String): Later<String>
}