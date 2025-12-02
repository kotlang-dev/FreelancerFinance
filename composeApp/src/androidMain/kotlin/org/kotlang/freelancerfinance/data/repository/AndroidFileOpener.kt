package org.kotlang.freelancerfinance.data.repository

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import org.kotlang.freelancerfinance.domain.repository.FileOpener
import java.io.File

class AndroidFileOpener(private val context: Context) : FileOpener {
    override fun openFile(path: String) {
        val file = File(path)
        if (!file.exists()) return

        try {
            // Generate a secure URI using the Authority defined in Manifest
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            // Create the Intent to View/Share
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            }

            // Launch the chooser
            val chooser = Intent.createChooser(intent, "Open Invoice with")
            chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(chooser)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}