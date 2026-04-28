package com.example.profile.presentation.changepassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.components.RentPrimaryButton
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.RentPlatformTheme
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteBackground

@Composable
fun ChangePasswordScreen(
    uiState: ChangePasswordUiState,
    onBackClick: () -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Назад",
                    tint = TextPrimary
                )
            }

            Text(
                text = "Смена пароля",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Введите текущий пароль и новый пароль для входа в аккаунт.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        PasswordField(
            value = uiState.currentPassword,
            onValueChange = onCurrentPasswordChange,
            label = "Текущий пароль",
            error = uiState.currentPasswordError
        )

        Spacer(modifier = Modifier.height(14.dp))

        PasswordField(
            value = uiState.newPassword,
            onValueChange = onNewPasswordChange,
            label = "Новый пароль",
            error = uiState.newPasswordError
        )

        Spacer(modifier = Modifier.height(14.dp))

        PasswordField(
            value = uiState.confirmNewPassword,
            onValueChange = onConfirmNewPasswordChange,
            label = "Повторите новый пароль",
            error = uiState.confirmNewPasswordError
        )

        if (uiState.generalError != null) {
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = uiState.generalError,
                color = ErrorRed,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        RentPrimaryButton(
            text = if (uiState.isSaving) {
                "Сохранение..."
            } else {
                "Сохранить пароль"
            },
            enabled = uiState.canSave,
            onClick = onSaveClick
        )
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(text = label)
        },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        isError = error != null,
        supportingText = {
            if (error != null) {
                Text(text = error)
            }
        }
    )
}

@Preview(
    name = "Change password",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ChangePasswordScreenPreview() {
    RentPlatformTheme {
        ChangePasswordScreen(
            uiState = ChangePasswordUiState(),
            onBackClick = {},
            onCurrentPasswordChange = {},
            onNewPasswordChange = {},
            onConfirmNewPasswordChange = {},
            onSaveClick = {}
        )
    }
}

@Preview(
    name = "Change password error",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ChangePasswordScreenErrorPreview() {
    RentPlatformTheme {
        ChangePasswordScreen(
            uiState = ChangePasswordUiState(
                currentPassword = "123",
                newPassword = "123",
                confirmNewPassword = "321",
                newPasswordError = "Пароль должен быть не короче 8 символов",
                confirmNewPasswordError = "Пароли не совпадают",
                generalError = "Неверный текущий пароль"
            ),
            onBackClick = {},
            onCurrentPasswordChange = {},
            onNewPasswordChange = {},
            onConfirmNewPasswordChange = {},
            onSaveClick = {}
        )
    }
}