package com.example.profile.presentation.profilesettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileSettingsRoute(
    onNavigateBack: () -> Unit,
    onProfileDeleted: () -> Unit
) {
    val viewModel: ProfileSettingsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.actions.collect { action ->
            when (action) {
                ProfileSettingsAction.NavigateToChangePassword -> {
                    // Потом сюда подключишь экран смены пароля
                }

                ProfileSettingsAction.ProfileDeleted -> {
                    onProfileDeleted()
                }

                is ProfileSettingsAction.ShowError -> {
                    // Потом можно показать snackbar
                }
            }
        }
    }

    ProfileSettingsScreen(
        uiState = uiState,
        onBackClick = onNavigateBack,
        onChangePasswordClick = {
            viewModel.onEvent(ProfileSettingsEvent.ChangePasswordClicked)
        },
        onDeleteProfileClick = {
            viewModel.onEvent(ProfileSettingsEvent.DeleteProfileClicked)
        },
        onConfirmDeleteClick = {
            viewModel.onEvent(ProfileSettingsEvent.ConfirmDeleteProfileClicked)
        },
        onDismissDeleteDialog = {
            viewModel.onEvent(ProfileSettingsEvent.DismissDeleteDialogClicked)
        }
    )
}