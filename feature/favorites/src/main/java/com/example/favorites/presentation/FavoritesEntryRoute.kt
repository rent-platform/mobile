package com.example.favorites.presentation

import androidx.compose.runtime.Composable
import com.example.favorites.presentation.favorites.FavoritesRoute
import com.example.favorites.presentation.guest.GuestFavoritesRoute

@Composable
fun FavoritesEntryRoute(
    isAuthorized: Boolean,
    onLoginClick: () -> Unit,
    onItemClick: (String) -> Unit,
) {
    if (isAuthorized) {
        FavoritesRoute(
            onItemClick = onItemClick
        )
    } else {
        GuestFavoritesRoute(
            onLoginClick = onLoginClick
        )
    }
}