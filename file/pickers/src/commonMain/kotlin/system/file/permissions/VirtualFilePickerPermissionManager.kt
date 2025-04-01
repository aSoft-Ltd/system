package system.file.permissions

import koncurrent.Later
import system.Permission
import system.file.mime.Mime
import system.file.PickerPermissionsManager

class VirtualFilePickerPermissionManager(
    private val result: Permission = Permission.Granted
) : PickerPermissionsManager {
    override fun check(mimes: List<Mime>): Permission = result
    override fun request(mimes: List<Mime>): Later<Permission> = Later(result)
}