package com.example.profile.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileRoute(
    onEditProfileClick: () -> Unit = {},
    onRatingClick: () -> Unit = {},

    onMyItemsClick: (ProfileItemsStatus) -> Unit = {},

    onMyRentalsClick: () -> Unit = {},
    onRentalHistoryClick: () -> Unit = {},

    onCreateItemClick: () -> Unit = {},

    onLogoutClick: () -> Unit = {}
) {
    val viewModel: ProfileViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.actions.collect { action ->
            when (action) {
                ProfileAction.NavigateToEditProfile -> {
                    onEditProfileClick()
                }

                ProfileAction.NavigateToRating -> {
                    onRatingClick()
                }

                is ProfileAction.NavigateToMyItems -> {
                    onMyItemsClick(action.status)
                }

                ProfileAction.NavigateToMyRentals -> {
                    onMyRentalsClick()
                }

                ProfileAction.NavigateToRentalHistory -> {
                    onRentalHistoryClick()
                }

                ProfileAction.NavigateToCreateItem -> {
                    onCreateItemClick()
                }

                ProfileAction.Logout -> {
                    onLogoutClick()
                }
            }
        }
    }

    ProfileScreen(
        uiState = uiState,

        onEditProfileClick = {
            viewModel.onEvent(ProfileEvent.EditProfileClicked)
        },

        onRatingClick = {
            viewModel.onEvent(ProfileEvent.RatingClicked)
        },

        onActiveClick = {
            viewModel.onEvent(ProfileEvent.ActiveItemsClicked)
        },

        onModerationClick = {
            viewModel.onEvent(ProfileEvent.ModerationItemsClicked)
        },

        onRejectedClick = {
            viewModel.onEvent(ProfileEvent.RejectedItemsClicked)
        },

        onDraftClick = {
            viewModel.onEvent(ProfileEvent.DraftItemsClicked)
        },

        onArchiveClick = {
            viewModel.onEvent(ProfileEvent.ArchivedItemsClicked)
        },

        onMyRentalsClick = {
            viewModel.onEvent(ProfileEvent.MyRentalsClicked)
        },

        onHistoryClick = {
            viewModel.onEvent(ProfileEvent.RentalHistoryClicked)
        },

        onCreateItemClick = {
            viewModel.onEvent(ProfileEvent.CreateItemClicked)
        },

        onLogoutClick = {
            viewModel.onEvent(ProfileEvent.LogoutClicked)
        }
    )
}