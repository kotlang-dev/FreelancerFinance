package org.kotlang.freelancerfinance.presentation.profile.component
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

actual class ImagePickerFactory {
    @Composable
    actual fun createPicker(onImagePicked: (ByteArray) -> Unit): ImagePicker {
        return remember {
            object : ImagePicker {
                override fun pickImage() {
                    val dialog = FileDialog(null as Frame?, "Select Logo", FileDialog.LOAD)
                    dialog.file = "*.jpg;*.png;*.jpeg"
                    dialog.isVisible = true

                    if (dialog.file != null) {
                        val file = File(dialog.directory, dialog.file)
                        onImagePicked(file.readBytes())
                    }
                }
            }
        }
    }
}