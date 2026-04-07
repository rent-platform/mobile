package com.example.marketplace.presentation.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.theme.RentPlatformTheme
import androidx.compose.foundation.lazy.grid.items

@Composable
fun CatalogScreen(
    uiState: CatalogUiState,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onCategoryClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CatalogSearchBar(
            searchText = uiState.searchText,
            onSearchClick = onSearchClick,
            onFilterClick = onFilterClick,
            onNotificationsClick = onNotificationsClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Популярные категории",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        PopularCategoriesRow(
            categories = uiState.popularCategories,
            onCategoryClick = onCategoryClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        CatalogPromoBlock(
            promo = uiState.promo
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Рекомендуем для аренды",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        RecommendationGrid(
            items = uiState.recommendedItems
        )
    }
}

@Composable
fun CatalogSearchBar(
    searchText: String,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(onClick = onSearchClick),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = if (searchText.isBlank()) {
                            "Поиск вещей рядом"
                        } else {
                            searchText
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(onClick = onFilterClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Фильтры",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        CatalogActionButton(
            onClick = onNotificationsClick
        ) {
            Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = "Уведомления"
            )
        }
    }
}

@Composable
fun PopularCategoriesRow(
    categories: List<CatalogCategoryUi>,
    onCategoryClick: (Long) -> Unit
) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(176.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories, key = { it.id }) { category ->
            PopularCategoryCard(
                title = category.name,
                onClick = { onCategoryClick(category.id) }
            )
        }
    }
}

@Composable
fun PopularCategoryCard(
    title: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(138.dp)
            .height(78.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun CatalogActionButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(56.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun CatalogPromoBlock(
    promo: CatalogPromoUi
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = promo.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = promo.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = "Подробнее позже",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun RecommendationGrid(
    items: List<CatalogItemUi>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    ItemCard(
                        item = item,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ItemCard(
    item: CatalogItemUi,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Фото",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )

                if (item.priceLabel.isNotBlank()) {
                    Text(
                        text = item.priceLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (item.location.isNotBlank()) {
                    Text(
                        text = item.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatalogScreenPreview() {
    RentPlatformTheme {
        CatalogScreen(
            uiState = CatalogUiState(
                searchText = "",
                popularCategories = listOf(
                    CatalogCategoryUi(1, "Одежда"),
                    CatalogCategoryUi(2, "Инструменты"),
                    CatalogCategoryUi(3, "Спорт"),
                    CatalogCategoryUi(4, "Бытовая техника"),
                    CatalogCategoryUi(5, "Фото и видео"),
                    CatalogCategoryUi(6, "Туризм")
                ),
                promo = CatalogPromoUi(
                    title = "Арендай",
                    subtitle = "Надёжная аренда вещей рядом: находите, бронируйте и договаривайтесь проще"
                ),
                recommendedItems = listOf(
                    CatalogItemUi(
                        id = "1",
                        title = "Дрель Makita",
                        priceLabel = "700 ₽ / день",
                        location = "Новосибирск"
                    ),
                    CatalogItemUi(
                        id = "2",
                        title = "Горные лыжи",
                        priceLabel = "1200 ₽ / день",
                        location = "Новосибирск"
                    ),
                    CatalogItemUi(
                        id = "3",
                        title = "Проектор Epson",
                        priceLabel = "1500 ₽ / день",
                        location = "Новосибирск"
                    ),
                    CatalogItemUi(
                        id = "4",
                        title = "Пылесос Karcher",
                        priceLabel = "900 ₽ / день",
                        location = "Новосибирск"
                    )
                )
            ),
            onSearchClick = {},
            onFilterClick = {},
            onNotificationsClick = {},
            onCategoryClick = {}
        )
    }
}