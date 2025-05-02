package system.file

import system.GB
import system.MemorySize
import system.file.mime.All
import system.file.mime.Mime
import system.file.response.SingleFileResponse

interface SingleFilePicker {
    suspend fun open(
        mimes: List<Mime> = listOf(All),
        limit: MemorySize = 2.GB,
    ): SingleFileResponse
}