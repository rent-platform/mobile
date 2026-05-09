package com.example.marketplace.presentation.rentrequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace.domain.repository.CatalogRepository
import com.example.marketplace.presentation.components.RentCalendarDayUi
import com.example.marketplace.presentation.mapper.toDrawableRes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class RentRequestViewModel(private val catalogRepository: CatalogRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RentRequestUiState(isLoading = false))
    val uiState: StateFlow<RentRequestUiState> = _uiState.asStateFlow()

    private val _actions = Channel<RentRequestAction>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            _uiState.value = RentRequestUiState(isLoading = true)

            runCatching {
                catalogRepository.getItemDetails(itemId)
            }.onSuccess { item ->
                _uiState.value = item?.let {
                    RentRequestUiState(
                        isLoading = false,
                        itemId = it.id,
                        ownerId = it.ownerId,
                        title = it.title,
                        imageResId = it.imageKeys
                            .firstOrNull()
                            ?.toDrawableRes(),
                        city = it.city,
                        pickupLocation = it.pickupLocation,
                        pricePerDay = it.pricePerDay,
                        pricePerHour = it.pricePerHour,
                        depositAmount = it.depositAmount,
                        startDateInput = "",
                        endDateInput = "",
                        availability = it.availability.map { day ->
                            RentCalendarDayUi(
                                date = day.date,
                                isAvailable = day.isAvailable
                            )
                        }
                    )
                } ?: RentRequestUiState(
                    isLoading = false,
                    errorMessage = "Товар не найден"
                )
            }.onFailure { error ->
                error.printStackTrace()

                _uiState.value = RentRequestUiState(
                    isLoading = false,
                    errorMessage = "Не удалось загрузить товар"
                )
            }
        }
    }

    fun onEvent(event: RentRequestEvent) {
        when (event) {
            RentRequestEvent.OnBackClick -> {
                sendAction(RentRequestAction.NavigateBack)
            }

            is RentRequestEvent.OnStartDateInputChange -> {
                onStartDateInputChange(event.value)
            }

            is RentRequestEvent.OnDateClick -> {
                selectDate(event.date)
            }

            is RentRequestEvent.OnEndDateInputChange -> {
                onEndDateInputChange(event.value)
            }

            RentRequestEvent.OnSubmitClick -> {
                val state = _uiState.value
                val startDate = state.selectedStartDate
                val endDate = state.selectedEndDate

                if (
                    state.itemId.isNotBlank() &&
                    state.ownerId.isNotBlank() &&
                    startDate != null &&
                    endDate != null
                ) {
                    val hasUnavailableDate = hasUnavailableDateInRange(
                        startDate = startDate,
                        endDate = endDate,
                        availability = state.availability
                    )
                    if (hasUnavailableDate) {
                        _uiState.update {
                            it.copy(
                                errorMessage = "Выбранный период содержит занятую дату"
                            )
                        }
                        return
                    }

                    sendAction(
                        RentRequestAction.SubmitRentRequest(
                            itemId = state.itemId,
                            ownerId = state.ownerId,
                            startDate = startDate,
                            endDate = endDate
                        )
                    )
                }
            }
        }
    }

    private fun selectDate(date: String) {
        _uiState.update { state ->
            val clickedDay = state.availability.firstOrNull { day ->
                day.date == date
            }

            if (clickedDay?.isAvailable == false) {
                return@update state.copy(
                    errorMessage = "Эта дата уже занята"
                )
            }

            when {
                state.selectedStartDate == null -> {
                    state.copy(
                        selectedStartDate = date,
                        selectedEndDate = null,
                        startDateInput = date,
                        endDateInput = "",
                        errorMessage = null
                    )
                }

                state.selectedEndDate == null -> {
                    val startDate = state.selectedStartDate
                    val start = parseDate(startDate)?.time
                    val clicked = parseDate(date)?.time

                    if (start == null || clicked == null) {
                        state.copy(
                            errorMessage = "Не удалось обработать выбранную дату"
                        )
                    } else if (clicked < start) {
                        state.copy(
                            selectedStartDate = date,
                            selectedEndDate = null,
                            startDateInput = date,
                            endDateInput = "",
                            errorMessage = null
                        )
                    } else {
                        val hasUnavailableDate = hasUnavailableDateInRange(
                            startDate = startDate,
                            endDate = date,
                            availability = state.availability
                        )

                        if (hasUnavailableDate) {
                            state.copy(
                                selectedEndDate = null,
                                endDateInput = "",
                                errorMessage = "Выбранный период содержит занятую дату"
                            )
                        } else {
                            state.copy(
                                selectedEndDate = date,
                                endDateInput = date,
                                errorMessage = null
                            )
                        }
                    }
                }

                else -> {
                    state.copy(
                        selectedStartDate = date,
                        selectedEndDate = null,
                        startDateInput = date,
                        endDateInput = "",
                        errorMessage = null
                    )
                }
            }
        }
    }

    private fun onStartDateInputChange(value: String) {
        val normalizedValue = formatDateInput(value)

        _uiState.update { state ->
            if (normalizedValue.isBlank()) {
                return@update state.copy(
                    startDateInput = "",
                    selectedStartDate = null,
                    selectedEndDate = null,
                    endDateInput = "",
                    errorMessage = null
                )
            }

            if (!isPotentialDateInput(normalizedValue)) {
                return@update state.copy(
                    startDateInput = normalizedValue,
                    errorMessage = "Введите дату в формате yyyy-MM-dd"
                )
            }

            if (normalizedValue.length < 10) {
                return@update state.copy(
                    startDateInput = normalizedValue,
                    selectedStartDate = null,
                    selectedEndDate = null,
                    errorMessage = null
                )
            }

            val validationError = validateSingleDate(
                date = normalizedValue,
                availability = state.availability
            )

            if (validationError != null) {
                return@update state.copy(
                    startDateInput = normalizedValue,
                    selectedStartDate = null,
                    selectedEndDate = null,
                    errorMessage = validationError
                )
            }

            val endDate = state.selectedEndDate

            if (endDate != null) {
                val rangeError = validateRange(
                    startDate = normalizedValue,
                    endDate = endDate,
                    availability = state.availability
                )

                if (rangeError != null) {
                    return@update state.copy(
                        startDateInput = normalizedValue,
                        selectedStartDate = normalizedValue,
                        selectedEndDate = null,
                        endDateInput = "",
                        errorMessage = rangeError
                    )
                }
            }

            state.copy(
                startDateInput = normalizedValue,
                selectedStartDate = normalizedValue,
                errorMessage = null
            )
        }
    }

    private fun onEndDateInputChange(value: String) {
        val normalizedValue = formatDateInput(value)

        _uiState.update { state ->
            if (normalizedValue.isBlank()) {
                return@update state.copy(
                    endDateInput = "",
                    selectedEndDate = null,
                    errorMessage = null
                )
            }

            if (!isPotentialDateInput(normalizedValue)) {
                return@update state.copy(
                    endDateInput = normalizedValue,
                    errorMessage = "Введите дату в формате yyyy-MM-dd"
                )
            }

            if (normalizedValue.length < 10) {
                return@update state.copy(
                    endDateInput = normalizedValue,
                    selectedEndDate = null,
                    errorMessage = null
                )
            }

            val startDate = state.selectedStartDate

            if (startDate == null) {
                return@update state.copy(
                    endDateInput = normalizedValue,
                    selectedEndDate = null,
                    errorMessage = "Сначала выберите дату начала аренды"
                )
            }

            val validationError = validateSingleDate(
                date = normalizedValue,
                availability = state.availability
            )

            if (validationError != null) {
                return@update state.copy(
                    endDateInput = normalizedValue,
                    selectedEndDate = null,
                    errorMessage = validationError
                )
            }

            val rangeError = validateRange(
                startDate = startDate,
                endDate = normalizedValue,
                availability = state.availability
            )

            if (rangeError != null) {
                return@update state.copy(
                    endDateInput = normalizedValue,
                    selectedEndDate = null,
                    errorMessage = rangeError
                )
            }

            state.copy(
                endDateInput = normalizedValue,
                selectedEndDate = normalizedValue,
                errorMessage = null
            )
        }
    }


    private fun sendAction(action: RentRequestAction) {
        viewModelScope.launch {
            _actions.send(action)
        }
    }

    private fun parseDate(date: String): java.util.Date? {
        return runCatching {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
        }.getOrNull()
    }

    private fun validateSingleDate(
        date: String,
        availability: List<RentCalendarDayUi>
    ): String? {
        if (parseDate(date) == null) {
            return "Введите корректную дату в формате yyyy-MM-dd"
        }

        val day = availability.firstOrNull { it.date == date }
            ?: return "Эта дата недоступна для выбора"

        if (!day.isAvailable) {
            return "Эта дата уже занята"
        }

        return null
    }

    private fun validateRange(
        startDate: String,
        endDate: String,
        availability: List<RentCalendarDayUi>
    ): String? {
        val startTime = parseDate(startDate)?.time
            ?: return "Введите корректную дату начала"

        val endTime = parseDate(endDate)?.time
            ?: return "Введите корректную дату окончания"

        if (endTime < startTime) {
            return "Дата окончания не может быть раньше даты начала"
        }

        val hasUnavailableDate = hasUnavailableDateInRange(
            startDate = startDate,
            endDate = endDate,
            availability = availability
        )

        if (hasUnavailableDate) {
            return "Выбранный период содержит занятую дату"
        }

        return null
    }

    private fun formatDateInput(value: String): String {
        val digits = value
            .filter { char -> char.isDigit() }
            .take(8)

        return buildString {
            digits.forEachIndexed { index, char ->
                if (index == 4 || index == 6) {
                    append('-')
                }

                append(char)
            }
        }
    }

    private fun hasUnavailableDateInRange(
        startDate: String,
        endDate: String,
        availability: List<RentCalendarDayUi>
    ): Boolean {
        val startTime = parseDate(startDate)?.time ?: return true
        val endTime = parseDate(endDate)?.time ?: return true

        return availability.any { day ->
            val dayTime = parseDate(day.date)?.time ?: return@any false

            dayTime in startTime..endTime && !day.isAvailable
        }
    }

    private fun isPotentialDateInput(value: String): Boolean {
        return value.all { char ->
            char.isDigit() || char == '-'
        }
    }
}