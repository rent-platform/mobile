package com.example.profile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.RentPrimaryButton
import com.example.ui.theme.BorderLight
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GreenContainer
import com.example.ui.theme.GreenContainerText
import com.example.ui.theme.GreenPrimary
import com.example.ui.theme.RentPlatformTheme
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WhiteBackground

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    modifier: Modifier = Modifier,

    onEditProfileClick: () -> Unit = {},
    onRatingClick: () -> Unit = {},

    onDraftClick: () -> Unit = {},
    onModerationClick: () -> Unit = {},
    onActiveClick: () -> Unit = {},
    onRejectedClick: () -> Unit = {},
    onArchiveClick: () -> Unit = {},

    onMyRentalsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},

    onCreateItemClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        ProfileHeader(
            uiState = uiState,
            onEditProfileClick = onEditProfileClick
        )

        RatingRow(
            rating = uiState.rating,
            reviewsCount = uiState.reviewsCount,
            onClick = onRatingClick
        )

        ProfileInfoColumn(uiState = uiState)

        ProfileSection(title = "Мои объявления") {

            ProfileNavigationRow(
                title = "Активные",
                subtitle = "Доступы к аренде",
                value = uiState.activeItemsCount.toString(),
                onClick = onActiveClick
            )

            HorizontalDivider(color = BorderLight)

            ProfileNavigationRow(
                title = "На модерации",
                subtitle = "Ожидают проверки",
                value = "1",
                onClick = onModerationClick
            )

            HorizontalDivider(color = BorderLight)

            ProfileNavigationRow(
                title = "Отклонённые",
                subtitle = "Нужны исправления",
                value = "1",
                onClick = onRejectedClick
            )

            HorizontalDivider(color = BorderLight)

            ProfileNavigationRow(
                title = "Черновики",
                subtitle = "Не опубликованы",
                value = "2",
                onClick = onDraftClick
            )

            HorizontalDivider(color = BorderLight)

            ProfileNavigationRow(
                title = "Архив",
                subtitle = "Скрытые объявления",
                value = "4",
                onClick = onArchiveClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            RentPrimaryButton(
                text = "Разместить объявление",
                onClick = onCreateItemClick
            )
        }

        ProfileSection(title = "Я арендую") {
            ProfileNavigationRow(
                title = "Мои аренды",
                subtitle = "Активные бронирования",
                value = uiState.rentedCount.toString(),
                onClick = onMyRentalsClick
            )

            HorizontalDivider(color = BorderLight)

            ProfileNavigationRow(
                title = "История аренд",
                subtitle = "Завершённые сделки",
                value = "8",
                onClick = onHistoryClick
            )
        }

        ProfileSection(title = "Активность аккаунта") {
            ProfileActivityRow(
                value = uiState.activeItemsCount.toString(),
                title = activeItemsText(uiState.activeItemsCount),
                subtitle = "Сейчас доступны другим пользователям"
            )

            HorizontalDivider(color = BorderLight)

            ProfileActivityRow(
                value = uiState.rentedOutCount.toString(),
                title = rentedOutText(uiState.rentedOutCount),
                subtitle = "Сделки, где вы сдавали вещи"
            )

            HorizontalDivider(color = BorderLight)

            ProfileActivityRow(
                value = uiState.rentedCount.toString(),
                title = rentedText(uiState.rentedCount),
                subtitle = "Сделки, где вы брали товары"
            )

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(14.dp))

            ProfileDetailRow(
                title = "Зарегистрирован",
                value = uiState.registeredAt
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProfileDetailRow(
                title = "Обновлён",
                value = uiState.updatedAt.orEmptyText()
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    uiState: ProfileUiState,
    onEditProfileClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfileAvatar(isActive = uiState.isActive)

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = uiState.fullName,
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${uiState.nickname ?: "nickname"}",
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(
            onClick = onEditProfileClick
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Редактировать профиль",
                tint = GreenPrimary
            )
        }
    }
}

@Composable
private fun ProfileAvatar(
    isActive: Boolean
) {
    Box(
        modifier = Modifier.size(88.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            color = GreenContainer
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = GreenContainerText,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(22.dp)
                .background(WhiteBackground, CircleShape)
                .padding(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = if (isActive) SuccessGreen else ErrorRed,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = WhiteBackground,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun RatingRow(
    rating: String,
    reviewsCount: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(BorderLight.copy(alpha = 0.25f))
            .border(
                1.dp,
                BorderLight,
                MaterialTheme.shapes.large
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Star,
            contentDescription = null,
            tint = WarningAmber
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Рейтинг профиля",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Text(
                text = "$rating • $reviewsCount отзывов",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary
            )
        }

        Text(
            text = "›",
            style = MaterialTheme.typography.headlineLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ProfileInfoColumn(
    uiState: ProfileUiState
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        ProfileInfoRow(
            title = "Почта",
            value = uiState.email.orEmptyText(),
            statusText = if (uiState.isEmailVerified) "подтверждена" else "не подтверждена",
            ok = uiState.isEmailVerified
        )

        ProfileInfoRow(
            title = "Телефон",
            value = uiState.phone.orEmptyText(),
            statusText = if (uiState.isPhoneVerified) "подтверждён" else "не подтверждён",
            ok = uiState.isPhoneVerified
        )
    }
}

@Composable
private fun ProfileInfoRow(
    title: String,
    value: String,
    statusText: String,
    ok: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = value,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            text = statusText,
            color = if (ok) SuccessGreen else WarningAmber,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun ProfileSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = WhiteBackground
            ),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = SolidColor(BorderLight)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                content = content
            )
        }
    }
}

@Composable
private fun ProfileNavigationRow(
    title: String,
    subtitle: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = subtitle,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Surface(
            shape = CircleShape,
            color = GreenContainer
        ) {
            Text(
                text = value,
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                ),
                color = GreenContainerText,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "›",
            color = GreenPrimary,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ProfileActivityRow(
    value: String,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(52.dp),
            shape = MaterialTheme.shapes.large,
            color = GreenContainer
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    color = GreenContainerText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = subtitle,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun ProfileDetailRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = TextSecondary,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value,
            color = TextPrimary,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun activeItemsText(count: Int): String {
    return when {
        count % 100 in 11..14 -> "активных объявлений"
        count % 10 == 1 -> "активное объявление"
        count % 10 in 2..4 -> "активных объявления"
        else -> "активных объявлений"
    }
}

private fun rentedOutText(count: Int): String {
    return when {
        count % 100 in 11..14 -> "сдач в аренду"
        count % 10 == 1 -> "сдача в аренду"
        count % 10 in 2..4 -> "сдачи в аренду"
        else -> "сдач в аренду"
    }
}

private fun rentedText(count: Int): String {
    return when {
        count % 100 in 11..14 -> "аренд товаров"
        count % 10 == 1 -> "аренда товара"
        count % 10 in 2..4 -> "аренды товаров"
        else -> "аренд товаров"
    }
}

private fun String?.orEmptyText(): String {
    return if (isNullOrBlank()) "Не указано" else this
}