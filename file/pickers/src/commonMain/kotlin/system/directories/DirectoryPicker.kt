package system.directories

import koncurrent.Later
import system.LocalFile
import system.file.FilePickerPermissionsManager

interface DirectoryPicker {
    val permission: FilePickerPermissionsManager
    fun openDirChooser(): Later<LocalFile?>
}