package com.example.profile.presentation.editprofile

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditProfileRoute(
    onNavigateBack: () -> Unit,
    onGuestProfileClick: () -> Unit = {}
) {
    val viewModel: EditProfileViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.actions.collect { action ->
            when (action) {
                EditProfileAction.NavigateBack -> {
                    onNavigateBack()
                }

                EditProfileAction.NavigateToGuestProfile -> {
                    onGuestProfileClick()
                }

                is EditProfileAction.ShowError -> {
                    snackbarHostState.showSnackbar(action.message)
                }
            }
        }
    }

    EditProfileScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = {
            viewModel.onEvent(EditProfileEvent.BackClicked)
        },
        onGuestProfileClick = {
            viewModel.onEvent(EditProfileEvent.GuestProfileClicked)
        },
        onUploadAvatarClick = {
            viewModel.onEvent(EditProfileEvent.UploadAvatarClicked)
        },
        onDeleteAvatarClick = {
            viewModel.onEvent(EditProfileEvent.DeleteAvatarClicked)
        },
        onNicknameChanged = { value ->
            viewModel.onEvent(EditProfileEvent.NicknameChanged(value))
        },
        onFullNameChanged = { value ->
            viewModel.onEvent(EditProfileEvent.FullNameChanged(value))
        },
        onEmailChanged = { value ->
            viewModel.onEvent(EditProfileEvent.EmailChanged(value))
        },
        onBioChanged = { value ->
            viewModel.onEvent(EditProfileEvent.BioChanged(value))
        },
        onSaveClick = {
            viewModel.onEvent(EditProfileEvent.SaveClicked)
        }
    )
}