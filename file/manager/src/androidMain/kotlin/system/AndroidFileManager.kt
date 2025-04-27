package system

import androidx.activity.ComponentActivity
import koncurrent.Executor
import koncurrent.Later
import koncurrent.TODOLater
import system.file.FilePickers
import system.internal.FileInfo
import system.internal.LocalFilePath
import system.internal.LocalFileUri
import java.io.ByteArrayOutputStream
import java.io.File

class AndroidFileManager(private val activity: ComponentActivity) : FileManager {
    override val pickers by lazy {
        FilePickers(
            documents = AndroidMultiFilePicker(activity),
            document = AndroidSingleFilePicker(activity),
            medias = AndroidMultiMediaPicker(activity),
            media = AndroidSingleMediaPicker(activity),
        )
    }

    fun register() {
        pickers.documents.register()
        pickers.document.register()
        pickers.medias.register()
        pickers.media.register()
    }

    override fun exists(file: LocalFile): Boolean {
        file as LocalFilePath
        return File(file.path).exists()
    }

    override fun info(file: LocalFile): FileInfo = FileInfo(activity, file)

    override fun open(file: LocalFile): Later<String> = TODOLater()

    override fun open(url: String): Later<String> = TODOLater()

    override fun save(file: LocalFile, name: String?): Later<String> = TODOLater()

    override fun read(file: LocalFile, executor: Executor): Later<ByteArray> = Later { resolve, reject ->
        try {
            when (file) {
                is LocalFilePath -> resolve(File(file.path).readBytes())
                is LocalFileUri -> {
                    val contentResolver = activity.contentResolver
                    val fis = contentResolver.openInputStream(file.uri) ?: return@Later reject(
                        IllegalArgumentException("Failed to open file: ${file.uri} for reading")
                    )
                    val baos = ByteArrayOutputStream()
                    fis.copyTo(baos)
                    fis.close()
                    baos.close()
                    resolve(baos.toByteArray())
                }

                else -> reject(IllegalArgumentException("LocalFile of type `${file::class.simpleName}` is not supported on Android"))
            }
            resolve(File((file as LocalFilePath).path).readBytes())
        } catch (err: Throwable) {
            reject(err)
        }
    }

    fun unregister() {
        pickers.documents.unregister()
        pickers.document.unregister()
        pickers.medias.unregister()
        pickers.media.unregister()
    }
}