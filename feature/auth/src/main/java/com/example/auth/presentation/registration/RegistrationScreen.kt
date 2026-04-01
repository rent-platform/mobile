package com.example.auth.presentation.registration

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.ui.theme.RentPlatformTheme
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText

@Composable
fun RegistrationScreen(
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
    Column(
        modifier = Modifier
            .fillMaxSize()
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
        OutlinedTextField(
            value = uiState.phone,
            onValueChange = { input ->
                val digits = input.filter { it.isDigit() }.take(10)
                onPhoneChanged(digits)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Номер телефона") },
            prefix = { Text("+7 ") },
            placeholder = { Text("996 123-45-67") },
            singleLine = true,
            isError = uiState.phoneError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            visualTransformation = PhoneNumberVisualTransformation(),
            supportingText = {
                uiState.phoneError?.let {
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
        //Имя пользователя
        OutlinedTextField(
            value = uiState.fullName,
            onValueChange = onFullNameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Имя пользователя") },
            singleLine = true,
            isError = uiState.fullNameError != null,
            supportingText = {
                uiState.fullNameError?.let {
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

        Button(
            onClick = onContinueClick,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .height(54.dp),
            enabled = uiState.isContinueEnabled,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Зарегистрироваться",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistationTest(){
    RentPlatformTheme {
        RegistrationScreen(
            uiState = RegistrationUiState(
                phone = "9961234567",
                fullName = "Александр",
                password = "12345678",
                confirmPassword = "12345678",
                isContinueEnabled = true
            ),
            onPhoneChanged = {},
            onFullNameChanged = {},
            onPasswordChanged = {},
            onConfirmPasswordChanged = {},
            onContinueClick = {},
            onBackClick = {},
            onTogglePasswordVisibility = {},
            onToggleConfirmPasswordVisibility = {}
        )
    }
}

class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.take(10)

        val formatted = buildString {
            digits.forEachIndexed { index, char ->
                append(char)
                when (index) {
                    2 -> if (index != digits.lastIndex) append(" ")
                    5 -> if (index != digits.lastIndex) append("-")
                    7 -> if (index != digits.lastIndex) append("-")
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 6 -> offset + 1
                    offset <= 8 -> offset + 2
                    offset <= 10 -> offset + 3
                    else -> formatted.length
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 7 -> offset - 1
                    offset <= 10 -> offset - 2
                    offset <= 13 -> offset - 3
                    else -> digits.length
                }.coerceIn(0, digits.length)
            }
        }

        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = offsetMapping
        )
    }
}