package com.example.rentplatform.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.auth.presentation.authorization.AuthorizationRoute
import com.example.auth.presentation.registration.RegistrationRoute
import com.example.marketplace.presentation.catalog.CatalogRoute

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination
        ?.hierarchy
        ?.any {
            it.hasRoute<CatalogDestination>() ||
                    it.hasRoute<AuthorizationDestination>() ||
                    it.hasRoute<RegistrationDestination>()
        } == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    currentDestination = currentDestination,
                    onCatalogClick = {
                        navController.navigate(CatalogDestination) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onProfileClick = {
                        navController.navigate(AuthorizationDestination) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CatalogDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<CatalogDestination> {
                CatalogRoute(
                    onNavigateToSearch = {},
                    onNavigateToFilters = {},
                    onNavigateToNotifications = {},
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
}