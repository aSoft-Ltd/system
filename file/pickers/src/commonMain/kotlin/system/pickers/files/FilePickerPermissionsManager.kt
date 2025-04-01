package system.picker.files

import koncurrent.Later
import system.Permission

interface FilePickerPermissionsManager {
    fun check(): Permission
    fun request(): Later<Permission>
}