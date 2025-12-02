package org.kotlang.freelancerfinance.data.repository

import org.kotlang.freelancerfinance.domain.repository.FileOpener
import java.awt.Desktop
import java.io.File

class DesktopFileOpener : FileOpener {
    override fun openFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            try {
                // Try to open the file directly
                Desktop.getDesktop().open(file)
            } catch (e: Exception) {
                // Fallback: If opening fails, try to open the folder containing it
                try {
                    Desktop.getDesktop().open(file.parentFile)
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }
            }
        }
    }
}