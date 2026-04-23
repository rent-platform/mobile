package com.example.profile.presentation.guestprofile

import androidx.compose.runtime.Composable

@Composable
fun GuestProfileRoute(
    onLoginClick: () -> Unit,
) {
    GuestProfileScreen(
        onLoginClick = onLoginClick
    )
}