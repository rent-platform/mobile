package com.example.deals.presentation.details

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.demo.DemoIds
import com.example.core.demo.DemoScenario
import com.example.core.ui.toDemoDrawableRes
import com.example.deals.domain.model.DealDetails
import com.example.deals.domain.model.DealItemDetails
import com.example.deals.domain.model.DealRole
import com.example.deals.domain.model.DealStatus
import com.example.ui.components.RentPrimaryButton
import com.example.ui.theme.RentPlatformTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealDetailsScreen(
    uiState: DealDetailsUiState,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
    onChatClick: (chatId: String) -> Unit,
    onDealActionClick: (DealDetailsActionUi) -> Unit,
    modifier: Modifier = Modifier
) {
    val details = uiState.details

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Детали сделки",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (
                !uiState.isLoading &&
                uiState.errorMessage == null &&
                details != null &&
                (details.availableActions().isNotEmpty() || details.chatId != null)
            ) {
                DealDetailsBottomBar(
                    details = details,
                    onChatClick = onChatClick,
                    onDealActionClick = onDealActionClick
                )
            }
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                DealDetailsLoadingContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            uiState.errorMessage != null -> {
                DealDetailsErrorContent(
                    message = uiState.errorMessage,
                    onRetryClick = onRetryClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            uiState.details == null -> {
                DealDetailsEmptyContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            else -> {
                DealDetailsContent(
                    details = uiState.details,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun DealDetailsContent(
    details: DealDetails,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 12.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            DealDetailsHeaderCard(details = details)
        }

        if (!details.rejectionReason.isNullOrBlank()) {
            item {
                DealReasonCard(
                    title = when (details.status) {
                        DealStatus.REJECTED -> "Причина отклонения"
                        DealStatus.CANCELLED -> "Причина отмены"
                        else -> "Комментарий к сделке"
                    },
                    reason = details.rejectionReason
                )
            }
        }

        item {
            DealProcessCard(details = details)
        }

        item {
            DealTermsCard(details = details)
        }

        item {
            DealCounterpartyCard(details = details)
        }

        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun DealDetailsHeaderCard(
    details: DealDetails,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                DealDetailsImage(
                    imageResId = details.item.imageResId,
                    title = details.item.title,
                    modifier = Modifier.fillMaxSize()
                )

                DealStatusBadge(
                    details = details,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                )
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = details.item.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = details.role.descriptionText(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DealDetailsImage(
    @DrawableRes imageResId: Int?,
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clip(
            RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        if (imageResId == null) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.ImageNotSupported,
                        contentDescription = null,
                        modifier = Modifier.size(46.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun DealProcessCard(
    details: DealDetails,
    modifier: Modifier = Modifier
) {
    val steps = details.toProcessSteps()

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Процесс аренды",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                steps.forEachIndexed { index, step ->
                    DealProcessStepRow(
                        index = index + 1,
                        step = step
                    )
                }
            }

            Text(
                text = details.statusDescription(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DealProcessStepRow(
    index: Int,
    step: DealProcessStepUi,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = CircleShape,
            color = when (step.state) {
                DealProcessStepState.Done -> MaterialTheme.colorScheme.primary
                DealProcessStepState.Current -> MaterialTheme.colorScheme.primaryContainer
                DealProcessStepState.Waiting -> MaterialTheme.colorScheme.surfaceVariant
                DealProcessStepState.Failed -> MaterialTheme.colorScheme.errorContainer
            }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = index.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = when (step.state) {
                        DealProcessStepState.Done -> MaterialTheme.colorScheme.onPrimary
                        DealProcessStepState.Current -> MaterialTheme.colorScheme.onPrimaryContainer
                        DealProcessStepState.Waiting -> MaterialTheme.colorScheme.onSurfaceVariant
                        DealProcessStepState.Failed -> MaterialTheme.colorScheme.onErrorContainer
                    }
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = step.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (step.state == DealProcessStepState.Current) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                },
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = step.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DealTermsCard(
    details: DealDetails,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Условия сделки",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            DealInfoRow(
                title = "Период",
                value = "${formatDemoDate(details.startDate)} — ${formatDemoDate(details.endDate)}"
            )

            DealInfoRow(
                title = "Стоимость аренды",
                value = "${formatPrice(details.totalPrice)} ₽"
            )

            DealInfoRow(
                title = "Залог",
                value = "${formatPrice(details.depositAmount)} ₽"
            )

            DealInfoRow(
                title = "Итого к оплате",
                value = "${formatPrice(details.totalPaymentAmount())} ₽",
                isImportant = true
            )

            details.item.city?.takeIf { it.isNotBlank() }?.let { city ->
                DealInfoRow(
                    title = "Город",
                    value = city
                )
            }

            details.item.pickupLocation?.takeIf { it.isNotBlank() }?.let { location ->
                DealInfoRow(
                    title = "Место передачи",
                    value = location
                )
            }
        }
    }
}

@Composable
private fun DealCounterpartyCard(
    details: DealDetails,
    modifier: Modifier = Modifier
) {
    val title = when (details.role) {
        DealRole.Owner -> "Арендатор"
        DealRole.Renter -> "Арендодатель"
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = details.counterpartyName
                            .firstOrNull()
                            ?.uppercase()
                            .orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = details.counterpartyName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun DealReasonCard(
    title: String,
    reason: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.55f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Text(
                text = reason,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun DealActionsCard(
    details: DealDetails,
    onChatClick: (chatId: String) -> Unit,
    onDealActionClick: (DealDetailsActionUi) -> Unit,
    modifier: Modifier = Modifier
) {
    val actions = details.availableActions()

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Действия",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            actions.forEach { action ->
                DealActionButton(
                    action = action,
                    role = details.role,
                    onClick = {
                        onDealActionClick(action)
                    }
                )
            }

            FilledTonalButton(
                onClick = {
                    details.chatId?.let(onChatClick)
                },
                enabled = details.chatId != null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Открыть чат",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DealActionButton(
    action: DealDetailsActionUi,
    role: DealRole,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = action.titleForRole(role)

    when (action.type) {
        DealDetailsActionType.PRIMARY -> {
            RentPrimaryButton(
                text = title,
                onClick = onClick,
                modifier = modifier
            )
        }

        DealDetailsActionType.SECONDARY -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        DealDetailsActionType.DANGER -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DealInfoRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    isImportant: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = if (isImportant) {
                MaterialTheme.typography.titleSmall
            } else {
                MaterialTheme.typography.bodyMedium
            },
            fontWeight = if (isImportant) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
            color = if (isImportant) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = value,
            modifier = Modifier.weight(1.25f),
            style = if (isImportant) {
                MaterialTheme.typography.titleSmall
            } else {
                MaterialTheme.typography.bodyMedium
            },
            fontWeight = FontWeight.Bold,
            color = if (isImportant) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

@Composable
private fun DealStatusBadge(
    details: DealDetails,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = details.status.containerColor()
    ) {
        Text(
            text = details.displayStatusTitle(),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = details.status.contentColor(),
            maxLines = 1
        )
    }
}

@Composable
private fun DealDetailsLoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DealDetailsErrorContent(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = null,
                modifier = Modifier.size(42.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(onClick = onRetryClick) {
                Text(text = "Повторить")
            }
        }
    }
}

@Composable
private fun DealDetailsEmptyContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = null,
                        modifier = Modifier.size(34.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Сделка не найдена",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Попробуйте вернуться назад и открыть сделку заново.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Immutable
private data class DealProcessStepUi(
    val title: String,
    val description: String,
    val state: DealProcessStepState
)

private enum class DealProcessStepState {
    Done,
    Current,
    Waiting,
    Failed
}

private fun DealDetails.toProcessSteps(): List<DealProcessStepUi> {
    val failed = status == DealStatus.REJECTED || status == DealStatus.CANCELLED

    return listOf(
        DealProcessStepUi(
            title = "Заявка",
            description = "Арендатор отправил запрос на аренду.",
            state = when {
                failed -> DealProcessStepState.Done
                else -> DealProcessStepState.Done
            }
        ),
        DealProcessStepUi(
            title = "Подтверждение",
            description = "Владелец подтверждает возможность аренды.",
            state = when (status) {
                DealStatus.PENDING -> DealProcessStepState.Current
                DealStatus.REJECTED,
                DealStatus.CANCELLED -> DealProcessStepState.Failed
                else -> DealProcessStepState.Done
            }
        ),
        DealProcessStepUi(
            title = "Оплата",
            description = "Оплата аренды и залога перед передачей вещи.",
            state = when {
                status == DealStatus.PENDING -> {
                    DealProcessStepState.Waiting
                }

                status == DealStatus.CONFIRMED -> {
                    DealProcessStepState.Current
                }

                status == DealStatus.PAYMENT_PENDING && !isPaymentPaid -> {
                    DealProcessStepState.Current
                }

                status == DealStatus.PAYMENT_PENDING && isPaymentPaid -> {
                    DealProcessStepState.Done
                }

                status == DealStatus.ACTIVE || status == DealStatus.COMPLETED -> {
                    DealProcessStepState.Done
                }

                status == DealStatus.REJECTED || status == DealStatus.CANCELLED -> {
                    DealProcessStepState.Failed
                }

                else -> {
                    DealProcessStepState.Waiting
                }
            }
        ),
        DealProcessStepUi(
            title = "Передача вещи",
            description = "Стороны подтверждают передачу вещи перед началом аренды.",
            state = when {
                status == DealStatus.PAYMENT_PENDING && isPaymentPaid && !startConfirmedByMe -> {
                    DealProcessStepState.Current
                }

                status == DealStatus.PAYMENT_PENDING && isPaymentPaid && startConfirmedByMe && !startConfirmedByOther -> {
                    DealProcessStepState.Current
                }

                status == DealStatus.ACTIVE || status == DealStatus.COMPLETED -> {
                    DealProcessStepState.Done
                }

                status == DealStatus.REJECTED || status == DealStatus.CANCELLED -> {
                    DealProcessStepState.Failed
                }

                else -> {
                    DealProcessStepState.Waiting
                }
            }
        ),
        DealProcessStepUi(
            title = "Возврат вещи",
            description = "После срока аренды стороны подтверждают возврат вещи.",
            state = when {
                status == DealStatus.ACTIVE && !completeConfirmedByMe -> {
                    DealProcessStepState.Current
                }

                status == DealStatus.ACTIVE && completeConfirmedByMe && !completeConfirmedByOther -> {
                    DealProcessStepState.Current
                }

                status == DealStatus.COMPLETED -> {
                    DealProcessStepState.Done
                }

                status == DealStatus.REJECTED || status == DealStatus.CANCELLED -> {
                    DealProcessStepState.Failed
                }

                else -> {
                    DealProcessStepState.Waiting
                }
            }
        )
    )
}

@Composable
private fun DealDetailsBottomBar(
    details: DealDetails,
    onChatClick: (chatId: String) -> Unit,
    onDealActionClick: (DealDetailsActionUi) -> Unit,
    modifier: Modifier = Modifier
) {
    val actions = details.availableActions()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            actions.forEach { action ->
                DealActionButton(
                    action = action,
                    role = details.role,
                    onClick = {
                        onDealActionClick(action)
                    }
                )
            }

            FilledTonalButton(
                onClick = {
                    details.chatId?.let(onChatClick)
                },
                enabled = details.chatId != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Открыть чат",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun DealDetails.statusDescription(): String {
    return when (status) {
        DealStatus.PENDING -> {
            when (role) {
                DealRole.Owner -> "Новая заявка ожидает вашего подтверждения."
                DealRole.Renter -> "Заявка отправлена. Ожидаем подтверждения владельца."
            }
        }

        DealStatus.CONFIRMED -> {
            when (role) {
                DealRole.Owner -> "Заявка подтверждена. Ссылка на оплату создана автоматически, ожидаем оплату от арендатора."
                DealRole.Renter -> "Владелец подтвердил заявку. Теперь можно перейти к оплате аренды и залога."
            }
        }

        DealStatus.PAYMENT_PENDING -> {
            when {
                !isPaymentPaid && role == DealRole.Owner -> {
                    "Счёт создан. Ожидаем оплату от арендатора."
                }

                !isPaymentPaid && role == DealRole.Renter -> {
                    "Оплатите аренду и залог, чтобы перейти к передаче вещи."
                }

                isPaymentPaid && !startConfirmedByMe && role == DealRole.Owner -> {
                    "Оплата прошла. Передайте вещь арендатору и подтвердите передачу."
                }

                isPaymentPaid && !startConfirmedByMe && role == DealRole.Renter -> {
                    "Оплата прошла. Получите вещь у владельца и подтвердите получение."
                }

                isPaymentPaid && startConfirmedByMe && !startConfirmedByOther -> {
                    "Вы подтвердили передачу. Ожидаем подтверждения второй стороны."
                }

                else -> {
                    "Оплата прошла. Ожидаем подтверждение передачи вещи."
                }
            }
        }

        DealStatus.ACTIVE -> {
            "Сделка активна. После возврата вещи стороны подтверждают завершение."
        }

        DealStatus.COMPLETED -> {
            "Аренда завершена. Теперь можно оставить отзыв по сделке."
        }

        DealStatus.REJECTED -> {
            "Заявка была отклонена владельцем."
        }

        DealStatus.CANCELLED -> {
            "Сделка была отменена одной из сторон."
        }
    }
}

private fun DealRole.descriptionText(): String {
    return when (this) {
        DealRole.Owner -> "Вы передаёте вещь арендатору"
        DealRole.Renter -> "Вы получаете вещь у владельца"
    }
}

@Composable
private fun DealStatus.containerColor(): Color {
    return when (this) {
        DealStatus.PENDING -> MaterialTheme.colorScheme.tertiaryContainer

        DealStatus.CONFIRMED,
        DealStatus.PAYMENT_PENDING -> MaterialTheme.colorScheme.primaryContainer

        DealStatus.ACTIVE -> MaterialTheme.colorScheme.secondaryContainer

        DealStatus.COMPLETED -> MaterialTheme.colorScheme.surface

        DealStatus.REJECTED,
        DealStatus.CANCELLED -> MaterialTheme.colorScheme.errorContainer
    }
}

@Composable
private fun DealStatus.contentColor(): Color {
    return when (this) {
        DealStatus.PENDING -> MaterialTheme.colorScheme.onTertiaryContainer

        DealStatus.CONFIRMED,
        DealStatus.PAYMENT_PENDING -> MaterialTheme.colorScheme.onPrimaryContainer

        DealStatus.ACTIVE -> MaterialTheme.colorScheme.onSecondaryContainer

        DealStatus.COMPLETED -> MaterialTheme.colorScheme.onSurface

        DealStatus.REJECTED,
        DealStatus.CANCELLED -> MaterialTheme.colorScheme.onErrorContainer
    }
}

private fun formatPrice(value: Long): String {
    return value
        .toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()
}

private fun formatDemoDate(value: String): String {
    val date = value.take(10)
    val parts = date.split("-")

    if (parts.size != 3) return date

    val year = parts[0]
    val month = when (parts[1]) {
        "01" -> "янв."
        "02" -> "фев."
        "03" -> "мар."
        "04" -> "апр."
        "05" -> "мая"
        "06" -> "июн."
        "07" -> "июл."
        "08" -> "авг."
        "09" -> "сен."
        "10" -> "окт."
        "11" -> "ноя."
        "12" -> "дек."
        else -> parts[1]
    }
    val day = parts[2].trimStart('0').ifBlank { parts[2] }

    return "$day $month $year"
}

private fun DealDetails.availableActions(): List<DealDetailsActionUi> {
    return when (status) {
        DealStatus.PENDING -> {
            when (role) {
                DealRole.Owner -> {
                    listOf(
                        DealDetailsActionUi.CONFIRM_REQUEST,
                        DealDetailsActionUi.REJECT_REQUEST
                    )
                }

                DealRole.Renter -> {
                    listOf(DealDetailsActionUi.CANCEL)
                }
            }
        }

        DealStatus.CONFIRMED,
        DealStatus.PAYMENT_PENDING -> {
            when {
                !isPaymentPaid && role == DealRole.Renter -> {
                    listOf(
                        DealDetailsActionUi.PAY,
                        DealDetailsActionUi.CANCEL
                    )
                }

                !isPaymentPaid && role == DealRole.Owner -> {
                    listOf(DealDetailsActionUi.CANCEL)
                }

                isPaymentPaid && !startConfirmedByMe -> {
                    listOf(DealDetailsActionUi.CONFIRM_START)
                }

                isPaymentPaid && startConfirmedByMe && !startConfirmedByOther -> {
                    emptyList()
                }

                else -> {
                    emptyList()
                }
            }
        }

        DealStatus.ACTIVE -> {
            if (!completeConfirmedByMe) {
                listOf(DealDetailsActionUi.CONFIRM_COMPLETE)
            } else {
                emptyList()
            }
        }

        DealStatus.COMPLETED -> {
            if (reviewLeftByMe) {
                emptyList()
            } else {
                listOf(DealDetailsActionUi.LEAVE_REVIEW)
            }
        }

        DealStatus.REJECTED,
        DealStatus.CANCELLED -> {
            emptyList()
        }
    }
}

private fun DealDetailsActionUi.titleForRole(role: DealRole): String {
    return when (this) {
        DealDetailsActionUi.CONFIRM_START -> {
            when (role) {
                DealRole.Owner -> "Вещь передана"
                DealRole.Renter -> "Вещь получена"
            }
        }

        DealDetailsActionUi.CONFIRM_COMPLETE -> {
            when (role) {
                DealRole.Owner -> "Вещь возвращена"
                DealRole.Renter -> "Я вернул вещь"
            }
        }

        else -> title
    }
}

private fun DealDetails.totalPaymentAmount(): Long {
    return totalPrice + depositAmount
}
private fun DealDetails.displayStatusTitle(): String {
    return when {
        status == DealStatus.PAYMENT_PENDING && isPaymentPaid && !startConfirmedByMe -> {
            when (role) {
                DealRole.Owner -> "Ожидает передачи"
                DealRole.Renter -> "Ожидает получения"
            }
        }

        status == DealStatus.PAYMENT_PENDING && isPaymentPaid && startConfirmedByMe && !startConfirmedByOther -> {
            "Ожидает подтверждения"
        }

        else -> status.title
    }
}

@Preview(
    name = "Renter — Pending",
    showBackground = true,
    heightDp = 1400
)
@Composable
private fun DealDetailsScreenRenterPendingPreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                details = previewDealDetails(
                    status = DealStatus.PENDING,
                    role = DealRole.Renter
                )
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Owner — Pending",
    showBackground = true,
    heightDp = 1450
)
@Composable
private fun DealDetailsScreenOwnerPendingPreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                details = previewDealDetails(
                    status = DealStatus.PENDING,
                    role = DealRole.Owner,
                    counterpartyName = "ivan_phone"
                )
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Owner — Confirmed",
    showBackground = true,
    heightDp = 1400
)
@Composable
private fun DealDetailsScreenOwnerConfirmedPreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                details = previewDealDetails(
                    status = DealStatus.CONFIRMED,
                    role = DealRole.Owner,
                    counterpartyName = "ivan_phone"
                )
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Renter — Payment Pending",
    showBackground = true,
    heightDp = 1470
)
@Composable
private fun DealDetailsScreenRenterPaymentPendingPreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                details = previewDealDetails(
                    status = DealStatus.PAYMENT_PENDING,
                    role = DealRole.Renter
                )
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Owner — Active",
    showBackground = true,
    heightDp = 1400
)
@Composable
private fun DealDetailsScreenOwnerActivePreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                details = previewDealDetails(
                    status = DealStatus.ACTIVE,
                    role = DealRole.Owner,
                    counterpartyName = "ivan_phone",
                    startConfirmedByMe = true,
                    startConfirmedByOther = true
                )
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Renter — Active",
    showBackground = true,
    heightDp = 1400
)
@Composable
private fun DealDetailsScreenRenterActivePreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                details = previewDealDetails(
                    status = DealStatus.ACTIVE,
                    role = DealRole.Renter,
                    startConfirmedByMe = true,
                    startConfirmedByOther = true
                )
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Completed — Review Available",
    showBackground = true,
    heightDp = 1400
)
@Composable
private fun DealDetailsScreenCompletedPreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                details = previewDealDetails(
                    status = DealStatus.COMPLETED,
                    role = DealRole.Renter,
                    startConfirmedByMe = true,
                    startConfirmedByOther = true,
                    completeConfirmedByMe = true,
                    completeConfirmedByOther = true,
                    reviewLeftByMe = false
                )
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Rejected",
    showBackground = true,
    heightDp = 1450
)
@Composable
private fun DealDetailsScreenRejectedPreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                details = previewDealDetails(
                    status = DealStatus.REJECTED,
                    role = DealRole.Renter,
                    rejectionReason = "Владелец отклонил заявку: вещь уже занята на выбранные даты."
                )
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Cancelled",
    showBackground = true,
    heightDp = 1420
)
@Composable
private fun DealDetailsScreenCancelledPreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                details = previewDealDetails(
                    status = DealStatus.CANCELLED,
                    role = DealRole.Owner,
                    counterpartyName = "ivan_phone",
                    rejectionReason = "Сделка отменена по договорённости сторон."
                )
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Получена у арендатора",
    showBackground = true,
    heightDp = 1420
)
@Composable
private fun DealDetailsScreenGetPreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                details = previewDealDetails(
                    status = DealStatus.PAYMENT_PENDING,
                    role = DealRole.Renter,
                    isPaymentPaid = true,
                    startConfirmedByMe = false,
                    startConfirmedByOther = false
                )
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Loading",
    showBackground = true
)
@Composable
private fun DealDetailsScreenLoadingPreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                isLoading = true
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Error",
    showBackground = true
)
@Composable
private fun DealDetailsScreenErrorPreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                errorMessage = "Не удалось загрузить сделку"
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

@Preview(
    name = "Empty",
    showBackground = true
)
@Composable
private fun DealDetailsScreenEmptyPreview() {
    RentPlatformTheme {
        DealDetailsScreen(
            uiState = DealDetailsUiState(
                details = null
            ),
            onBackClick = {},
            onRetryClick = {},
            onChatClick = {},
            onDealActionClick = {}
        )
    }
}

private fun previewDealDetails(
    status: DealStatus = DealStatus.PAYMENT_PENDING,
    role: DealRole = DealRole.Renter,
    counterpartyName: String = "maria_home",
    rejectionReason: String? = null,
    isPaymentPaid: Boolean = false,
    startConfirmedByMe: Boolean = false,
    startConfirmedByOther: Boolean = false,
    completeConfirmedByMe: Boolean = false,
    completeConfirmedByOther: Boolean = false,
    reviewLeftByMe: Boolean = false
): DealDetails {
    val item = requireNotNull(
        DemoScenario.findItemById(DemoIds.ITEM_CANON_ID)
    )

    return DealDetails(
        id = DemoIds.DEAL_CANON_ID,
        item = DealItemDetails(
            id = item.id,
            title = item.title,
            description = item.description,
            imageResId = item.imageKey.toDemoDrawableRes(),
            city = item.city,
            pickupLocation = item.pickupLocation,
            pricePerDay = item.pricePerDay,
            pricePerHour = item.pricePerHour,
            depositAmount = item.depositAmount
        ),
        status = status,
        role = role,
        renterId = DemoIds.CURRENT_USER_ID,
        ownerId = DemoIds.OWNER_MARIA_ID,
        counterpartyName = counterpartyName,
        counterpartyAvatarResId = null,
        startDate = "2026-04-23T10:00:00.000Z",
        endDate = "2026-04-25T18:00:00.000Z",
        totalPrice = 15_000,
        depositAmount = 15_000,
        rejectionReason = rejectionReason,
        chatId = DemoIds.CHAT_CANON_ID,
        startConfirmedByMe = startConfirmedByMe,
        startConfirmedByOther = startConfirmedByOther,
        completeConfirmedByMe = completeConfirmedByMe,
        completeConfirmedByOther = completeConfirmedByOther,
        reviewLeftByMe = reviewLeftByMe,
        isPaymentPaid = isPaymentPaid,
    )
}