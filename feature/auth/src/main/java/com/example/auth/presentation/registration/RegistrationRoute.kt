package com.example.auth.presentation.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegistrationRoute(
    onNavigateBack: () -> Unit,
    onAuthSuccess: () -> Unit
) {
    val viewModel: RegistrationViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                RegistrationAction.AuthSuccess -> onAuthSuccess()
                is RegistrationAction.ShowError -> {
                    // можно показать snackbar
                }
            }
        }
    }

    RegistrationScreen(
        uiState = uiState,
        onPhoneChanged = {
            viewModel.onEvent(RegistrationEvent.PhoneChanged(it))
        },
        onFullNameChanged = {
            viewModel.onEvent(RegistrationEvent.NicknameChanged(it))
        },
        onPasswordChanged = {
            viewModel.onEvent(RegistrationEvent.PasswordChanged(it))
        },
        onConfirmPasswordChanged = {
            viewModel.onEvent(RegistrationEvent.ConfirmPasswordChanged(it))
        },
        onTogglePasswordVisibility = {
            viewModel.onEvent(RegistrationEvent.TogglePasswordVisibility)
        },
        onToggleConfirmPasswordVisibility = {
            viewModel.onEvent(RegistrationEvent.ToggleConfirmPasswordVisibility)
        },
        onContinueClick = {
            viewModel.onEvent(RegistrationEvent.ContinueClicked)
        },
        onBackClick = onNavigateBack
    )
}