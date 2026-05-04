package com.example.marketplace.presentation.search.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.marketplace.presentation.catalog.CatalogItemUi
import com.example.marketplace.presentation.components.MarketplaceItemCard
import com.example.marketplace.presentation.components.MarketplaceSearchBar
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchResultsScreen(
    uiState: SearchResultsUiState,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    onRemoveFilterClick: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        MarketplaceSearchBar(
            searchText = uiState.query,
            placeholder = "Что ищем?",
            showNotifications = false,
            onSearchClick = onSearchClick,
            onFilterClick = onFilterClick,
            onNotificationsClick = {},
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.activeFilters.isNotEmpty()) {
            SearchFiltersRow(
                filters = uiState.activeFilters,
                onRemoveFilterClick = onRemoveFilterClick
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        SearchResultsContent(
            uiState = uiState,
            onItemClick = onItemClick,
            onFavoriteClick = onFavoriteClick,
            onRetryClick = onRetryClick
        )
    }
}

@Composable
private fun SearchFiltersRow(
    filters: List<SearchFilterUi>,
    onRemoveFilterClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = filters,
            key = { filter -> filter.id }
        ) { filter ->
            InputChip(
                selected = false,
                onClick = {
                    onRemoveFilterClick(filter.id)
                },
                label = {
                    Text(text = filter.title)
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Удалить фильтр"
                    )
                }
            )
        }
    }
}

@Composable
private fun SearchResultsContent(
    uiState: SearchResultsUiState,
    onItemClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onRetryClick: () -> Unit
) {
    when {
        uiState.isLoading -> {
            SearchResultsLoading()
        }

        uiState.errorMessage != null -> {
            SearchResultsError(
                message = uiState.errorMessage,
                onRetryClick = onRetryClick
            )
        }

        uiState.isEmpty -> {
            SearchResultsEmpty(
                query = uiState.query
            )
        }

        else -> {
            SearchResultsList(
                items = uiState.items,
                onItemClick = onItemClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
private fun SearchResultsList(
    items: List<CatalogItemUi>,
    onItemClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit
) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 4.dp,
            end = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.chunked(2).forEachIndexed { rowIndex, rowItems ->
            item(
                key = rowItems.joinToString(separator = "_") { it.id }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { item ->
                        MarketplaceItemCard(
                            item = item,
                            onClick = {
                                onItemClick(item.id)
                            },
                            onFavoriteClick = {
                                onFavoriteClick(item.id)
                            },
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
}

@Composable
private fun SearchResultsLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SearchResultsEmpty(
    query: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ничего не найдено",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (query.isBlank()) {
                "Попробуйте изменить запрос или фильтры"
            } else {
                "По запросу «$query» ничего не найдено. Попробуйте изменить запрос или фильтры"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SearchResultsError(
    message: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.WarningAmber,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Не удалось загрузить результаты",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        ElevatedButton(
            onClick = onRetryClick,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Повторить")
        }
    }
}