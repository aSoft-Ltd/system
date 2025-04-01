package system

import koncurrent.Later
import platform.Foundation.NSURL
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTType
import platform.darwin.NSObject
import system.file.FilePicker
import system.file.mime.Mime
import system.file.toResponse

class OSXFilePicker(private var controller: UIViewController?) : FilePicker {

    fun initialize(c: UIViewController) {
        controller = c
    }

    override fun openPicker(mimes: List<Mime>, multiple: Boolean): Later<PickerResponse> = Later { resolve, _ ->
        val types = when {
            mimes.isEmpty() -> listOf(UTType.typeWithMIMEType("*/*"))
            else -> mimes.mapNotNull { UTType.typeWithMIMEType(it.text) }
        }
        val picker = UIDocumentPickerViewController(forExportingURLs = types)
        picker.allowsMultipleSelection = multiple
        picker.delegate = object : NSObject(), UIDocumentPickerDelegateProtocol {
            override fun documentPicker(
                controller: UIDocumentPickerViewController,
                didPickDocumentAtURL: NSURL
            ) = documentPicker(controller, listOf(didPickDocumentAtURL))

            override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentsAtURLs: List<*>) {
                val results = didPickDocumentsAtURLs.mapNotNull { it as? NSURL }.mapNotNull { it.path }
                resolve(results.map { LocalFileImpl(it) }.toResponse(multiple))
                picker.dismissModalViewControllerAnimated(true)
            }

            override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
                resolve(PickerResponse.Cancelled)
                picker.dismissModalViewControllerAnimated(true)
            }
        }
        val c = controller ?: UIViewController()
        c.presentViewController(picker, animated = true, null)
    }

    fun deInitialize() {
        controller = null
    }
}