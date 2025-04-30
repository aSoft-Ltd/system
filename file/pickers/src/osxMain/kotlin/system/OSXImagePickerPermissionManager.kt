package system

import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary
import system.file.PickerPermissionsManager
import system.file.mime.Mime
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class OSXImagePickerPermissionManager : PickerPermissionsManager {
    override fun check(mimes: List<Mime>): Permission = PHPhotoLibrary.authorizationStatus().toPermission()

    override suspend fun request(mimes: List<Mime>): Permission {
        val permission = check(mimes)
        if (permission != Permission.Unauthorized) return permission
        return suspendCoroutine { cont ->
            PHPhotoLibrary.requestAuthorization { status ->
                cont.resume(status.toPermission())
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