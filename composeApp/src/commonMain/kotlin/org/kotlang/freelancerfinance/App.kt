package org.kotlang.freelancerfinance

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.kotlang.freelancerfinance.presentation.client_list.ClientListScreen
import org.kotlang.freelancerfinance.presentation.profile.ProfileScreen

@Composable
fun App() {
    MaterialTheme {
        ClientListScreen()
    }
}