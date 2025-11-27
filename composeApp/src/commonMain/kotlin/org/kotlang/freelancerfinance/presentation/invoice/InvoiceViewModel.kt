package org.kotlang.freelancerfinance.presentation.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kotlang.freelancerfinance.domain.logic.TaxCalculator
import org.kotlang.freelancerfinance.domain.model.Invoice
import org.kotlang.freelancerfinance.domain.model.InvoiceLineItem
import org.kotlang.freelancerfinance.domain.model.InvoiceStatus
import org.kotlang.freelancerfinance.domain.repository.ClientRepository
import org.kotlang.freelancerfinance.domain.repository.InvoiceRepository

class InvoiceViewModel(
    private val invoiceRepository: InvoiceRepository,
    private val clientRepository: ClientRepository
) : ViewModel() {

    // 1. Main List State
    val uiState: StateFlow<InvoiceUiState> = combine(
        invoiceRepository.getAllInvoices(),
        clientRepository.getAllClients()
    ) { invoices, clients ->
        InvoiceUiState(invoices = invoices, clients = clients, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InvoiceUiState()
    )

    // 2. Draft Form State
    private val _draftState = MutableStateFlow(DraftInvoiceState())
    val draftState: StateFlow<DraftInvoiceState> = _draftState

    fun onAction(action: InvoiceUiAction) {
        when (action) {
            is InvoiceUiAction.SelectClient -> {
                _draftState.update { it.copy(selectedClient = action.client) }
                recalculateTotals()
            }
            is InvoiceUiAction.AddItem -> {
                // Parse inputs
                val qty = action.qty.toDoubleOrNull() ?: 0.0
                val price = action.price.toDoubleOrNull() ?: 0.0
                val tax = action.taxRate.toDoubleOrNull() ?: 18.0 // Default 18%
                
                val newItem = InvoiceLineItem(
                    id = System.currentTimeMillis(), // Temp ID
                    description = action.desc,
                    quantity = qty,
                    unitPrice = price,
                    taxRate = tax
                )
                
                _draftState.update { it.copy(items = it.items + newItem) }
                recalculateTotals()
            }
            is InvoiceUiAction.RemoveItem -> {
                _draftState.update { it.copy(items = it.items - action.item) }
                recalculateTotals()
            }
            InvoiceUiAction.SaveInvoice -> saveInvoice()
            InvoiceUiAction.ResetDraft -> _draftState.value = DraftInvoiceState()
        }
    }

    private fun recalculateTotals() {
        val current = _draftState.value
        val client = current.selectedClient ?: return

        // 1. Calculate raw subtotal
        val subTotal = current.items.sumOf { it.quantity * it.unitPrice }
        
        // 2. Calculate Tax using our Domain Logic (IGST vs CGST)
        // Note: For this MVP, we are summing tax per item. 
        // In a complex app, you'd apply the TaxCalculator logic to the whole invoice.
        var totalTax = 0.0
        
        // We use the TaxCalculator purely to see "How much tax" for each item
        current.items.forEach { item ->
            // Assuming "User State" is Maharashtra (Hardcoded for MVP, ideally passed in)
            val result = TaxCalculator.calculate(
                amount = item.quantity * item.unitPrice,
                gstRatePercent = item.taxRate,
                supplierState = client.state, // Ideally this is YOU (User Profile)
                placeOfSupply = client.state // This is CLIENT
            )
            // Fix Logic: We need User Profile State here. 
            // For now, let's just sum the tax amount simply:
            totalTax += (item.quantity * item.unitPrice) * (item.taxRate / 100.0)
        }

        _draftState.update { 
            it.copy(
                subTotal = subTotal,
                totalTax = totalTax,
                grandTotal = subTotal + totalTax
            ) 
        }
    }

    private fun saveInvoice() {
        val draft = _draftState.value
        if (draft.selectedClient == null || draft.items.isEmpty()) return

        val newInvoice = Invoice(
            invoiceNumber = draft.invoiceNumber,
            client = draft.selectedClient!!,
            date = System.currentTimeMillis(),
            items = draft.items,
            status = InvoiceStatus.DRAFT,
            subTotal = draft.subTotal,
            taxAmount = draft.totalTax,
            totalAmount = draft.grandTotal
        )

        viewModelScope.launch {
            invoiceRepository.createInvoice(newInvoice)
            onAction(InvoiceUiAction.ResetDraft)
        }
    }
}