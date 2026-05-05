package com.example.marketplace.presentation.listing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.components.RentPrimaryButton
import com.example.ui.components.RentTextField
import com.example.ui.theme.RentPlatformTheme
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect

@Composable
fun CreateListingScreen(
    state: CreateListingUiState,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onPublishClick: () -> Unit,

    onAddPhotoClick: () -> Unit,
    onRemovePhotoClick: (photoId: String) -> Unit,

    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategorySelected: (ListingCategory) -> Unit,

    onPricePerDayChange: (String) -> Unit,
    onPricePerHourChange: (String) -> Unit,
    onDepositEnabledChange: (Boolean) -> Unit,
    onDepositAmountChange: (String) -> Unit,

    onCityChange: (String) -> Unit,
    onPickupLocationChange: (String) -> Unit,

    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.currentStep) {
        listState.scrollToItem(0)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CreateListingTopBar(
                state = state,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            CreateListingBottomBar(
                state = state,
                onBackClick = onBackClick,
                onNextClick = onNextClick,
                onPublishClick = onPublishClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 12.dp,
                end = 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                CreateListingContentCard(
                    title = state.currentStep.title,
                    subtitle = state.currentStep.subtitle
                ) {
                    when (state.currentStep) {
                        ListingStep.Photos -> {
                            PhotosStepContent(
                                photos = state.photos,
                                isPhotosError = state.isPhotosError,
                                onAddPhotoClick = onAddPhotoClick,
                                onRemovePhotoClick = onRemovePhotoClick
                            )
                        }

                        ListingStep.Description -> {
                            DescriptionStepContent(
                                title = state.title,
                                description = state.description,
                                selectedCategory = state.selectedCategory,
                                onTitleChange = onTitleChange,
                                onDescriptionChange = onDescriptionChange,
                                onCategorySelected = onCategorySelected
                            )
                        }

                        ListingStep.Terms -> {
                            TermsStepContent(
                                pricePerDay = state.pricePerDay,
                                pricePerHour = state.pricePerHour,
                                hasDeposit = state.hasDeposit,
                                depositAmount = state.depositAmount,
                                city = state.city,
                                pickupLocation = state.pickupLocation,
                                onPricePerDayChange = onPricePerDayChange,
                                onPricePerHourChange = onPricePerHourChange,
                                onDepositEnabledChange = onDepositEnabledChange,
                                onDepositAmountChange = onDepositAmountChange,
                                onCityChange = onCityChange,
                                onPickupLocationChange = onPickupLocationChange
                            )
                        }
                    }
                }
            }

            state.errorMessage?.let { error ->
                item {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateListingTopBar(
    state: CreateListingUiState,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .statusBarsPadding()
            .padding( bottom = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Сдать в аренду",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "Шаг ${state.currentStepNumber} из ${state.totalSteps}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = { state.progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(100.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        CreateListingStepIndicator(
            currentStep = state.currentStep,
            hasPhotos = state.hasPhotos
        )
    }
}

@Composable
private fun CreateListingStepIndicator(
    currentStep: ListingStep,
    hasPhotos: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ListingStep.entries.forEach { step ->
            val isSelected = step == currentStep

            val isCompleted = when (step) {
                ListingStep.Photos -> hasPhotos && step.ordinal < currentStep.ordinal
                else -> step.ordinal < currentStep.ordinal
            }

            Surface(
                shape = RoundedCornerShape(100.dp),
                color = when {
                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                    isCompleted -> MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    else -> MaterialTheme.colorScheme.surface
                },
                border = BorderStroke(
                    width = 1.dp,
                    color = when {
                        isSelected || isCompleted -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.outlineVariant
                    }
                )
            ) {
                Text(
                    text = if (isCompleted) {
                        "✓ ${step.shortTitle}"
                    } else {
                        step.shortTitle
                    },
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = when {
                        isSelected || isCompleted -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun CreateListingContentCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            content()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PhotosStepContent(
    photos: List<ListingPhotoUi>,
    isPhotosError: Boolean,
    onAddPhotoClick: () -> Unit,
    onRemovePhotoClick: (photoId: String) -> Unit
) {
    Column {

        Text(
            text = "Объявление можно сохранить в черновиках без фотографий.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = if (isPhotosError) {
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
            } else {
                MaterialTheme.colorScheme.surface
            },
            border = BorderStroke(
                width = 1.dp,
                color = if (isPhotosError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }
            )
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                val spacing = 12.dp
                val itemSize = (maxWidth - spacing * 2) / 3

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    verticalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    photos.forEach { photo ->
                        ListingPhotoItem(
                            photo = photo,
                            size = itemSize,
                            onRemoveClick = {
                                onRemovePhotoClick(photo.id)
                            }
                        )
                    }

                    if (photos.size < CreateListingUiState.MAX_PHOTOS) {
                        AddPhotoItem(
                            size = itemSize,
                            onClick = onAddPhotoClick
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "❗ Для публикации добавьте хотя бы одну фотографию.",
            style = MaterialTheme.typography.bodySmall,
            color = if (isPhotosError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
private fun ListingPhotoItem(
    photo: ListingPhotoUi,
    size: Dp,
    onRemoveClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Text(
            text = "Фото",
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (photo.isCover) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp),
                shape = RoundedCornerShape(100.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "Обложка",
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .size(24.dp)
                .clickable(onClick = onRemoveClick),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.55f)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "×",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun AddPhotoItem(
    size: Dp,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(
                modifier = Modifier.size(34.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Добавить",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1
        )
    }
}

@Composable
private fun DescriptionStepContent(
    title: String,
    description: String,
    selectedCategory: ListingCategory?,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategorySelected: (ListingCategory) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        LabeledField(
            label = "Название *"
        ) {
            RentTextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = "Например: Дрель Bosch",
                singleLine = true
            )
        }

        LabeledField(
            label = "Категория *"
        ) {
            ListingCategoryDropdown(
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )
        }

        LabeledField(
            label = "Описание"
        ) {
            RentTextField(
                value = description,
                onValueChange = onDescriptionChange,
                placeholder = "Расскажите о вещи: что входит в комплект, особенности, правила использования",
                singleLine = false,
                minLines = 5
            )
        }
    }
}
@Composable
private fun PriceFieldsBlock(
    pricePerDay: String,
    pricePerHour: String,
    onPricePerDayChange: (String) -> Unit,
    onPricePerHourChange: (String) -> Unit
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Цена за сутки",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                PriceTextField(
                    value = pricePerDay,
                    onValueChange = onPricePerDayChange,
                    label = null
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Цена за час",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                PriceTextField(
                    value = pricePerHour,
                    onValueChange = onPricePerHourChange,
                    label = null
                )
            }
        }
        Text(
            text = "Укажите цену за сутки или за час. Достаточно одного варианта.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LabeledField(
    label: String,
    modifier: Modifier = Modifier,
    helperText: String? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        content()

        if (helperText != null) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = helperText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListingCategoryDropdown(
    selectedCategory: ListingCategory?,
    onCategorySelected: (ListingCategory) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        RentTextField(
            value = selectedCategory?.displayName.orEmpty(),
            onValueChange = {},
            placeholder = "Выберите категорию",
            readOnly = true,
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Открыть список категорий",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            ListingCategory.entries.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(text = category.displayName)
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

@Composable
private fun TermsStepContent(
    pricePerDay: String,
    pricePerHour: String,
    hasDeposit: Boolean,
    depositAmount: String,
    city: String,
    pickupLocation: String,
    onPricePerDayChange: (String) -> Unit,
    onPricePerHourChange: (String) -> Unit,
    onDepositEnabledChange: (Boolean) -> Unit,
    onDepositAmountChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onPickupLocationChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PriceFieldsBlock(
            pricePerDay = pricePerDay,
            pricePerHour = pricePerHour,
            onPricePerDayChange = onPricePerDayChange,
            onPricePerHourChange = onPricePerHourChange
        )

        ListingDepositBlock(
            hasDeposit = hasDeposit,
            depositAmount = depositAmount,
            onDepositEnabledChange = onDepositEnabledChange,
            onDepositAmountChange = onDepositAmountChange
        )

        LabeledField(
            label = "Город *"
        ) {
            RentTextField(
                value = city,
                onValueChange = onCityChange,
                placeholder = "Например: Москва",
                singleLine = true
            )
        }

        LabeledField(
            label = "Место выдачи *"
        ) {
            RentTextField(
                value = pickupLocation,
                onValueChange = onPickupLocationChange,
                placeholder = "Город, район или адрес самовывоза",
                singleLine = true
            )
        }
    }
}

@Composable
private fun PriceTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier
) {
    RentTextField(
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue.filter(Char::isDigit))
        },
        label = label,
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    )
}

@Composable
private fun ListingDepositBlock(
    hasDeposit: Boolean,
    depositAmount: String,
    onDepositEnabledChange: (Boolean) -> Unit,
    onDepositAmountChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(
            width = 1.dp,
            color = if (hasDeposit) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Залог",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = if (hasDeposit) {
                            "Укажите сумму, которую арендатор оставит как гарантию"
                        } else {
                            "Без залога вы повысите популярность объявления, но это несет риски"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Switch(
                    checked = hasDeposit,
                    onCheckedChange = onDepositEnabledChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            if (hasDeposit) {
                Spacer(modifier = Modifier.height(16.dp))

                PriceTextField(
                    value = depositAmount,
                    onValueChange = onDepositAmountChange,
                    label = "Размер залога"
                )
            }
        }
    }
}

@Composable
private fun CreateListingBottomBar(
    state: CreateListingUiState,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onPublishClick: () -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!state.isFirstStep) {
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = "Назад")
                }
            }

            RentPrimaryButton(
                text = if (state.isLastStep) "Опубликовать" else "Далее",
                onClick = if (state.isLastStep) onPublishClick else onNextClick,
                enabled = state.canGoNext && !state.isLoading,
                modifier = Modifier.weight(if (state.isFirstStep) 1f else 1.4f)
            )
        }
    }
}

@Preview(
    name = "CreateListing - Photos",
    showBackground = true
)
@Composable
private fun CreateListingPhotosPreview() {
    RentPlatformTheme{
       CreateListingScreen(
           state = CreateListingPreviewData.photos,
           onBackClick = {},
           onNextClick = {},
           onPublishClick = {},
           onAddPhotoClick = {},
           onRemovePhotoClick = {},
           onTitleChange = {},
           onDescriptionChange = {},
           onPricePerDayChange = {},
           onPricePerHourChange = {},
           onCategorySelected = {},
           onDepositEnabledChange = {},
           onDepositAmountChange = {},
           onCityChange = {},
           onPickupLocationChange = {}
       )
   }
}

@Preview(
    name = "CreateListing - Description",
    showBackground = true
)
@Composable
private fun CreateListingDescriptionPreview() {
    RentPlatformTheme {
        CreateListingScreen(
            state = CreateListingPreviewData.description,
            onBackClick = {},
            onNextClick = {},
            onPublishClick = {},
            onAddPhotoClick = {},
            onRemovePhotoClick = {},
            onTitleChange = {},
            onDescriptionChange = {},
            onPricePerDayChange = {},
            onCategorySelected = {},
            onDepositEnabledChange = {},
            onPricePerHourChange = {},
            onDepositAmountChange = {},
            onCityChange = {},
            onPickupLocationChange = {}
        )
    }
}

@Preview(
    name = "CreateListing - Terms",
    showBackground = true
)
@Composable
private fun CreateListingTermsPreview() {
    RentPlatformTheme {
        CreateListingScreen(
            state = CreateListingPreviewData.terms,
            onBackClick = {},
            onNextClick = {},
            onPublishClick = {},
            onAddPhotoClick = {},
            onRemovePhotoClick = {},
            onTitleChange = {},
            onDescriptionChange = {},
            onPricePerDayChange = {},
            onPricePerHourChange = {},
            onCategorySelected = {},
            onDepositEnabledChange = {},
            onDepositAmountChange = {},
            onCityChange = {},
            onPickupLocationChange = {}
        )
    }
}

private object CreateListingPreviewData {
    private val photosList = listOf(
        ListingPhotoUi(
            id = "1",
            sortOrder = 0
        ),
        ListingPhotoUi(
            id = "2",
            sortOrder = 1
        )
    )

    val photos = CreateListingUiState(
        currentStep = ListingStep.Photos,
        photos = photosList
    )

    val description = CreateListingUiState(
        currentStep = ListingStep.Description,
        photos = photosList,
        title = "Шуруповёрт Bosch",
        description = "Мощный шуруповёрт в хорошем состоянии. В комплекте кейс, зарядка и два аккумулятора.",
        selectedCategory = ListingCategory.Tools
    )

    val terms = CreateListingUiState(
        currentStep = ListingStep.Terms,
        photos = photosList,
        title = "Шуруповёрт Bosch",
        description = "Мощный шуруповёрт в хорошем состоянии.",
        selectedCategory = ListingCategory.Tools,
        pricePerDay = "700",
        pricePerHour = "150",
        hasDeposit = true,
        depositAmount = "3000",
        city = "Москва",
        pickupLocation = "м. Алексеевская"
    )

    val termsWithoutDeposit = terms.copy(
        hasDeposit = false,
        depositAmount = ""
    )
}