package system.file.permissions

import koncurrent.Later
import system.Permission
import system.file.mime.Mime
import system.file.FilePickerPermissionsManager

class VirtualFilePickerPermissionManager(
    private val result: Permission = Permission.Granted
) : FilePickerPermissionsManager {
    override fun check(mimes: List<Mime>): Permission = result
    override fun request(mimes: List<Mime>): Later<Permission> = Later(result)
}