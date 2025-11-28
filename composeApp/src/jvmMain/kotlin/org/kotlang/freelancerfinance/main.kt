package org.kotlang.freelancerfinance

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.kotlang.freelancerfinance.di.initKoin

fun main() {
    System.setProperty("skiko.renderApi", "OPENGL") //TODO read about this
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "FreelancerFinance",
        ) {
            App()
        }
    }
}