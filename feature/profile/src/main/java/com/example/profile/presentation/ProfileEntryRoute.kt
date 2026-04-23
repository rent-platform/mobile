package com.example.profile.presentation

import androidx.compose.runtime.Composable
import com.example.profile.presentation.guestprofile.GuestProfileRoute

@Composable
fun ProfileEntryRoute(
    isAuthorized: Boolean,
    onLoginClick: () -> Unit
) {
    if (isAuthorized) {
  //      ProfileRoute()
    } else {
        GuestProfileRoute(
            onLoginClick = onLoginClick
        )
    }
}