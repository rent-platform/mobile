package com.example.auth.presentation.registration

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ui.components.RentPhoneTextField
import com.example.ui.components.RentPrimaryButton

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    uiState: RegistrationUiState,
    onPhoneChanged: (String) -> Unit,
    onFullNameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onContinueClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Арендай",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Создайте аккаунт, чтобы арендовать и сдавать вещи",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(28.dp))
        //Номер телефона
        RentPhoneTextField(
            value = uiState.phone,
            onValueChange = onPhoneChanged,
            label = "Номер телефона",
            errorText = uiState.phoneError
        )

        Spacer(modifier = Modifier.height(16.dp))
        //Имя пользователя
        OutlinedTextField(
            value = uiState.nickname,
            onValueChange = onFullNameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Имя пользователя") },
            singleLine = true,
            isError = uiState.nicknameError != null,
            supportingText = {
                uiState.nicknameError?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        //Пароль
        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Пароль") },
            singleLine = true,
            isError = uiState.passwordError != null,
            visualTransformation = if (uiState.isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                androidx.compose.material3.IconButton(onClick = onTogglePasswordVisibility) {
                    androidx.compose.material3.Icon(
                        imageVector = if (uiState.isPasswordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = "Показать или скрыть пароль"
                    )
                }
            },
            supportingText = {
                uiState.passwordError?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Подтверждение пароля
        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Подтвердите пароль") },
            singleLine = true,
            isError = uiState.confirmPasswordError != null,
            visualTransformation = if (uiState.isConfirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                androidx.compose.material3.IconButton(onClick = onToggleConfirmPasswordVisibility) {
                    androidx.compose.material3.Icon(
                        imageVector = if (uiState.isConfirmPasswordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = "Показать или скрыть пароль"
                    )
                }
            },
            supportingText = {
                uiState.confirmPasswordError?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        RentPrimaryButton(
            text = "Зарегистрироваться",
            onClick = onContinueClick,
            enabled = uiState.isContinueEnabled,
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
        )
    }
}