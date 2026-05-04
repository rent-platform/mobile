package com.example.rentplatform.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.compose.material.icons.outlined.ReceiptLong

@Composable
fun AppBottomBar(
    currentDestination: NavDestination?,
    onCatalogClick: () -> Unit,
    onProfileClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onDealsClick: () -> Unit,
) {
    NavigationBar(modifier = Modifier.height(75.dp),

        ) {
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
                    it.hasRoute<FavoritesEntryDestination>()
                } == true,
            onClick = onFavoriteClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Избранное"
                )
            },
            label = { Text("Избранное") }
        )

        NavigationBarItem(
            selected = currentDestination
                ?.hierarchy
                ?.any { it.hasRoute<DealsEntryDestination>() } == true,
            onClick = onDealsClick,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ReceiptLong,
                    contentDescription = "Сделки"
                )
            },
            label = { Text("Сделки") }
        )

        NavigationBarItem(
            selected = currentDestination
                ?.hierarchy
                ?.any {
                    it.hasRoute<ProfileEntryDestination>()
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
