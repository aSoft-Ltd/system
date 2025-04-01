package system

import koncurrent.Later
import koncurrent.SuccessfulLaterValues
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreServices.kUTTypeURL
import platform.Foundation.NSItemProvider
import platform.Foundation.NSURL
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.darwin.NSObject
import system.file.FilePicker
import system.file.mime.Mime
import system.file.toResponse

class OSXImagePicker : FilePicker {

    private val permission = OSXImagePickerPermissionManager()

    private fun launchPicker(mimes: List<Mime>, multiple: Boolean) = Later { resolve, reject ->
        val config = PHPickerConfiguration()
        config.filter = PHPickerFilter.imagesFilter
        config.selectionLimit = if (multiple) 0 else 1

        val picker = PHPickerViewController(configuration = config)
        picker.delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
            override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                picker.dismissModalViewControllerAnimated(animated = true)
                if (didFinishPicking.isEmpty()) {
                    resolve(PickerResponse.Cancelled)
                } else {
                    val results = didFinishPicking.mapNotNull {
                        it as? PHPickerResult
                    }.map {
                        it.itemProvider.toPath()
                    }

                    SuccessfulLaterValues(*results.toTypedArray()).then { urls ->
                        urls.mapNotNull { it }
                    }.then {
                        it.map { LocalFileImpl(it) }
                    }.then {
                        it.toResponse(multiple)
                    }.then {
                        resolve(it)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun NSItemProvider.toPath() = Later { resolve, reject ->
        loadItemForTypeIdentifier(kUTTypeURL.toString(), null) { url, error ->
            if (error != null) {
                return@loadItemForTypeIdentifier reject(IllegalStateException("Failed to convert NSItemProvider into a path: $error"))
            }
            resolve((url as? NSURL)?.path)
        }
    }

    override fun openPicker(mimes: List<Mime>, multiple: Boolean): Later<PickerResponse> {
        if (permission.check(mimes) == Permission.Granted) return launchPicker(mimes, multiple)
        return permission.request(mimes).andThen { permit ->
            when (permit) {
                Permission.Granted -> launchPicker(mimes, multiple)
                else -> Later(PickerResponse.Denied)
            }
        }
    }
}