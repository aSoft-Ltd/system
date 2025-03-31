package system.permissions

import system.Permission

interface FileChooserPermissionsManager {
    fun check(): Permission
    fun request(): Permission
}