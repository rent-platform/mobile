package com.example.rentplatform.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
fun AppBottomBar(
    currentDestination: NavDestination?,
    onCatalogClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentDestination
                ?.hierarchy
                ?.any { it.hasRoute<CatalogDestination>() } == true,
            onClick = onCatalogClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = "Каталог"
                )
            },
            label = { Text("Каталог") }
        )

        NavigationBarItem(
            selected = currentDestination
                ?.hierarchy
                ?.any {
                    it.hasRoute<AuthorizationDestination>() ||
                            it.hasRoute<RegistrationDestination>()
                } == true,
            onClick = onProfileClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Профиль"
                )
            },
            label = { Text("Профиль") }
        )
    }
}
