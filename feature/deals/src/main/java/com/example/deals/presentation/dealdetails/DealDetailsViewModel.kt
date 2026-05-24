package com.example.deals.presentation.dealdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deals.domain.DealsRepository
import com.example.deals.domain.model.DealDetails
import com.example.deals.domain.model.DealStatus
import com.example.deals.presentation.details.DealDetailsActionUi
import com.example.deals.presentation.details.DealDetailsUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DealDetailsViewModel(
    private val dealId: String,
    private val dealsRepository: DealsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        DealDetailsUiState(isLoading = true)
    )
    val uiState = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<DealDetailsAction>()
    val actions = _actions.asSharedFlow()

    init {
        loadDealDetails()
    }

    fun onEvent(event: DealDetailsEvent) {
        when (event) {
            DealDetailsEvent.BackClicked -> {
                navigateBack()
            }

            DealDetailsEvent.RetryClicked -> {
                loadDealDetails()
            }

            is DealDetailsEvent.ChatClicked -> {
                navigateToChat(event.chatId)
            }

            is DealDetailsEvent.DealActionClicked -> {
                onDealActionClicked(event.action)
            }
        }
    }

    private fun loadDealDetails() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                dealsRepository.getDealDetails(dealId)
            }.onSuccess { details ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = null,
                        details = details
                    )
                }
            }.onFailure {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        errorMessage = "Не удалось загрузить сделку"
                    )
                }
            }
        }
    }

    private fun onDealActionClicked(action: DealDetailsActionUi) {
        val details = _uiState.value.details ?: return

        when (action) {
            DealDetailsActionUi.CONFIRM_REQUEST -> {
                updateDetails(
                    details.copy(
                        status = DealStatus.PAYMENT_PENDING,
                        rejectionReason = null
                    )
                )

                showMessage("Заявка подтверждена. Ссылка на оплату создана")
            }

            DealDetailsActionUi.REJECT_REQUEST -> {
                updateDetails(
                    details.copy(
                        status = DealStatus.REJECTED,
                        rejectionReason = "Заявка отклонена владельцем"
                    )
                )

                showMessage("Заявка отклонена")
            }

            DealDetailsActionUi.CREATE_PAYMENT -> {
                updateDetails(
                    details.copy(
                        status = DealStatus.PAYMENT_PENDING
                    )
                )

                showMessage("Ссылка на оплату создана")
            }

            DealDetailsActionUi.PAY -> {
                updateDetails(
                    details.copy(
                        status = DealStatus.PAID
                    )
                )
                showMessage("Оплата прошла")
            }

            DealDetailsActionUi.CONFIRM_START -> {
                val updatedDetails = if (details.startConfirmedByOther) {
                    details.copy(
                        status = DealStatus.ACTIVE,
                        startConfirmedByMe = true,
                        startConfirmedByOther = true
                    )
                } else {
                    details.copy(
                        startConfirmedByMe = true
                    )
                }

                updateDetails(updatedDetails)

                if (updatedDetails.status == DealStatus.ACTIVE) {
                    showMessage("Аренда началась")
                } else {
                    showMessage("Передача подтверждена. Ожидаем вторую сторону")
                }
            }

            DealDetailsActionUi.CONFIRM_COMPLETE -> {
                val updatedDetails = if (details.completeConfirmedByOther) {
                    details.copy(
                        status = DealStatus.COMPLETED,
                        completeConfirmedByMe = true,
                        completeConfirmedByOther = true
                    )
                } else {
                    details.copy(
                        completeConfirmedByMe = true
                    )
                }

                updateDetails(updatedDetails)

                if (updatedDetails.status == DealStatus.COMPLETED) {
                    showMessage("Аренда завершена")
                } else {
                    showMessage("Возврат подтверждён. Ожидаем вторую сторону")
                }
            }

            DealDetailsActionUi.LEAVE_REVIEW -> {
                navigateToReview(details.id)
            }

            DealDetailsActionUi.CANCEL -> {
                updateDetails(
                    details.copy(
                        status = DealStatus.CANCELLED,
                        rejectionReason = "Сделка отменена"
                    )
                )

                showMessage("Сделка отменена")
            }
        }
    }

    private fun updateDetails(details: DealDetails) {
        _uiState.update { state ->
            state.copy(details = details)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _actions.emit(DealDetailsAction.NavigateBack)
        }
    }

    private fun navigateToChat(chatId: String) {
        viewModelScope.launch {
            _actions.emit(
                DealDetailsAction.NavigateToChat(chatId)
            )
        }
    }

    private fun navigateToReview(dealId: String) {
        viewModelScope.launch {
            _actions.emit(
                DealDetailsAction.NavigateToReview(dealId)
            )
        }
    }

    private fun showMessage(message: String) {
        viewModelScope.launch {
            _actions.emit(
                DealDetailsAction.ShowMessage(message)
            )
        }
    }
}