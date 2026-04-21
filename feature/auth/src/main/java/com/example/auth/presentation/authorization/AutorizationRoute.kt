package com.example.auth.presentation.authorization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthorizationRoute(
    onAuthSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit
) {
    val viewModel: AutorizationViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                AuthorizationAction.NavigateToCatalog -> onAuthSuccess()
                AuthorizationAction.NavigateToRegistration -> onNavigateToRegistration()
                is AuthorizationAction.ShowError -> {
                    // Позже Snackbar
                }
            }
        }
    }

    AuthorizationScreen(
        uiState = uiState,
        onLoginChanged = {
            viewModel.onEvent(AuthorizationEvent.LoginChanged(it))
        },
        onPasswordChanged = {
            viewModel.onEvent(AuthorizationEvent.PasswordChanged(it))
        },
        onTogglePasswordVisibility = {
            viewModel.onEvent(AuthorizationEvent.TogglePasswordVisibility)
        },
        onLoginClick = {
            viewModel.onEvent(AuthorizationEvent.LoginClicked)
        },
        onRegisterClick = {
            viewModel.onEvent(AuthorizationEvent.RegisterClicked)
        }
    )
}