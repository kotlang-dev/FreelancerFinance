package org.kotlang.freelancerfinance.data.local

import java.io.File

fun getDesktopFile(fileName: String): File {
    val userHome = System.getProperty("user.home")

    val appDataDir = when {
        System.getProperty("os.name").startsWith("Windows") -> {
            File(System.getenv("APPDATA"), "FreelancerFinance")
        }
        System.getProperty("os.name") == "Mac OS X" -> {
            File(userHome, "Library/Application Support/FreelancerFinance")
        }
        else -> {
            File(userHome, ".FreelancerFinance")
        }
    }

    if (!appDataDir.exists()) {
        appDataDir.mkdirs()
    }

    return File(appDataDir, fileName)
}