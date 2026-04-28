package com.example.profile.presentation.profilesettings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BorderLight
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhiteBackground

@Composable
fun ProfileSettingsScreen(
    uiState: ProfileSettingsUiState,
    onBackClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onDeleteProfileClick: () -> Unit,
    onConfirmDeleteClick: () -> Unit,
    onDismissDeleteDialog: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
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
                text = "Настройки профиля",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = WhiteBackground
            ),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column {
                SettingsRow(
                    title = "Сменить пароль",
                    subtitle = "Обновить пароль для входа",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null,
                            tint = TextPrimary
                        )
                    },
                    onClick = onChangePasswordClick
                )

                HorizontalDivider(color = BorderLight)

                SettingsRow(
                    title = "Удалить профиль",
                    subtitle = "Будут утеряны все данные аккаунта",
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                            tint = ErrorRed
                        )
                    },
                    titleColor = ErrorRed,
                    subtitleColor = ErrorRed.copy(alpha = 0.75f),
                    onClick = onDeleteProfileClick
                )
            }
        }
    }

    if (uiState.isDeleteDialogVisible) {
        AlertDialog(
            onDismissRequest = onDismissDeleteDialog,
            title = {
                Text(
                    text = "Удалить профиль?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Вы точно хотите удалить профиль? Будут утеряны все данные аккаунта, объявления, история аренд и настройки профиля."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmDeleteClick,
                    enabled = !uiState.isDeleting,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = ErrorRed
                    )
                ) {
                    Text(
                        text = if (uiState.isDeleting) {
                            "Удаление..."
                        } else {
                            "Удалить профиль"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismissDeleteDialog,
                    enabled = !uiState.isDeleting
                ) {
                    Text(text = "Отмена")
                }
            }
        )
    }
}

@Composable
private fun SettingsRow(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    titleColor: androidx.compose.ui.graphics.Color = TextPrimary,
    subtitleColor: androidx.compose.ui.graphics.Color = TextSecondary,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.padding(end = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = titleColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = subtitle,
                color = subtitleColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = "›",
            color = titleColor,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}