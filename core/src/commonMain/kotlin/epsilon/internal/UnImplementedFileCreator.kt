package epsilon.internal

import epsilon.FileCreator
import epsilon.RawFile

internal class UnImplementedFileCreator : FileCreator {
    override fun binary(content: ByteArray, name: String, type: String): RawFile {
        TODO("Not yet implemented")
    }

    override fun text(content: String, name: String, type: String): RawFile {
        TODO("Not yet implemented")
    }
}