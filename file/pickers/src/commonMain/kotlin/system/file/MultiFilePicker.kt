package system.file

import system.file.mime.All
import system.file.mime.Mime
import system.file.response.MultiPickerResponse

interface MultiFilePicker {
    suspend fun open(
        mimes: List<Mime> = listOf(All),
        limit: PickerLimit = PickerLimit.Default,
    ): MultiPickerResponse
}