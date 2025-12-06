package org.kotlang.freelancerfinance.presentation.profile.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

actual class ImagePickerFactory {
    @Composable
    actual fun createPicker(onImagePicked: (ByteArray) -> Unit): ImagePicker {
        val context = LocalContext.current

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                context.contentResolver.openInputStream(it)?.use { stream ->
                    onImagePicked(stream.readBytes())
                }
            }
        }

        return remember {
            object : ImagePicker {
                override fun pickImage() {
                    launcher.launch("image/*")
                }
            }
        }
    }
}