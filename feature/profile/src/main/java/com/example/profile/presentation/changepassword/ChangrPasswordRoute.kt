package com.example.profile.presentation.changepassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChangePasswordRoute(
    onNavigateBack: () -> Unit,
    onPasswordChanged: () -> Unit
) {
    val viewModel: ChangePasswordViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.actions.collect { action ->
            when (action) {
                ChangePasswordAction.PasswordChanged -> {
                    onPasswordChanged()
                }
            }
        }
    }

    ChangePasswordScreen(
        uiState = uiState,
        onBackClick = onNavigateBack,
        onCurrentPasswordChange = {
            viewModel.onEvent(ChangePasswordEvent.CurrentPasswordChanged(it))
        },
        onNewPasswordChange = {
            viewModel.onEvent(ChangePasswordEvent.NewPasswordChanged(it))
        },
        onConfirmNewPasswordChange = {
            viewModel.onEvent(ChangePasswordEvent.ConfirmNewPasswordChanged(it))
        },
        onSaveClick = {
            viewModel.onEvent(ChangePasswordEvent.SaveClicked)
        }
    )
}