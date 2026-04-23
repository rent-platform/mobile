package com.example.rentplatform.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.example.auth.presentation.authorization.AuthorizationRoute
import com.example.auth.presentation.registration.RegistrationRoute
import com.example.marketplace.presentation.catalog.CatalogRoute
import com.example.marketplace.presentation.itemdetails.ItemDetailsRoute
import com.example.profile.presentation.ProfileEntryRoute

@Composable
fun AppNavHost(isAuthorized: Boolean = false) {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = MainShellDestination
    ) {
        composable<MainShellDestination> {
            MainShell(
                isAuthorized = isAuthorized,
                onNavigateToItemDetails = { itemId ->
                    rootNavController.navigate(ItemDetailsDestination(itemId))
                },
                onOpenAuthFlow = {
                    rootNavController.navigate(AuthorizationDestination)
                }
            )
        }

        composable<ItemDetailsDestination> { backStackEntry ->
            val destination = backStackEntry.toRoute<ItemDetailsDestination>()

            ItemDetailsRoute(
                itemId = destination.itemId,
                onBackClick = {
                    rootNavController.popBackStack()
                },
                onShareClick = { itemTitle ->
                    // share intent
                },
                onRentClick = { itemTitle ->
                    if (isAuthorized) {
                        // переход на экран бронирования
                    } else {
                        rootNavController.navigate(AuthorizationDestination)
                    }
                }
            )
        }

        composable<AuthorizationDestination> {
            AuthorizationRoute(
                onNavigateToRegistration = {
                    rootNavController.navigate(RegistrationDestination)
                },
                onAuthSuccess = {
                    rootNavController.popBackStack()
                }
            )
        }

        composable<RegistrationDestination> {
            RegistrationRoute(
                onNavigateBack = {
                    rootNavController.popBackStack()
                },
                onAuthSuccess = {
                    //Возврат пользователя до auth
                    rootNavController.popBackStack<AuthorizationDestination>(inclusive = true)
                }
            )
        }
    }
}

@Composable
private fun MainShell(
    isAuthorized: Boolean,
    onNavigateToItemDetails: (String) -> Unit,
    onOpenAuthFlow: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = CatalogDestination
            ) {
                composable<CatalogDestination> {
                    CatalogRoute(
                        onNavigateToSearch = {},
                        onNavigateToFilters = {},
                        onNavigateToNotifications = {},
                        onNavigateToItemDetails = { itemId ->
                            onNavigateToItemDetails(itemId)
                        }
                    )
                }

                composable<ProfileEntryDestination> {
                    ProfileEntryRoute(
                        isAuthorized = isAuthorized,
                        onLoginClick = onOpenAuthFlow
                    )
                }
            }

            AppBottomBar(
                currentDestination = currentDestination,
                onCatalogClick = {
                    navController.navigateToTopLevel(CatalogDestination)
                },
                onProfileClick = {
                    navController.navigateToTopLevel(ProfileEntryDestination)
                }
            )
        }
    }
}

private fun NavHostController.navigateToTopLevel(route: Any) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}