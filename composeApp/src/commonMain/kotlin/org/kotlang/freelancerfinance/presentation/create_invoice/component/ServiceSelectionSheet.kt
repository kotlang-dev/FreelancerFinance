package org.kotlang.freelancerfinance.presentation.create_invoice.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_edit
import freelancerfinance.composeapp.generated.resources.ic_person
import org.jetbrains.compose.resources.painterResource
import org.kotlang.freelancerfinance.domain.model.ServiceItem
import org.kotlang.freelancerfinance.presentation.design_system.layout.InitialsAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceSelectionSheet(
    services: List<ServiceItem>,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onServiceSelected: (ServiceItem) -> Unit,
    onEditServiceClick: (Long) -> Unit,
    onAddNewServiceClick: () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        content = {
            ServiceSheetContent(
                services = services,
                onServiceSelected = onServiceSelected,
                onEditServiceClick = onEditServiceClick,
                onAddServiceClick = onAddNewServiceClick
            )
        }
    )
}

@Composable
private fun ServiceSheetContent(
    services: List<ServiceItem>,
    modifier: Modifier = Modifier,
    onServiceSelected: (ServiceItem) -> Unit,
    onEditServiceClick: (Long) -> Unit,
    onAddServiceClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "Select Service",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(services) { service ->
                ServiceItemCard(
                    service = service,
                    onCardClick = { onServiceSelected(service) },
                    onEditClick = { onEditServiceClick(service.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddServiceClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_person),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create New Service")
        }
    }
}

@Composable
private fun ServiceItemCard(
    service: ServiceItem,
    onCardClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InitialsAvatar(
                name = service.name,
                initials = service.initials,
                avatarSizeDp = 40
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${service.formattedPrice}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = " • ",
                        color = MaterialTheme.colorScheme.outline
                    )

                    Text(
                        text = service.taxDisplay,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onEditClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_edit),
                    contentDescription = "Edit Service",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}