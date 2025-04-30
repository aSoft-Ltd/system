package system

import kotlinx.coroutines.channels.Channel
import platform.Foundation.NSItemProvider
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import system.file.PickerLimit
import system.file.PickingException
import system.file.fits
import system.file.mime.MediaMime
import system.file.mime.Mime
import system.file.picker.response.Denied
import system.file.picker.response.MultiPickerResponse
import system.file.toResponse
import system.internal.LocalFileInfoProvider
import system.internal.LocalFileProvider

abstract class OSXMediaPicker {

    protected val permission by lazy { OSXImagePickerPermissionManager() }

    private var host: UIViewController? = null

    private val results = Channel<List<NSItemProvider>>()

    fun initialize(host: UIViewController?) {
        this.host = host
    }

    private val delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
        override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
            results.trySend(didFinishPicking.mapNotNull { (it as? PHPickerResult)?.itemProvider })
        }
    }

    private suspend fun launch(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse {
        val config = PHPickerConfiguration()
        config.filter = PHPickerFilter.imagesFilter
        config.selectionLimit = limit.count.toLong()

        val chooser = PHPickerViewController(configuration = config)
        chooser.delegate = delegate

        host?.presentViewController(chooser, animated = true, completion = null) ?: throw IllegalStateException(
            "OSXMultiMediaPicker has not been initialized with a non null host view controller"
        )

        val providers = results.receive()

        chooser.dismissViewControllerAnimated(true, completion = null)

        val files = providers.map { LocalFileProvider(it) }

        val errors = buildList {
            if (files.size > limit.count) {
                add(PickingException.CountLimitExceeded(files.size, limit.count))
            }
            for (file in files.map { LocalFileInfoProvider(it) }) {
                addAll(file.fits(mimes, limit.size))
            }
        }

        return files.toResponse(errors)
    }

    protected suspend fun show(mimes: List<MediaMime>, limit: PickerLimit): MultiPickerResponse {
        if (permission.check(mimes) == Permission.Granted) return launch(mimes, limit)
        return when (permission.request(mimes)) {
            Permission.Granted -> launch(mimes, limit)
            else -> Denied
        }
    }
}