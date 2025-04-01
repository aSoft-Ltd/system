package system.picker.files

import koncurrent.Later
import system.LocalFile
import system.file.mime.All
import system.file.mime.Mime

interface FilePicker {
    val permission: FilePickerPermissionsManager

    fun openFileChooser(
        mimes: List<Mime> = listOf(All),
        multiple: Boolean = false
    ): Later<List<LocalFile>>
}