package system.file

import system.GB
import system.MemorySize
import system.file.mime.Image
import system.file.mime.MediaMime
import system.file.mime.Video
import system.file.response.SingleFileResponse

interface SingleMediaPicker {
    suspend fun open(
        mimes: List<MediaMime> = listOf(Image, Video),
        limit: MemorySize = 2.GB,
    ): SingleFileResponse
}