package org.kotlang.freelancerfinance

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.presentation.client_list.ClientListScreen
import org.kotlang.freelancerfinance.presentation.invoice.CreateInvoiceScreen
import org.kotlang.freelancerfinance.presentation.invoice.InvoiceViewModel
import org.kotlang.freelancerfinance.presentation.profile.ProfileScreen

@Composable
fun App() {
    MaterialTheme {
        val viewModel = koinViewModel<InvoiceViewModel>()
        CreateInvoiceScreen(viewModel) {}
    }
}