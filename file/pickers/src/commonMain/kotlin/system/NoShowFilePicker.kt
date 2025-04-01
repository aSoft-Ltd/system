package system

import koncurrent.FailedLater
import koncurrent.Later
import system.file.mime.Mime
import system.permissions.VirtualFilePickerPermissionManager
import system.picker.files.FilePicker

class NoShowFilePicker(private val result: Permission = Permission.Denied) : FilePicker {
    override val permission by lazy { VirtualFilePickerPermissionManager(result) }
    override fun openFileChooser(
        mimes: List<Mime>,
        multiple: Boolean
    ): Later<List<LocalFile>> = FailedLater(
        NotImplementedError("trying to choose a file using a NoShowFileChooser")
    )
}