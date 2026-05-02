package com.example.favorites.presentation.guest

import androidx.compose.runtime.Composable

@Composable
fun GuestFavoritesRoute(
    onLoginClick: () -> Unit
) {
    GuestFavoritesScreen(
        onLoginClick = onLoginClick
    )
}