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
import org.kotlang.freelancerfinance.presentation.add_edit_client.AddEditClientRoot
import org.kotlang.freelancerfinance.presentation.add_edit_service.AddEditServiceRoot
import org.kotlang.freelancerfinance.presentation.dashboard.DashboardScreenRoot
import org.kotlang.freelancerfinance.presentation.create_invoice.CreateInvoiceScreenRoot
import org.kotlang.freelancerfinance.presentation.manage_client.ManageClientScreenRoot
import org.kotlang.freelancerfinance.presentation.manage_services.ManageServicesScreenRoot
import org.kotlang.freelancerfinance.presentation.navigation.Route
import org.kotlang.freelancerfinance.presentation.preview_invoice.PreviewInvoiceScreenRoot
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
                        onManageClients = { navController.navigate(Route.ManageClients) },
                        onManageServices = { navController.navigate(Route.ManageServices) },
                        onCreateInvoice = { navController.navigate(Route.CreateInvoice) },
                        onInvoiceCardClick = { navController.navigate(Route.PreviewInvoice(it)) },
                        onViewAllClick = {}
                    )
                }

                composable<Route.Profile> {
                    ProfileScreenRoot(
                        snackbarHostState = snackbarHostState,
                        onNavigateBack = { navController.navigateUp() }
                    )
                }

                composable<Route.ManageClients> {
                    ManageClientScreenRoot(
                        onNavigateBack = { navController.navigateUp() },
                        onAddEditClient = { navController.navigate(Route.AddEditClient(it)) }
                    )
                }

                composable<Route.AddEditClient> {
                    AddEditClientRoot(
                        snackbarHostState = snackbarHostState,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable<Route.ManageServices> {
                    ManageServicesScreenRoot(
                        onNavigateBack = { navController.navigateUp() },
                        onAddEditService = { navController.navigate(Route.AddEditService(it)) }
                    )
                }

                composable<Route.AddEditService> {
                    AddEditServiceRoot(
                        snackbarHostState = snackbarHostState,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable<Route.CreateInvoice> {
                    CreateInvoiceScreenRoot(
                        snackbarHostState = snackbarHostState,
                        onNavigateBack = { navController.navigateUp() },
                        onNavigateToAddClient = { navController.navigate(Route.AddEditClient(null)) },
                        onNavigateToEditClient = { navController.navigate(Route.AddEditClient(it)) },
                        onNavigateToAddService = { navController.navigate(Route.AddEditService(null)) },
                        onNavigateToEditService = { navController.navigate(Route.AddEditService(it))}
                    )
                }

                composable<Route.PreviewInvoice> {
                    PreviewInvoiceScreenRoot(
                        onNavigateBack = { navController.navigateUp() }
                    )
                }
            }
        }
    }
}

//TODO think of using a single field for address in business profile.
//TODO Swap the text field in the profile screen, first gstin then prefilled pan
//TODO Make the state list sorted alphabetically
//TODO Invoice Data
// - Consecutive Serial Number: e.g., INV/24-25/001
// - Date of Issue:
// - HSN