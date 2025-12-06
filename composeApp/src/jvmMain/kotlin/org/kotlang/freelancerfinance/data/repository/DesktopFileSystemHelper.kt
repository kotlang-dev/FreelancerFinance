package org.kotlang.freelancerfinance.data.repository

import org.kotlang.freelancerfinance.domain.repository.FileSystemHelper
import java.io.File

class DesktopFileSystemHelper : FileSystemHelper {

    override fun getPrivateStoragePath(): String {
        val userHome = System.getProperty("user.home")
        val folder = File(userHome, ".freelancerfinance")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return folder.absolutePath
    }

    override fun saveFile(fileName: String, bytes: ByteArray): String {
        val folder = File(getPrivateStoragePath())
        val file = File(folder, fileName)
        file.writeBytes(bytes)
        return file.absolutePath
    }
}