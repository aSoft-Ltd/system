package epsilon

import koncurrent.Later

interface FileOpener {
    fun open(url: String): Later<String>
}