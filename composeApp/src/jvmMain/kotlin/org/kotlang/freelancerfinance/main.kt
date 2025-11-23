package org.kotlang.freelancerfinance

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "FreelancerFinance",
    ) {
        App()
    }
}