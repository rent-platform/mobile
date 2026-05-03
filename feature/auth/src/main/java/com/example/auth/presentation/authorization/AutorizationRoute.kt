package com.example.auth.presentation.authorization

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthorizationRoute(
    onAuthSuccess: () -> Unit,
    onNavigateToRegistration: () -> Unit
) {
    val viewModel: AutorizationViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                AuthorizationAction.AuthSuccess -> onAuthSuccess()
                AuthorizationAction.NavigateToRegistration -> onNavigateToRegistration()
                is AuthorizationAction.ShowError -> {
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
        AuthorizationScreen(
            modifier = Modifier.padding(innerPadding),
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
}