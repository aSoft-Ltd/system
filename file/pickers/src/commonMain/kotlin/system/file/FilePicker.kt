package system.file

import koncurrent.Later
import system.PickerResponse
import system.file.mime.All
import system.file.mime.Mime

interface FilePicker {
    fun openPicker(
        mimes: List<Mime> = listOf(All),
        multiple: Boolean = false
    ): Later<PickerResponse>
}