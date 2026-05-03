package com.example.marketplace.presentation.rentrequest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.marketplace.presentation.components.RentAvailabilityCalendar
import com.example.marketplace.presentation.components.RentCalendarDayUi
import com.example.ui.components.RentPrimaryButton
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentRequestScreen(
    uiState: RentRequestUiState,
    onEvent: (RentRequestEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Оформление аренды",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(RentRequestEvent.OnBackClick) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            RentRequestBottomBar(
                isEnabled = uiState.selectedStartDate != null &&
                        uiState.selectedEndDate != null,
                onSubmitClick = {
                    onEvent(RentRequestEvent.OnSubmitClick)
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding()),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = innerPadding.calculateBottomPadding() + 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        RentItemSummaryBlock(
                            title = uiState.title,
                            imageResId = uiState.imageResId,
                            city = uiState.city,
                            pickupLocation = uiState.pickupLocation,
                            pricePerDay = uiState.pricePerDay,
                            pricePerHour = uiState.pricePerHour
                        )
                    }

                    item {
                        RentDateSelectionBlock(
                            availability = uiState.availability,
                            selectedStartDate = uiState.selectedStartDate,
                            selectedEndDate = uiState.selectedEndDate,
                            startDateInput = uiState.startDateInput,
                            endDateInput = uiState.endDateInput,
                            errorMessage = uiState.errorMessage,
                            onDateClick = { date ->
                                onEvent(RentRequestEvent.OnDateClick(date))
                            },
                            onStartDateInputChange = { value ->
                                onEvent(RentRequestEvent.OnStartDateInputChange(value))
                            },
                            onEndDateInputChange = { value ->
                                onEvent(RentRequestEvent.OnEndDateInputChange(value))
                            }
                        )
                    }

                    item {
                        RentPriceSummaryBlock(
                            pricePerDay = uiState.pricePerDay,
                            depositAmount = uiState.depositAmount,
                            selectedStartDate = uiState.selectedStartDate,
                            selectedEndDate = uiState.selectedEndDate
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RentItemSummaryBlock(
    title: String,
    imageResId: Int?,
    city: String,
    pickupLocation: String?,
    pricePerDay: Long?,
    pricePerHour: Long?,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (imageResId != null) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "Фото",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )

                if (city.isNotBlank()) {
                    Text(
                        text = buildString {
                            append(city)

                            if (!pickupLocation.isNullOrBlank()) {
                                append(", ")
                                append(pickupLocation)
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }

                pricePerDay?.let { price ->
                    Text(
                        text = "${formatMoney(price)} ₽ / день",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                pricePerHour?.let { price ->
                    Text(
                        text = "${formatMoney(price)} ₽ / час",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun RentDateSelectionBlock(
    availability: List<RentCalendarDayUi>,
    selectedStartDate: String?,
    selectedEndDate: String?,
    startDateInput: String,
    endDateInput: String,
    errorMessage: String?,
    onDateClick: (String) -> Unit,
    onStartDateInputChange: (String) -> Unit,
    onEndDateInputChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Выберите даты аренды",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = buildSelectedPeriodText(
                        startDate = selectedStartDate,
                        endDate = selectedEndDate
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RentDateTextField(
                        value = startDateInput,
                        onValueChange = onStartDateInputChange,
                        label = "Начало",
                        modifier = Modifier.weight(1f)
                    )

                    RentDateTextField(
                        value = endDateInput,
                        onValueChange = onEndDateInputChange,
                        label = "Окончание",
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(
                    text = "Формат: 2026-05-02",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        RentAvailabilityCalendar(
            availability = availability,
            selectedStartDate = selectedStartDate,
            selectedEndDate = selectedEndDate,
            onDateClick = onDateClick
        )
    }
}

@Composable
private fun RentDateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        )
    }

    LaunchedEffect(value) {
        if (value != textFieldValue.text) {
            textFieldValue = TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        }
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            onValueChange(newValue.text)
        },
        modifier = modifier,
        singleLine = true,
        label = {
            Text(text = label)
        },
        placeholder = {
            Text(text = "гггг-мм-дд")
        },
        shape = RoundedCornerShape(14.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}

@Composable
private fun RentPriceSummaryBlock(
    pricePerDay: Long?,
    depositAmount: Long,
    selectedStartDate: String?,
    selectedEndDate: String?,
    modifier: Modifier = Modifier
) {
    val daysCount = calculateRentalDays(
        startDate = selectedStartDate,
        endDate = selectedEndDate
    )

    val rentPrice = if (pricePerDay != null && daysCount != null) {
        pricePerDay * daysCount
    } else {
        null
    }

    val totalPrice = rentPrice?.plus(depositAmount)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Итого",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            SummaryRow(
                title = "Период",
                value = buildSelectedPeriodText(
                    startDate = selectedStartDate,
                    endDate = selectedEndDate
                )
            )

            daysCount?.let { count ->
                SummaryRow(
                    title = "Дней аренды",
                    value = count.toString()
                )
            }

            rentPrice?.let { price ->
                SummaryRow(
                    title = "Стоимость аренды",
                    value = "${formatMoney(price)} ₽"
                )
            }

            if (depositAmount > 0) {
                SummaryRow(
                    title = "Залог",
                    value = "${formatMoney(depositAmount)} ₽"
                )
            }

            totalPrice?.let { total ->
                SummaryRow(
                    title = "Всего",
                    value = "${formatMoney(total)} ₽",
                    isBold = true
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(
    title: String,
    value: String,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun RentRequestBottomBar(
    isEnabled: Boolean,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        RentPrimaryButton(
            text = "Отправить запрос",
            onClick = {
                if (isEnabled) {
                    onSubmitClick()
                }
            },
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .alpha(if (isEnabled) 1f else 0.55f)
        )
    }
}

private fun buildSelectedPeriodText(
    startDate: String?,
    endDate: String?
): String {
    return when {
        startDate == null -> "Выберите дату начала аренды"
        endDate == null -> "Выберите дату окончания аренды"
        else -> "${formatDate(startDate, "d MMMM")} — ${formatDate(endDate, "d MMMM")}"
    }
}

private fun calculateRentalDays(
    startDate: String?,
    endDate: String?
): Long? {
    if (startDate == null || endDate == null) return null

    val start = parseDate(startDate)?.time ?: return null
    val end = parseDate(endDate)?.time ?: return null

    val millisInDay = 24L * 60L * 60L * 1000L

    return ((end - start) / millisInDay) + 1
}

private fun formatDate(
    date: String,
    pattern: String
): String {
    return runCatching {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat(pattern, Locale("ru"))

        val parsedDate = inputFormat.parse(date)
        parsedDate?.let(outputFormat::format).orEmpty()
    }.getOrDefault(date)
}

private fun parseDate(date: String): java.util.Date? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    return runCatching {
        inputFormat.parse(date)
    }.getOrNull()
}

private fun formatMoney(value: Long): String {
    return "%,d".format(value).replace(',', ' ')
}