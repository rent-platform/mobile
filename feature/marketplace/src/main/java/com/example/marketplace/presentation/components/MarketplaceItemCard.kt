package com.example.marketplace.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.core.ui.components.RentItemCard
import com.example.marketplace.presentation.catalog.CatalogItemUi

@Composable
fun MarketplaceItemCard(
    item: CatalogItemUi,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    RentItemCard(
        title = item.title,
        location = item.location,
        pricePerDay = item.pricePerDay?.let { formatPricePerDay(it) },
        imageResId = item.imageUrl,
        isFavorite = item.isFavorite,
        onClick = onClick,
        onFavoriteClick = onFavoriteClick,
        modifier = modifier
    )
}