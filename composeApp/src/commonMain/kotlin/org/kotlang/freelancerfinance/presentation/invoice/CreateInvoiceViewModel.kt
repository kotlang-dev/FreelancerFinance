package org.kotlang.freelancerfinance.presentation.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kotlang.freelancerfinance.domain.repository.ClientRepository
import org.kotlang.freelancerfinance.domain.repository.FileOpener
import org.kotlang.freelancerfinance.domain.repository.InvoiceRepository
import org.kotlang.freelancerfinance.domain.repository.PdfGenerator
import org.kotlang.freelancerfinance.domain.repository.ProfileRepository

class CreateInvoiceViewModel(
    private val invoiceRepository: InvoiceRepository,
    private val clientRepository: ClientRepository,
    private val profileRepository: ProfileRepository,
    private val pdfGenerator: PdfGenerator,
    private val fileOpener: FileOpener
) : ViewModel() {

    // 1. Main List State
    private val _state = MutableStateFlow(CreateInvoiceUiState())
    val state: StateFlow<CreateInvoiceUiState> = combine(
        _state,
        clientRepository.getAllClients()
    ) { state, clients ->
        state.copy(availableClients = clients)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CreateInvoiceUiState()
    )

    private val _isGeneratingPdf = MutableStateFlow(false)
    val isGeneratingPdf = _isGeneratingPdf.asStateFlow()

    private val _uiEvent = Channel<CreateInvoiceEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: CreateInvoiceUiAction) {
        when(action) {
            CreateInvoiceUiAction.OnAddClientClick -> {
                _state.update { it.copy(showClientSelectionSheet = true) }
            }
            is CreateInvoiceUiAction.OnEditClientClick -> {
                dismissClientSheet()
                sendEvent(CreateInvoiceEvent.NavigateToEditClient(action.clientId))
            }
            CreateInvoiceUiAction.OnDismissClientSheet -> dismissClientSheet()
            CreateInvoiceUiAction.OnAddLineItemClick -> TODO()
            is CreateInvoiceUiAction.OnClientSelected -> {
                _state.update {
                    it.copy(
                        selectedClient = action.client,
                        clientError = null,
                        showClientSelectionSheet = false
                    )
                }
            }
            is CreateInvoiceUiAction.OnDateSelected -> onDateSelected(action.dateMillis)
            CreateInvoiceUiAction.OnDismissServiceSheet -> TODO()
            is CreateInvoiceUiAction.OnDateFieldClick -> {
                _state.update { it.copy(activeDatePicker = action.type) }
            }
            is CreateInvoiceUiAction.OnInvoiceNumberChange -> TODO()
            is CreateInvoiceUiAction.OnQuantityChange -> TODO()
            is CreateInvoiceUiAction.OnRemoveLineItem -> TODO()
            CreateInvoiceUiAction.OnSaveAndPreviewClick -> TODO()
            is CreateInvoiceUiAction.OnServiceSelected -> TODO()
            CreateInvoiceUiAction.OnGoBackClick -> {
                sendEvent(CreateInvoiceEvent.NavigateBack)
            }
            CreateInvoiceUiAction.OnAddNewClientClick -> {
                dismissClientSheet()
                sendEvent(CreateInvoiceEvent.NavigateToAddClient)
            }
        }
    }

    private fun onDateSelected(dateMillis: Long?) {
        val selectedMillis = dateMillis ?: run {
            _state.update { it.copy(activeDatePicker = DatePickerType.None) }
            return
        }

        _state.update { currentState ->
            when (currentState.activeDatePicker) {
                DatePickerType.IssueDate -> currentState.copy(
                    issueDate = selectedMillis,
                    activeDatePicker = DatePickerType.None
                )
                DatePickerType.DueDate -> currentState.copy(
                    dueDate = selectedMillis,
                    activeDatePicker = DatePickerType.None
                )
                else -> currentState.copy(
                    activeDatePicker = DatePickerType.None
                )
            }
        }
    }

    private fun dismissClientSheet() {
        _state.update { it.copy(showClientSelectionSheet = false) }
    }

    /*
    private fun saveInvoice() {
        val draft = _draftState.value
        if (draft.selectedClient == null || draft.items.isEmpty()) return

        val newInvoice = Invoice(
            invoiceNumber = draft.invoiceNumber,
            client = draft.selectedClient,
            date = System.currentTimeMillis(),
            items = draft.items,
            status = InvoiceStatus.DRAFT,
            subTotal = draft.subTotal,
            taxAmount = draft.totalTax,
            totalAmount = draft.grandTotal
        )

        viewModelScope.launch {
            _isGeneratingPdf.value = true
            try {
                invoiceRepository.createInvoice(newInvoice)
                profileRepository.getProfile().collect { userProfile ->
                    if (userProfile != null) {
                        val path = pdfGenerator.generateInvoicePdf(newInvoice, userProfile)

                        withContext(Dispatchers.Main) {
                            fileOpener.openFile(path)
                        }
                    } else {
                        //_effectChannel.send(InvoiceUiEffect.ShowError("Cannot generate PDF. No Business Profile found."))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                //_effectChannel.send(InvoiceUiEffect.ShowError("Failed to save"))
            } finally {
                _isGeneratingPdf.value = false
            }

            delay(200)
            //_effectChannel.send(InvoiceUiEffect.NavigateBack)
        }
    }
    */

    private fun sendEvent(event: CreateInvoiceEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}