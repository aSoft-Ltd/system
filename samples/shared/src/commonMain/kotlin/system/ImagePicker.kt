package system

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import koncurrent.later.catch
import koncurrent.later.then

@Composable
internal fun ImagePicker(
    files: FileManager
) {
    Column {
        val picked = remember { mutableStateListOf<LocalFile>() }
        val denied = remember { mutableStateOf(false) }
        Button(
            onClick = {
                files.pickers.media.openPicker().then {
                    when (it) {
                        is PickerResponse.Cancelled -> {}
                        is PickerResponse.Denied -> denied.value = true
                        is PickerResponse.Picked -> picked += it
                    }
                }
            }
        ) {
            Text("Pick Image")
        }

        Column {
            for (file in picked) PickedImage(files, file)
        }
    }
}

@Composable
internal fun PickedImage(
    files: FileManager,
    file: LocalFile
) {
    var image by remember { mutableStateOf<ImageBitmap?>(null) }
    val info = remember { files.info(file) }
    val name = info.name()
    println("Showing $name")
    LaunchedEffect(file) {
        println("Reading $name")
        files.read(file).then {
            println("Read $name")
            image = it.toImageBitmap()
            println("Assigned $image to $name")
        }.catch {
            println("Failed to read $name: $it")
        }
    }
    when (val i = image) {
        null -> Text("Loading $name...")
        else -> Image(i, contentDescription = info.name())
    }
}