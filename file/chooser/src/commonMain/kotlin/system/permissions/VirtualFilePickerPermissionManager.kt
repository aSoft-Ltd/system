package system.permissions

import koncurrent.Later
import system.Permission
import system.picker.files.FilePickerPermissionsManager

class VirtualFilePickerPermissionManager(
    private val result: Permission = Permission.Granted
) : FilePickerPermissionsManager {
    override fun check(): Permission = result
    override fun request(): Later<Permission> = Later(result)
}