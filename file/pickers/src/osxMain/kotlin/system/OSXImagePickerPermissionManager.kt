package system

import koncurrent.Later
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary
import system.file.PickerPermissionsManager
import system.file.mime.Mime

class OSXImagePickerPermissionManager : PickerPermissionsManager {
    override fun check(mimes: List<Mime>): Permission = PHPhotoLibrary.authorizationStatus().toPermission()

    override fun request(mimes: List<Mime>): Later<Permission> {
        val permission = check(mimes)
        if (permission != Permission.Unauthorized) return Later(permission)
        return Later { resolve, _ ->
            PHPhotoLibrary.requestAuthorization { status ->
                resolve(status.toPermission())
            }
        }
    }

    private fun Long.toPermission() = when (this) {
        PHAuthorizationStatusNotDetermined -> Permission.Unauthorized
        PHAuthorizationStatusAuthorized -> Permission.Granted
        PHAuthorizationStatusLimited -> Permission.Granted
        PHAuthorizationStatusDenied -> Permission.Denied
        PHAuthorizationStatusRestricted -> Permission.Denied
        else -> Permission.Unauthorized
    }
}