package system

import koncurrent.Later
import system.permissions.FileChooserPermissionsManager

interface FileChooser {
    val permission: FileChooserPermissionsManager

    fun openFileChooser(
        extensions: List<String> = listOf("*"),
        multiple: Boolean = false
    ): Later<List<LocalFile>>

    fun openDirChooser(): Later<LocalFile?>
}