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
import org.kotlang.freelancerfinance.presentation.DashboardScreen
import org.kotlang.freelancerfinance.presentation.client_list.ClientListScreen
import org.kotlang.freelancerfinance.presentation.invoice.CreateInvoiceScreen
import org.kotlang.freelancerfinance.presentation.invoice.InvoiceViewModel
import org.kotlang.freelancerfinance.presentation.navigation.Route
import org.kotlang.freelancerfinance.presentation.profile.ProfileScreen
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

                // 1. Home Screen
                composable<Route.Dashboard> {
                    DashboardScreen(
                        onNavigateToProfile = { navController.navigate(Route.Profile) },
                        onNavigateToClients = { navController.navigate(Route.ClientList) },
                        onNavigateToInvoice = { navController.navigate(Route.CreateInvoice) }
                    )
                }

                composable<Route.Profile> {
                    ProfileScreen(
                        snackbarHostState = snackbarHostState,
                        onNavigateBack = { navController.navigateUp() }
                    )
                }

                composable<Route.ClientList> {
                    ClientListScreen(
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