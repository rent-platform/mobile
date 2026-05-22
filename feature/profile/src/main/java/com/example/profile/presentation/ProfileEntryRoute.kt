package com.example.profile.presentation

import androidx.compose.runtime.Composable
import com.example.profile.presentation.guestprofile.GuestProfileRoute
import com.example.profile.presentation.profile.ProfileItemsStatus
import com.example.profile.presentation.profile.ProfileRoute

@Composable
fun ProfileEntryRoute(
    isAuthorized: Boolean,
    onLoginClick: () -> Unit,
    onEditProfileClick: () -> Unit = {},
    onRatingClick: () -> Unit = {},
    onMyItemsClick: (ProfileItemsStatus) -> Unit = {},
    onMyRentalsClick: () -> Unit = {},
    onRentalHistoryClick: () -> Unit = {},
    onCreateItemClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    if (isAuthorized) {
        ProfileRoute(
            onEditProfileClick = onEditProfileClick,
            onRatingClick = onRatingClick,
            onMyItemsClick = onMyItemsClick,
            onMyRentalsClick = onMyRentalsClick,
            onRentalHistoryClick = onRentalHistoryClick,
            onCreateItemClick = onCreateItemClick,
            onSettingClick = onSettingClick,
            onLogoutClick = onLogoutClick
        )
    } else {
        GuestProfileRoute(
            onLoginClick = onLoginClick
        )
    }
}