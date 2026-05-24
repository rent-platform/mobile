package com.example.deals.presentation.deals

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.deals.domain.model.DealRole
import com.example.deals.domain.model.DealStatus
import com.example.deals.presentation.DealFilter
import com.example.deals.presentation.DealListItemUi
import com.example.deals.presentation.DealsUiState
import com.example.ui.components.RentFilterItem
import com.example.ui.components.RentFilterRow
import com.example.ui.components.RentPrimaryButton
import com.example.ui.components.RentSegmentSwitcher
import com.example.ui.components.renterOwnerSegmentItems

@Composable
fun DealsScreen(
    uiState: DealsUiState,
    onRoleClick: (DealRole) -> Unit,
    onFilterClick: (DealFilter) -> Unit,
    onDealClick: (dealId: String) -> Unit,
    onCreateListingClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (uiState.selectedRole == DealRole.Owner) {
                DealsBottomBar(
                    onCreateListingClick = onCreateListingClick
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            Text(
                text = "Сделки",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = uiState.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            RentSegmentSwitcher(
                items = renterOwnerSegmentItems(
                    renterValue = DealRole.Renter,
                    ownerValue = DealRole.Owner
                ),
                selectedValue = uiState.selectedRole,
                onValueClick = onRoleClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            RentFilterRow(
                items = DealFilter.entries.map { filter ->
                    RentFilterItem(
                        value = filter,
                        title = filter.title
                    )
                },
                selectedValue = uiState.selectedFilter,
                onValueClick = onFilterClick,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> DealsLoadingContent()

                uiState.errorMessage != null -> DealsErrorContent(
                    message = uiState.errorMessage,
                    onRetryClick = onRetryClick
                )

                uiState.isEmpty -> DealsEmptyContent(
                    selectedRole = uiState.selectedRole,
                    selectedFilter = uiState.selectedFilter
                )

                else -> DealsListContent(
                    deals = uiState.filteredDeals,
                    onDealClick = onDealClick
                )
            }
        }
    }
}

@Composable
private fun DealsListContent(
    deals: List<DealListItemUi>,
    onDealClick: (dealId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = deals,
            key = { deal -> deal.id }
        ) { deal ->
            DealCard(
                deal = deal,
                onClick = { onDealClick(deal.id) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun DealCard(
    deal: DealListItemUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                DealItemImage(
                    title = deal.title,
                    imageResId = deal.imageResId,
                    modifier = Modifier.fillMaxSize()
                )

                DealStatusBadge(
                    status = deal.status,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                )
            }

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = deal.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )

                Text(
                    text = deal.dateRange,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = deal.totalPrice,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 4.dp
                            ),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = deal.pricingMode.title,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        deal.depositAmount?.let { deposit ->
                            Text(
                                text = "Залог $deposit",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DealItemImage(
    title: String,
    @DrawableRes imageResId: Int?,
    modifier: Modifier = Modifier
) {
    if (imageResId == null) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Фото",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = title,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun DealStatusBadge(
    status: DealStatus,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = status.containerColor()
    ) {
        Text(
            text = status.title,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = status.contentColor()
        )
    }
}

@Composable
private fun DealStatus.containerColor(): Color {
    return when (this) {
        DealStatus.PENDING -> MaterialTheme.colorScheme.tertiaryContainer
        DealStatus.CONFIRMED -> MaterialTheme.colorScheme.primaryContainer
        DealStatus.PAYMENT_PENDING -> MaterialTheme.colorScheme.primaryContainer
        DealStatus.PAID -> MaterialTheme.colorScheme.primaryContainer
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
        DealStatus.CONFIRMED -> MaterialTheme.colorScheme.onPrimaryContainer
        DealStatus.PAYMENT_PENDING -> MaterialTheme.colorScheme.onPrimaryContainer
        DealStatus.PAID -> MaterialTheme.colorScheme.onPrimaryContainer
        DealStatus.ACTIVE -> MaterialTheme.colorScheme.onSecondaryContainer
        DealStatus.COMPLETED -> MaterialTheme.colorScheme.onSurface
        DealStatus.REJECTED,
        DealStatus.CANCELLED -> MaterialTheme.colorScheme.onErrorContainer
    }
}

@Composable
private fun DealsLoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DealsErrorContent(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
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
                textAlign = TextAlign.Center,
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
private fun DealsEmptyContent(
    selectedRole: DealRole,
    selectedFilter: DealFilter,
    modifier: Modifier = Modifier
) {
    val title = when {
        selectedFilter != DealFilter.All -> "Сделок по фильтру пока нет"
        selectedRole == DealRole.Renter -> "У вас пока нет аренд"
        else -> "У вас пока нет заявок от арендаторов"
    }

    val description = when {
        selectedFilter != DealFilter.All -> "Попробуйте выбрать другой статус сделки."
        selectedRole == DealRole.Renter -> "Когда вы отправите запрос на аренду вещи, он появится в этом разделе."
        else -> "Когда пользователь отправит запрос на аренду вашей вещи, он появится здесь."
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.ReceiptLong,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DealsBottomBar(
    onCreateListingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        RentPrimaryButton(
            text = "Разместить объявление",
            onClick = onCreateListingClick,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 16.dp)
        )
    }
}