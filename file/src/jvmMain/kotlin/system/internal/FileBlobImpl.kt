@file:Suppress("NON_EXPORTABLE_TYPE")

package system.internal

import system.FileBlob
import system.FileNotFoundException
import koncurrent.Executor
import koncurrent.Later
import java.io.File

@PublishedApi
internal class FileBlobImpl private constructor(val file: File, override val path: String) : FileBlob {
    override val name: String = file.name

    override fun readBytes(executor: Executor) = Later(executor) { resolve, reject ->
        try {
            resolve(file.readBytes())
        } catch (err: Throwable) {
            reject(err)
        }
    }

    override fun toString() = "File(name=$name, path=$path)"

    companion object {
        fun of(path: String): FileBlobImpl {
            val file = File(path)
            if (!file.exists()) throw FileNotFoundException(path)
            return FileBlobImpl(file, file.absolutePath)
        }
    }
}