package system

import koncurrent.Executor
import koncurrent.Later
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTType
import platform.UniformTypeIdentifiers.loadDataRepresentationForContentType
import system.file.DeniedMultiFilePicker
import system.file.DeniedSingleFilePicker
import system.file.FilePickers
import system.internal.LocalFileInfoProvider
import system.internal.LocalFileProvider

class IosFileManager : FileManager {

    override val pickers by lazy {
        FilePickers(
            documents = DeniedMultiFilePicker(),
            document = DeniedSingleFilePicker(),
            medias = OSXMultiMediaPicker(),
            media = OSXSingleMediaPicker()
        )
    }

    fun initialize(host: UIViewController?) {
        pickers.medias.initialize(host)
        pickers.media.initialize(host)
    }

    override fun exists(file: LocalFile): Boolean {
        TODO("Not yet implemented")
    }

    override fun info(file: LocalFile): FileInfo = LocalFileInfoProvider(file as LocalFileProvider)

    override fun open(file: LocalFile): Later<String> {
        TODO("Not yet implemented")
    }

    override fun open(url: String): Later<String> {
        TODO("Not yet implemented")
    }

    override fun save(file: LocalFile, name: String?): Later<String> {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun read(file: LocalFile, executor: Executor): Later<ByteArray> = Later { resolve, reject ->
        file as LocalFileProvider
        val identifier = file.provider.registeredTypeIdentifiers.firstOrNull() ?: return@Later reject(RuntimeException("No identifier found"))
        val type = UTType.typeWithIdentifier(identifier as String) ?: return@Later reject(RuntimeException("No type found"))
        file.provider.loadDataRepresentationForContentType(type) { data, error ->
            if (error != null) {
                reject(RuntimeException(error.localizedDescription))
            } else {
                val len = data?.length()?.toInt() ?: 0
                resolve(data?.bytes()?.readBytes(len) ?: ByteArray(0))
            }
        }
    }
}