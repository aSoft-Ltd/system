package system

import koncurrent.FailedLater
import koncurrent.Later
import system.permissions.VirtualFilePickerPermissionManager

class NoShowFileChooser(private val result: Permission = Permission.Denied) : FileChooser {
    override val permission by lazy { VirtualFilePickerPermissionManager(result) }
    override fun openFileChooser(
        extensions: List<String>,
        multiple: Boolean
    ): Later<List<LocalFile>> = FailedLater(
        NotImplementedError("trying to choose a file using a NoShowFileChooser")
    )

    override fun openDirChooser(): Later<LocalFile?> = FailedLater(
        IllegalStateException("trying to choose a directory using a NoShowFileChooser")
    )
}