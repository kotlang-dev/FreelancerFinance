package org.kotlang.freelancerfinance.presentation.invoice.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_person
import org.jetbrains.compose.resources.painterResource
import org.kotlang.freelancerfinance.domain.model.Client

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientSelectionSheet(
    clients: List<Client>,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onClientSelected: (Client) -> Unit,
    onAddNewClient: () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        content = {
            ClientSheetContent(
                clients = clients,
                onClientSelected = onClientSelected,
                onAddNewClient = onAddNewClient
            )
        }
    )

}

@Composable
private fun ClientSheetContent(
    clients: List<Client>,
    modifier: Modifier = Modifier,
    onClientSelected: (Client) -> Unit,
    onAddNewClient: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Select Client",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(clients) { client ->
                ListItem(
                    modifier = Modifier.clickable { onClientSelected(client) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(Res.drawable.ic_person),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    },
                    headlineContent = { Text(client.name) },
                    supportingContent = {
                        if (client.gstin != null) {
                            Text(
                                text = "GSTIN: ${client.gstin}",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                )
                HorizontalDivider()
            }
        }

        Button(
            onClick = onAddNewClient,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Add New Client")
        }
    }
}