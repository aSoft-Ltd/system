package system

import koncurrent.Later
import system.file.mime.Mime
import system.file.FilePickerPermissionsManager

class OSXFilePickerPermissionManager : FilePickerPermissionsManager {
    override fun check(mimes: List<Mime>): Permission {
        TODO("Not yet implemented")
    }

    override fun request(mimes: List<Mime>): Later<Permission> {
        TODO("Not yet implemented")
    }
}