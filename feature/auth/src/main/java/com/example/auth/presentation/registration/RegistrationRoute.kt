package com.example.auth.presentation.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun RegistrationRoute(
    onNavigateBack: () -> Unit,
    onNavigateToCatalog: () -> Unit
) {
    var uiState by remember { mutableStateOf(RegistrationUiState()) }

    RegistrationScreen(
        uiState = uiState,
        onPhoneChanged = { uiState = uiState.copy(phone = it) },
        onFullNameChanged = { uiState = uiState.copy(fullName = it) },
        onPasswordChanged = { uiState = uiState.copy(password = it) },
        onConfirmPasswordChanged = { uiState = uiState.copy(confirmPassword = it) },
        onTogglePasswordVisibility = {
            uiState = uiState.copy(isPasswordVisible = !uiState.isPasswordVisible)
        },
        onToggleConfirmPasswordVisibility = {
            uiState = uiState.copy(
                isConfirmPasswordVisible = !uiState.isConfirmPasswordVisible
            )
        },
        onContinueClick = onNavigateToCatalog,
        onBackClick = onNavigateBack
    )
}