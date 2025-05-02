@file:OptIn(BetaInteropApi::class, ExperimentalEncodingApi::class)

package system.internal

import kotlinx.cinterop.BetaInteropApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSDataBase64DecodingIgnoreUnknownCharacters
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToURL
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerMode
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTTypeFolder
import platform.darwin.NSObject
import system.FileSaver
import system.SaveResult
import system.file.mime.Mime
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class OsxFileSaver : FileSaver {

    private var host: UIViewController? = null
    private val results = Channel<NSURL?>()

    fun initialize(host: UIViewController) {
        this.host = host
    }

    private val delegate = object : NSObject(), UIDocumentPickerDelegateProtocol {
        override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentAtURL: NSURL) {
            results.trySend(didPickDocumentAtURL)
        }

        override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentsAtURLs: List<*>) {
            results.trySend(didPickDocumentsAtURLs.firstNotNullOfOrNull { it as? NSURL })
        }

        override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
            results.trySend(null)
        }
    }


    private suspend fun directory(): NSURL? = withContext(Dispatchers.Main) {
        val picker = UIDocumentPickerViewController(documentTypes = listOf(UTTypeFolder.identifier), inMode = UIDocumentPickerMode.UIDocumentPickerModeOpen)

        picker.allowsMultipleSelection = false
        picker.delegate = delegate

        host?.presentViewController(picker, animated = true, null) ?: throw IllegalStateException(
            "OSXFilePicker has not been initialized with a non null host view controller"
        )

        val directory = results.receive()
        picker.dismissViewControllerAnimated(true, null)
        directory
    }

    override suspend fun save(content: ByteArray, name: String, type: Mime): SaveResult {
        val dir = directory() ?: return SaveResult.Cancelled

        val url = dir.URLByAppendingPathComponent(name) ?: return SaveResult.Failure(
            errors = listOf(IllegalStateException("Failed to create file URL"))
        )

        return withContext(Dispatchers.IO) {
            val data = NSData.create(
                base64EncodedString = Base64.encode(content),
                options = NSDataBase64DecodingIgnoreUnknownCharacters
            ) ?: return@withContext SaveResult.Failure(
                errors = listOf(IllegalStateException("Failed to create NSData from content"))
            )

            data.writeToURL(url = url, atomically = true)
            SaveResult.Success
        }
    }

//    override suspend fun save(content: String, name: String, type: Mime): SaveResult = withContext(Dispatchers.IO) {
//        val url = directory()?.URLByAppendingPathComponent(name) ?: return@withContext SaveResult.Failure(
//            errors = listOf(IllegalStateException("Failed to create file URL"))
//        )
//        val data = NSData.create(
//            base64EncodedString = Base64.encode(content.encodeToByteArray()),
//            options = NSDataBase64DecodingIgnoreUnknownCharacters
//        ) ?: return@withContext SaveResult.Failure(
//            errors = listOf(IllegalStateException("Failed to create NSData from content"))
//        )
//        data.writeToURL(url = url, atomically = true)
//        SaveResult.Success
//    }

    override suspend fun save(content: String, name: String, type: Mime): SaveResult = save(content.encodeToByteArray(), name, type)
}