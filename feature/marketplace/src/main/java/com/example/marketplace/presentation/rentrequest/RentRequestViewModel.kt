package com.example.marketplace.presentation.rentrequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketplace.data.mock.ItemDetailsMockData
import com.example.marketplace.presentation.components.RentCalendarDayUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class RentRequestViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RentRequestUiState(isLoading = false))
    val uiState: StateFlow<RentRequestUiState> = _uiState.asStateFlow()

    private val _actions = Channel<RentRequestAction>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()

    fun loadItem(itemId: String) {
        val item = ItemDetailsMockData.getById(itemId)

        _uiState.value = item?.let {
            RentRequestUiState(
                isLoading = false,
                itemId = it.id,
                ownerId = it.ownerId,
                title = it.title,
                imageResId = it.imageResIds.firstOrNull(),
                city = it.city,
                pickupLocation = it.pickupLocation,
                pricePerDay = it.pricePerDay,
                pricePerHour = it.pricePerHour,
                depositAmount = it.depositAmount ?: 0L,
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
    }

    fun onEvent(event: RentRequestEvent) {
        when (event) {
            RentRequestEvent.OnBackClick -> {
                sendAction(RentRequestAction.NavigateBack)
            }

            is RentRequestEvent.OnDateClick -> {
                selectDate(event.date)
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
            when {
                state.selectedStartDate == null -> {
                    state.copy(
                        selectedStartDate = date,
                        selectedEndDate = null
                    )
                }

                state.selectedEndDate == null -> {
                    val start = parseDate(state.selectedStartDate)?.time
                    val clicked = parseDate(date)?.time

                    if (start != null && clicked != null && clicked < start) {
                        state.copy(
                            selectedStartDate = date,
                            selectedEndDate = null
                        )
                    } else {
                        state.copy(
                            selectedEndDate = date
                        )
                    }
                }

                else -> {
                    state.copy(
                        selectedStartDate = date,
                        selectedEndDate = null
                    )
                }
            }
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
}