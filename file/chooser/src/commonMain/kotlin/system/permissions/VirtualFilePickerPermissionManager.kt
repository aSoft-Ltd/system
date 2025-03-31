package system.permissions

import system.Permission

class VirtualFilePickerPermissionManager(
    private val result: Permission = Permission.Granted
) : FileChooserPermissionsManager {
    override fun check(): Permission = result
    override fun request(): Permission = result
}