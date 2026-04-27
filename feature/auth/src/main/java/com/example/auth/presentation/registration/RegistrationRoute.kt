package com.example.auth.presentation.registration

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun RegistrationRoute(
    onNavigateBack: () -> Unit,
    onAuthSuccess: () -> Unit
) {
    val viewModel: RegistrationViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                RegistrationAction.AuthSuccess -> onAuthSuccess()
                is RegistrationAction.ShowError -> {
                    snackbarHostState.showSnackbar(action.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    ) { innerPadding ->
        RegistrationScreen(
            modifier = Modifier.padding(innerPadding),
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
}