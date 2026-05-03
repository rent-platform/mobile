package com.example.profile.presentation.editprofile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.profile.presentation.components.ProfileNavigationRow
import com.example.profile.presentation.components.ProfileOutlinedBlock
import com.example.ui.components.RentPrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    uiState: EditProfileUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onGuestProfileClick: () -> Unit,
    onUploadAvatarClick: () -> Unit,
    onDeleteAvatarClick: () -> Unit,
    onNicknameChanged: (String) -> Unit,
    onFullNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onBioChanged: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Редактирование профиля",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .navigationBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (uiState.isSaving) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }

                ProfileAvatarBlock(
                    fullName = uiState.fullName,
                    nickname = uiState.nickname,
                    isEnabled = !uiState.isSaving,
                    onUploadAvatarClick = onUploadAvatarClick,
                    onDeleteAvatarClick = onDeleteAvatarClick
                )

                ProfileOutlinedBlock {
                    ProfileNavigationRow(
                        title = "Обзор профиля",
                        subtitle = "Как видят профиль гости",
                        value = null,
                        enabled = !uiState.isSaving,
                        onClick = onGuestProfileClick
                    )
                }

                OutlinedTextField(
                    value = uiState.nickname,
                    onValueChange = onNicknameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Никнейм") },
                    singleLine = true,
                    isError = uiState.nicknameError != null,
                    supportingText = {
                        uiState.nicknameError?.let { Text(it) }
                    },
                    enabled = !uiState.isSaving
                )

                OutlinedTextField(
                    value = uiState.fullName,
                    onValueChange = onFullNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Имя и фамилия") },
                    singleLine = true,
                    isError = uiState.fullNameError != null,
                    supportingText = {
                        uiState.fullNameError?.let { Text(it) }
                    },
                    enabled = !uiState.isSaving
                )

                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = onEmailChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    isError = uiState.emailError != null,
                    supportingText = {
                        uiState.emailError?.let { Text(it) }
                    },
                    enabled = !uiState.isSaving
                )

                OutlinedTextField(
                    value = uiState.bio,
                    onValueChange = onBioChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("О себе") },
                    minLines = 4,
                    maxLines = 7,
                    isError = uiState.bioError != null,
                    supportingText = {
                        uiState.bioError?.let { Text(it) }
                    },
                    enabled = !uiState.isSaving
                )

                uiState.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                RentPrimaryButton(
                    text = if (uiState.isSaving) {
                        "Сохранение..."
                    } else {
                        "Сохранить"
                    },
                    onClick = onSaveClick,
                    enabled = !uiState.isSaving
                )
            }
        }
    }
}

@Composable
private fun ProfileAvatarBlock(
    fullName: String,
    nickname: String,
    isEnabled: Boolean,
    onUploadAvatarClick: () -> Unit,
    onDeleteAvatarClick: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Surface(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .clickable(
                        enabled = isEnabled,
                        onClick = { isMenuExpanded = true }
                    ),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    val avatarLetter = getAvatarLetter(
                        fullName = fullName,
                        nickname = nickname
                    )

                    if (avatarLetter != null) {
                        Text(
                            text = avatarLetter,
                            style = MaterialTheme.typography.headlineLarge
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Аватар",
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(32.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Загрузить новый аватар") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        isMenuExpanded = false
                        onUploadAvatarClick()
                    }
                )

                DropdownMenuItem(
                    text = { Text("Удалить текущий аватар") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        isMenuExpanded = false
                        onDeleteAvatarClick()
                    }
                )
            }
        }
    }
}

private fun getAvatarLetter(
    fullName: String,
    nickname: String
): String? {
    val source = fullName.ifBlank { nickname }.trim()

    return source
        .firstOrNull()
        ?.uppercaseChar()
        ?.toString()
}