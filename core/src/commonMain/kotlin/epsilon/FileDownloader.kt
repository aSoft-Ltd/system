package epsilon

import koncurrent.Later

interface FileDownloader {
    fun download(url: String, name: String? = null, headers: Map<String, String> = mapOf()): Later<RawFile>
}