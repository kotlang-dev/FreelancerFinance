package org.kotlang.freelancerfinance

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.kotlang.freelancerfinance.presentation.dashboard.DashboardScreenRoot
import org.kotlang.freelancerfinance.presentation.invoice.CreateInvoiceScreen
import org.kotlang.freelancerfinance.presentation.invoice.InvoiceViewModel
import org.kotlang.freelancerfinance.presentation.manage_client.ManageClientScreenRoot
import org.kotlang.freelancerfinance.presentation.navigation.Route
import org.kotlang.freelancerfinance.presentation.profile.ProfileScreenRoot
import org.kotlang.freelancerfinance.presentation.theme.FinanceAppTheme

@Composable
fun App() {
    FinanceAppTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->

            val navController = rememberNavController()

            NavHost(
                modifier = Modifier
                    .consumeWindowInsets(WindowInsets.systemBars)
                    .padding(innerPadding),
                navController = navController,
                startDestination = Route.Dashboard
            ) {

                composable<Route.Dashboard> {
                    DashboardScreenRoot(
                        onEditProfile = { navController.navigate(Route.Profile) },
                        onManageClients = { navController.navigate(Route.ClientList) },
                        onCreateInvoice = { navController.navigate(Route.CreateInvoice) }
                    )
                }

                composable<Route.Profile> {
                    ProfileScreenRoot(
                        snackbarHostState = snackbarHostState,
                        onNavigateBack = { navController.navigateUp() }
                    )
                }

                composable<Route.ClientList> {
                    ManageClientScreenRoot (
                        onNavigateBack = { navController.navigateUp() }
                    )
                }

                // 3. Create Invoice
                composable<Route.CreateInvoice> {
                    val viewModel = koinViewModel<InvoiceViewModel>()
                    CreateInvoiceScreen(
                        viewModel = viewModel,
                        onFinished = {
                            navController.popBackStack(Route.Dashboard, inclusive = false)
                        },
                        onNavigateBack = { navController.navigateUp() },
                        onAddNewClient = { navController.navigate(Route.ClientList) }
                    )
                }
            }
        }
    }
}