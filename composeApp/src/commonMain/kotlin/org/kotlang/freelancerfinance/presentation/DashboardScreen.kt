package org.kotlang.freelancerfinance.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import freelancerfinance.composeapp.generated.resources.Res
import freelancerfinance.composeapp.generated.resources.ic_description
import freelancerfinance.composeapp.generated.resources.ic_person
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToClients: () -> Unit,
    onNavigateToInvoice: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(text = "Dashboard") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        DashboardCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            title = "Manage Profile",
            iconRes = Res.drawable.ic_person,
            onClick = onNavigateToProfile
        )

        Spacer(modifier = Modifier.height(16.dp))

        DashboardCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            title = "Manage Clients",
            iconRes = Res.drawable.ic_person,
            onClick = onNavigateToClients
        )

        Spacer(modifier = Modifier.height(16.dp))

        DashboardCard(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            title = "Create Invoice",
            iconRes = Res.drawable.ic_description,
            onClick = onNavigateToInvoice
        )
    }
}

@Composable
fun DashboardCard(
    title: String,
    iconRes: DrawableResource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.height(120.dp).clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(title)
        }
    }
}