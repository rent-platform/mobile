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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.deals.presentation.DealsEntryRoute
import com.example.favorites.presentation.FavoritesEntryRoute
import com.example.marketplace.presentation.rentrequest.RentRequestRoute
import com.example.marketplace.presentation.search.filters.SearchFiltersRoute
import com.example.marketplace.presentation.search.filters.SearchFiltersUiState
import com.example.marketplace.presentation.search.input.SearchInputRoute
import com.example.marketplace.presentation.search.results.SearchResultsRoute
import com.example.profile.presentation.changepassword.ChangePasswordRoute
import com.example.profile.presentation.editprofile.EditProfileRoute
import com.example.profile.presentation.profilesettings.ProfileSettingsRoute
import com.example.session.SessionManager
import org.koin.compose.koinInject

@Composable
fun AppNavHost() {
    val sessionManager: SessionManager = koinInject()

    val isAuthorized by sessionManager.isAuthorized.collectAsStateWithLifecycle(
        initialValue = false
    )
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
                },
                onEditProfileClick = {
                    rootNavController.navigate(EditProfileDestination)
                },
                onSettingClick = {rootNavController.navigate(ProfileSettingDestination)},
                onNavigateToDealDetails = { dealId ->
                    // rootNavController.navigate(DealDetailsDestination(dealId))
                },
                onNavigateToCreateListing = {
                    // rootNavController.navigate(CreateListingDestination)
                },
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
                    // share intent позже
                },
                onRentClick = { itemId ->
                    if (isAuthorized) {
                        rootNavController.navigate(RentRequestDestination(itemId))
                    } else {
                        rootNavController.navigate(AuthorizationDestination)
                    }
                },
                onSimilarItemClick = { similarItemId ->
                    rootNavController.navigate(ItemDetailsDestination(similarItemId))
                },
                onSimilarSeeMoreClick = { categoryId ->
                    // позже: rootNavController.navigate(SearchDestination(categoryId))
                },
                onOwnerClick = { ownerId ->
                    // позже: rootNavController.navigate(OwnerProfileDestination(ownerId))
                },

                onAskOwnerClick = { itemId, ownerId ->
                    if (isAuthorized) {
                        // позже: rootNavController.navigate(ChatDestination(itemId = itemId, ownerId = ownerId))
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
        composable<EditProfileDestination> {
            EditProfileRoute(
                onNavigateBack = {
                    rootNavController.popBackStack()
                }
            )
        }
        composable<ProfileSettingDestination> {
            ProfileSettingsRoute(
                onNavigateBack = {
                    rootNavController.popBackStack()
                },
                onChangePasswordClick = {
                    rootNavController.navigate(ChangePasswordDestination)
                },
                onProfileDeleted = {
                    rootNavController.navigate(MainShellDestination) {
                        popUpTo(rootNavController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<ChangePasswordDestination> {
            ChangePasswordRoute(
                onNavigateBack = {
                    rootNavController.popBackStack()
                },
                onPasswordChanged = {
                    rootNavController.popBackStack()
                }
            )
        }

        composable<RentRequestDestination> { backStackEntry ->
            val destination = backStackEntry.toRoute<RentRequestDestination>()

            RentRequestRoute(
                itemId = destination.itemId,
                onBackClick = {
                    rootNavController.popBackStack()
                },
                onSubmitRentRequest = { itemId, ownerId, startDate, endDate ->
                    // позже запрос в deal-service
                    rootNavController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun MainShell(
    isAuthorized: Boolean,
    onNavigateToItemDetails: (String) -> Unit,
    onOpenAuthFlow: () -> Unit,
    onEditProfileClick: () -> Unit,
    onSettingClick: () -> Unit,
    onNavigateToDealDetails: (String) -> Unit,
    onNavigateToCreateListing: () -> Unit,
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
                        onNavigateToSearch = {
                            navController.navigate(MarketplaceSearchInputDestination)
                        },
                        onNavigateToFilters = {},
                        onNavigateToNotifications = {},
                        onNavigateToItemDetails = { itemId ->
                            onNavigateToItemDetails(itemId)
                        }
                    )
                }

                composable<MarketplaceSearchInputDestination>{
                    SearchInputRoute(
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onNavigateToSearchResults = { query ->
                            navController.navigate(
                                MarketplaceSearchResultsDestination(query = query))
                        },
                    )
                }

                composable<MarketplaceSearchFiltersDestination> {
                    SearchFiltersRoute(
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onApplyFilters = { filters ->
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("search_filters", filters)

                            navController.popBackStack()
                        }
                    )
                }

                composable<MarketplaceSearchResultsDestination> { backStackEntry ->
                    val destination = backStackEntry.toRoute<MarketplaceSearchResultsDestination>()

                    val filters = backStackEntry
                        .savedStateHandle
                        .get<SearchFiltersUiState>("search_filters")

                    SearchResultsRoute(
                        query = destination.query,
                        //initialFilters = filters,
                        onNavigateToSearchInput = {
                            navController.navigate(MarketplaceSearchInputDestination){
                                launchSingleTop = true
                            }
                        },
                        onNavigateToFilters = {
                            navController.navigate(MarketplaceSearchFiltersDestination)
                        },
                        onNavigateToItemDetails = { itemId ->
                            onNavigateToItemDetails(itemId)
                        }
                    )
                }

                composable<FavoritesEntryDestination> {
                    FavoritesEntryRoute(
                        isAuthorized = isAuthorized,
                        onLoginClick = onOpenAuthFlow,
                        onItemClick = { itemId ->
                            onNavigateToItemDetails(itemId)
                        }
                    )
                }

                composable<ProfileEntryDestination> {
                    ProfileEntryRoute(
                        isAuthorized = isAuthorized,
                        onLoginClick = onOpenAuthFlow,
                        onEditProfileClick = onEditProfileClick,
                        onSettingClick = onSettingClick,

                        onRatingClick = {
                            //navController.navigate(ProfileReviewsDestination)
                        },

                        onMyItemsClick = { status ->
                            //navController.navigate(MyItemsDestination(status.backendValue))
                        },

                        onMyRentalsClick = {
                            //navController.navigate(MyRentalsDestination)
                        },

                        onRentalHistoryClick = {
                            //navController.navigate(RentalHistoryDestination)
                        },

                        onCreateItemClick = {
                            //navController.navigate(CreateItemDestination)
                        },

                        onLogoutClick = {
                            navController.navigate(ProfileEntryDestination) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = false
                                }
                                launchSingleTop = true
                                restoreState = false
                            }
                        }
                    )
                }
                composable<DealsEntryDestination>{
                    DealsEntryRoute(
                        isAuthorized = isAuthorized,
                        onLoginClick = onOpenAuthFlow,
                        onNavigateToDealDetails = onNavigateToDealDetails,
                        onNavigateToCreateListing = onNavigateToCreateListing
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
                },
                onFavoriteClick = {
                    navController.navigateToTopLevel(FavoritesEntryDestination)
                },
                onDealsClick = {
                    navController.navigateToTopLevel(DealsEntryDestination)
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