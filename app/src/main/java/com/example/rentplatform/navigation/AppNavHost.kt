package com.example.rentplatform.navigation


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.auth.presentation.authorization.AuthorizationRoute
import com.example.auth.presentation.registration.RegistrationRoute

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthorizationDestination
//        startDestination = CatalogDestination
    ) {
//        composable<CatalogDestination> {
//        }

        composable<AuthorizationDestination> {
            AuthorizationRoute(
                onNavigateToRegistration = {
                    navController.navigate(RegistrationDestination)
                },
                onNavigateToCatalog = {
//                    navController.popBackStack() !Подумать куда возврат сделаю!
                }
            )
        }

        composable<RegistrationDestination> {
            RegistrationRoute(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCatalog = {
//                    navController.navigate(CatalogDestination)
                }
            )
        }
    }
}