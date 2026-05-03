package com.example.deals.presentation.dealsguest

import androidx.compose.runtime.Composable

@Composable
fun DealsGuestRoute(onNavigateToAuth: () -> Unit) {
    DealsGuestScreen(onLoginClick = onNavigateToAuth)
}
