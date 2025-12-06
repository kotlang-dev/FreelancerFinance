package org.kotlang.freelancerfinance.data.repository

import android.content.Context
import org.kotlang.freelancerfinance.domain.repository.FileSystemHelper
import java.io.File

class AndroidFileSystemHelper(private val context: Context) : FileSystemHelper {

    override fun getPrivateStoragePath(): String {
        return context.filesDir.absolutePath
    }

    override fun saveFile(fileName: String, bytes: ByteArray): String {
        val file = File(context.filesDir, fileName)
        file.writeBytes(bytes)
        return file.absolutePath
    }
}