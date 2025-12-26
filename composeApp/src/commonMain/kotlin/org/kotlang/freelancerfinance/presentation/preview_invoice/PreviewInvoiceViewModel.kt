package org.kotlang.freelancerfinance.presentation.preview_invoice// ui/invoice/preview/PdfPreviewViewModel.kt

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kotlang.freelancerfinance.domain.model.BusinessProfile
import org.kotlang.freelancerfinance.domain.model.IndianState
import org.kotlang.freelancerfinance.domain.repository.InvoiceRepository
import org.kotlang.freelancerfinance.domain.repository.PdfGenerator
import org.kotlang.freelancerfinance.presentation.navigation.Route

class PreviewInvoiceViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: InvoiceRepository,
    private val pdfGenerator: PdfGenerator
) : ViewModel() {

    private val invoiceId = savedStateHandle.toRoute<Route.PreviewInvoice>().invoiceId

    private val _uiState = MutableStateFlow(PreviewInvoiceUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<PreviewInvoiceEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadPdf()
    }

    fun onAction(action: PreviewInvoiceUiAction) {
        when (action) {
            PreviewInvoiceUiAction.OnRetryClick -> loadPdf()
            PreviewInvoiceUiAction.OnBackClick -> sendEvent(PreviewInvoiceEvent.NavigateBack)
            PreviewInvoiceUiAction.OnShareClick -> {
                val path = _uiState.value.pdfPath
                if (path != null) {
                    sendEvent(PreviewInvoiceEvent.SharePdf(path))
                }
            }
            PreviewInvoiceUiAction.OnEditClick -> {}
        }
    }

    private fun loadPdf() {
        viewModelScope.launch {
            // Reset to loading, clear errors
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // 1. Fetch Invoice
                val invoice = repository.getInvoiceById(invoiceId).first()

                if (invoice == null) {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Invoice not found")
                    }
                    return@launch
                }

                val profile = BusinessProfile(
                    businessName = invoice.businessProfile.name,
                    addressLine1 = invoice.businessProfile.address,
                    state = IndianState.DELHI,
                    pincode = "202001",
                    gstin = null,
                    panNumber = "ABC",
                    addressLine2 = ""
                )

                // 2. Generate PDF
                val path = pdfGenerator.generateInvoicePdf(invoice, profile)

                // 3. Update Success State
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        pdfPath = path,
                        invoiceNumber = invoice.invoiceNumber,
                        error = null
                    )
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(isLoading = false, error = "Failed to load PDF: ${e.message}")
                }
            }
        }
    }

    private fun sendEvent(event: PreviewInvoiceEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}