package system

import koncurrent.Later
import system.picker.files.FilePickerPermissionsManager

interface DirectoryPicker {
    val permission: FilePickerPermissionsManager
    fun openDirChooser(): Later<LocalFile?>
}