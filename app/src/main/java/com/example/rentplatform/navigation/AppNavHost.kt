package com.example.rentplatform.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auth.presentation.authorization.AuthorizationRoute
import com.example.auth.presentation.registration.RegistrationRoute
import com.example.marketplace.presentation.catalog.CatalogRoute

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = CatalogDestination
    ) {
        composable<CatalogDestination> {
            CatalogRoute(
                onNavigateToSearch = {
                },
                onNavigateToFilters = {
                },
                onNavigateToNotifications = {
                },
                onNavigateToItemDetails = {}
            )
        }

        composable<AuthorizationDestination> {
            AuthorizationRoute(
                onNavigateToRegistration = {
                    navController.navigate(RegistrationDestination)
                },
                onAuthSuccess = {
                    navController.popBackStack(
                        route = AuthorizationDestination,
                        inclusive = true
                    )
                }
            )
        }

        composable<RegistrationDestination> {
            RegistrationRoute(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAuthSuccess = {
                    navController.popBackStack(
                        route = AuthorizationDestination,
                        inclusive = true
                    )
                }
            )
        }
    }
}