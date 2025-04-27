package system.file

import system.file.mime.Image
import system.file.mime.MediaMime
import system.file.mime.Video
import system.file.picker.response.MultiPickerResponse

interface MultiMediaPicker {
    suspend fun open(
        mimes: List<MediaMime> = listOf(Image, Video),
        limit: PickerLimit = PickerLimit.Default,
    ): MultiPickerResponse
}