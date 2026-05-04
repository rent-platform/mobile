package com.example.marketplace.presentation.search.filters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SearchFiltersScreen(
    uiState: SearchFiltersUiState,
    onBackClick: () -> Unit,
    onCategorySelected: (SearchFilterCategory?) -> Unit,
    onCitySelected: (SearchFilterCity?) -> Unit,
    onMinPricePerDayChange: (String) -> Unit,
    onMaxPricePerDayChange: (String) -> Unit,
    onMinPricePerHourChange: (String) -> Unit,
    onMaxPricePerHourChange: (String) -> Unit,
    onOnlyAvailableNowChange: (Boolean) -> Unit,
    onResetClick: () -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        SearchFiltersTopBar(
            onBackClick = onBackClick,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            SearchFilterBlock(
                title = "Категория"
            ) {
                CategoryDropdown(
                    categories = uiState.categories,
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = onCategorySelected
                )
            }

            SearchFilterBlock(
                title = "Город"
            ) {
                CityDropdown(
                    cities = uiState.cities,
                    selectedCity = uiState.selectedCity,
                    onCitySelected = onCitySelected
                )
            }

            SearchFilterBlock(
                title = "Цена за сутки"
            ) {
                SearchPricePerDayFields(
                    minPrice = uiState.minPricePerDay,
                    maxPrice = uiState.maxPricePerDay,
                    onMinPriceChange = onMinPricePerDayChange,
                    onMaxPriceChange = onMaxPricePerDayChange
                )
            }

            SearchFilterBlock(
                title = "Цена за час"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SearchPricePerHourFields(
                        minPrice = uiState.minPricePerHour,
                        maxPrice = uiState.maxPricePerHour,
                        onMinPriceChange = onMinPricePerHourChange,
                        onMaxPriceChange = onMaxPricePerHourChange
                    )

                    Text(
                        text = "Если указать цену за час, поиск будет выполняться по почасовой аренде. Если оставить пустым — по цене за сутки.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            OnlyAvailableNowSwitch(
                checked = uiState.onlyAvailableNow,
                onCheckedChange = onOnlyAvailableNowChange
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        SearchFiltersBottomBar(
            isResetEnabled = uiState.hasAppliedFilters,
            onResetClick = onResetClick,
            onApplyClick = onApplyClick
        )
    }
}

@Composable
private fun SearchFiltersTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "Фильтры",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun SearchFilterSectionTitle(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun SearchFilterBlock(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SearchFilterSectionTitle(
            title = title
        )

        Spacer(modifier = Modifier.height(8.dp))

        content()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    categories: List<SearchFilterCategory>,
    selectedCategory: SearchFilterCategory?,
    onCategorySelected: (SearchFilterCategory?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedCategory?.title ?: "Все категории",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            label = {
                Text(text = "Категория")
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(18.dp),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = "Все категории")
                },
                onClick = {
                    onCategorySelected(null)
                    expanded = false
                }
            )

            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(text = category.title)
                    },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CityDropdown(
    cities: List<SearchFilterCity>,
    selectedCity: SearchFilterCity?,
    onCitySelected: (SearchFilterCity?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            value = selectedCity?.title ?: "Все города",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            label = {
                Text(text = "Город")
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            shape = RoundedCornerShape(18.dp),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = "Все города")
                },
                onClick = {
                    onCitySelected(null)
                    expanded = false
                }
            )

            cities.forEach { city ->
                DropdownMenuItem(
                    text = {
                        Text(text = city.title)
                    },
                    onClick = {
                        onCitySelected(city)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchPricePerDayFields(
    minPrice: String,
    maxPrice: String,
    onMinPriceChange: (String) -> Unit,
    onMaxPriceChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PriceTextField(
            value = minPrice,
            onValueChange = onMinPriceChange,
            label = "От",
            modifier = Modifier.weight(1f)
        )

        PriceTextField(
            value = maxPrice,
            onValueChange = onMaxPriceChange,
            label = "До",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SearchPricePerHourFields(
    minPrice: String,
    maxPrice: String,
    onMinPriceChange: (String) -> Unit,
    onMaxPriceChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PriceTextField(
            value = minPrice,
            onValueChange = onMinPriceChange,
            label = "От",
            modifier = Modifier.weight(1f)
        )

        PriceTextField(
            value = maxPrice,
            onValueChange = onMaxPriceChange,
            label = "До",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PriceTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue.filter { char -> char.isDigit() })
        },
        modifier = modifier,
        label = {
            Text(text = label)
        },
        suffix = {
            Text(text = "₸")
        },
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
private fun OnlyAvailableNowSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Только доступные сейчас",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )

                    Text(
                        text = "Скрыть вещи, которые сейчас заняты",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
private fun SearchFiltersBottomBar(
    isResetEnabled: Boolean,
    onResetClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    Surface(
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onResetClick,
                enabled = isResetEnabled,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(text = "Сбросить")
            }

            Button(
                onClick = onApplyClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text(text = "Показать")
            }
        }
    }
}