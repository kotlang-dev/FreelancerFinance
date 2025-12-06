package org.kotlang.freelancerfinance.domain.repository

interface FileSystemHelper {
    fun getPrivateStoragePath(): String
    fun saveFile(fileName: String, bytes: ByteArray): String
}