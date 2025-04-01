package system.file

import koncurrent.Later
import system.Permission
import system.file.mime.All
import system.file.mime.Mime

interface PickerPermissionsManager {
    fun check(mimes: List<Mime> = listOf(All)): Permission
    fun request(mimes: List<Mime> = listOf(All)): Later<Permission>
}