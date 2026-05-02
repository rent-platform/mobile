package com.example.favorites.presentation.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.core.ui.components.RentItemCard

@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onAction: (FavoritesAction) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        FavoritesUiState.Loading -> {
            FavoritesLoadingContent(modifier = modifier)
        }

        FavoritesUiState.Empty -> {
            EmptyFavoritesContent(modifier = modifier)
        }

        is FavoritesUiState.Content -> {
            FavoritesContent(
                items = uiState.items,
                onAction = onAction,
                modifier = modifier
            )
        }

        is FavoritesUiState.Error -> {
            FavoritesErrorContent(
                message = uiState.message,
                onRetryClick = {
                    onAction(FavoritesAction.RetryClick)
                },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun FavoritesContent(
    items: List<FavoriteItemUi>,
    onAction: (FavoritesAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Избранное",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(
                start = 16.dp,
                top = 20.dp,
                end = 16.dp,
                bottom = 12.dp
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 24.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = items,
                key = { item -> item.id }
            ) { item ->
                RentItemCard(
                    title = item.title,
                    location = item.location,
                    pricePerDay = item.pricePerDay,
                    isFavorite = item.isFavorite,
                    imageResId = item.imageResId,
                    onClick = {
                        onAction(FavoritesAction.ItemClick(item.id))
                    },
                    onFavoriteClick = {
                        onAction(FavoritesAction.FavoriteClick(item.id))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun FavoritesLoadingContent(
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
private fun EmptyFavoritesContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Пока ничего нет",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Добавляйте объявления в избранное, чтобы быстро находить понравившиеся товары для аренды.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
private fun FavoritesErrorContent(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Не удалось загрузить избранное",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}