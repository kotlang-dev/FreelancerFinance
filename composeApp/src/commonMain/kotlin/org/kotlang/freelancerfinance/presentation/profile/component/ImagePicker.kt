package org.kotlang.freelancerfinance.presentation.profile.component

import androidx.compose.runtime.Composable

interface ImagePicker {
    fun pickImage()
}

expect class ImagePickerFactory() {
    @Composable
    fun createPicker(onImagePicked: (ByteArray) -> Unit): ImagePicker
}

