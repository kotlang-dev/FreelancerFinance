package org.kotlang.freelancerfinance.presentation.create_invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.kotlang.freelancerfinance.domain.model.BusinessSnapshot
import org.kotlang.freelancerfinance.domain.model.ClientSnapshot
import org.kotlang.freelancerfinance.domain.model.Invoice
import org.kotlang.freelancerfinance.domain.model.InvoiceLineItem
import org.kotlang.freelancerfinance.domain.model.InvoiceStatus
import org.kotlang.freelancerfinance.domain.repository.ClientRepository
import org.kotlang.freelancerfinance.domain.repository.FileOpener
import org.kotlang.freelancerfinance.domain.repository.InvoiceRepository
import org.kotlang.freelancerfinance.domain.repository.PdfGenerator
import org.kotlang.freelancerfinance.domain.repository.ProfileRepository
import org.kotlang.freelancerfinance.domain.repository.ServiceItemRepository
import java.util.UUID

class CreateInvoiceViewModel(
    clientRepository: ClientRepository,
    serviceItemRepository: ServiceItemRepository,
    private val invoiceRepository: InvoiceRepository,
    private val profileRepository: ProfileRepository,
    private val pdfGenerator: PdfGenerator,
    private val fileOpener: FileOpener
) : ViewModel() {

    private val _state = MutableStateFlow(CreateInvoiceUiState())
    val state: StateFlow<CreateInvoiceUiState> = combine(
        _state,
        clientRepository.getAllClients(),
        serviceItemRepository.getAllServiceItems()
    ) { state, clients, services ->
        state.copy(
            availableClients = clients,
            availableServices = services
        )
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
            is CreateInvoiceUiAction.OnDateFieldClick -> {
                _state.update { it.copy(activeDatePicker = action.type) }
            }
            is CreateInvoiceUiAction.OnInvoiceNumberChange -> {
                _state.update { it.copy(invoiceNumber = action.number) }
            }
            is CreateInvoiceUiAction.OnQuantityChange -> TODO()
            CreateInvoiceUiAction.OnSaveAndPreviewClick -> saveInvoice()
            CreateInvoiceUiAction.OnGoBackClick -> {
                sendEvent(CreateInvoiceEvent.NavigateBack)
            }
            CreateInvoiceUiAction.OnAddNewClientClick -> {
                dismissClientSheet()
                sendEvent(CreateInvoiceEvent.NavigateToAddClient)
            }
            CreateInvoiceUiAction.OnAddServiceItemClick -> {
                _state.update { it.copy(showServiceSelectionSheet = true) }
            }
            CreateInvoiceUiAction.OnDismissServiceSheet -> dismissServiceSheet()
            is CreateInvoiceUiAction.OnServiceSelected -> {
                val newItem = InvoiceLineItemUi(
                    internalId = UUID.randomUUID().toString(),
                    serviceId = action.service.id,
                    name = action.service.name,
                    unitPrice = action.service.defaultPrice,
                    taxRate = action.service.taxRate,
                    quantity = 1.0,
                    description = action.service.description
                )
                _state.update {
                    it.copy(
                        lineItems = it.lineItems + newItem,
                        showServiceSelectionSheet = false,
                        itemsError = null
                    )
                }
            }
            CreateInvoiceUiAction.OnAddNewServiceItemClick -> {
                dismissServiceSheet()
                sendEvent(CreateInvoiceEvent.NavigateToAddService)
            }
            is CreateInvoiceUiAction.OnEditServiceItemClick -> {
                dismissServiceSheet()
                sendEvent(CreateInvoiceEvent.NavigateToEditService(action.serviceId))
            }
            is CreateInvoiceUiAction.OnRemoveServiceItemClick -> {
                _state.update { currentState ->
                    val updatedList = currentState.lineItems.filterNot {
                        it.internalId == action.internalId
                    }
                    currentState.copy(lineItems = updatedList)
                }
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

    private fun dismissServiceSheet() {
        _state.update { it.copy(showServiceSelectionSheet = false) }
    }

    private fun saveInvoice() {
        val currentState = state.value
        val client = currentState.selectedClient ?: return

        if (!currentState.isSaveEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            try {
                val businessProfile = profileRepository.getProfile().first()

                val domainItems = currentState.lineItems.map { uiItem ->
                    InvoiceLineItem(
                        id = 0,
                        name = uiItem.name,
                        description = uiItem.description,
                        quantity = uiItem.quantity,
                        unitPrice = uiItem.unitPrice,
                        taxRate = uiItem.taxRate
                    )
                }

                val newInvoice = Invoice(
                    id = 0,
                    invoiceNumber = currentState.invoiceNumber,
                    issueDate = currentState.issueDate,
                    dueDate = currentState.dueDate,
                    status = InvoiceStatus.DRAFT,

                    client = ClientSnapshot(
                        originalClientId = client.id,
                        name = client.name,
                        address = client.address,
                        gstin = client.gstin,
                        state = client.state.stateName
                    ),

                    // Freeze Business Data from the repo fetch
                    businessProfile = BusinessSnapshot(
                        name = businessProfile?.businessName ?: "",
                        address = businessProfile?.addressLine1 ?: "",
                        gstin = businessProfile?.gstin,
                        logoUrl = businessProfile?.logoPath
                    ),

                    lineItems = domainItems,
                    totalAmount = currentState.grandTotal,
                    subTotal = currentState.subtotal,
                    taxAmount = currentState.totalTax
                )

                invoiceRepository.createInvoice(newInvoice)

                _uiEvent.send(CreateInvoiceEvent.ShowSnackbar("Invoice saved successfully"))

            } catch (e: Exception) {
                e.printStackTrace()
                _uiEvent.send(CreateInvoiceEvent.ShowSnackbar("Failed to save invoice: ${e.message}"))
            } finally {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }
    private fun sendEvent(event: CreateInvoiceEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}