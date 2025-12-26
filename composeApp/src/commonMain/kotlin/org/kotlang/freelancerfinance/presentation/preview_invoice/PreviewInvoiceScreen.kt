package org.kotlang.freelancerfinance.presentation.preview_invoice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.presentation.design_system.bar.FinanceTopBar
import org.kotlang.freelancerfinance.presentation.util.ObserveAsEvents

@Composable
fun PreviewInvoiceScreenRoot(
    onNavigateBack: () -> Unit,
    viewModel: PreviewInvoiceViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { event ->
        when (event) {
            is PreviewInvoiceEvent.NavigateBack -> onNavigateBack()
            is PreviewInvoiceEvent.SharePdf -> {}
        }
    }

    PreviewInvoiceScreen(
        state = state,
        onBackClick = { viewModel.onAction(PreviewInvoiceUiAction.OnBackClick) },
        onShareClick = { viewModel.onAction(PreviewInvoiceUiAction.OnShareClick) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewInvoiceScreen(
    state: PreviewInvoiceUiState,
    onBackClick: () -> Unit,
    onShareClick: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            FinanceTopBar(
                title = "Invoice Preview",
                onNavigateBack = onBackClick
            )
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else if (state.pdfPath != null) {
                PdfViewer(
                    pdfPath = state.pdfPath,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}