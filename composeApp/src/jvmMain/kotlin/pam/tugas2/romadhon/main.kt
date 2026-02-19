package pam.tugas2.romadhon

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Tugas2_123140031",
    ) {
        App()
    }
}