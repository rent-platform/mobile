package com.example.deals.presentation.details

import androidx.compose.runtime.Immutable
import com.example.deals.domain.model.DealDetails

@Immutable
data class DealDetailsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val details: DealDetails? = null
)

enum class DealDetailsActionUi(val title: String, val type: DealDetailsActionType)
{
    CONFIRM_REQUEST(title = "Подтвердить заявку", type = DealDetailsActionType.PRIMARY),
    REJECT_REQUEST(title = "Отклонить заявку", type = DealDetailsActionType.DANGER),
    CREATE_PAYMENT(title = "Создать оплату", type = DealDetailsActionType.PRIMARY),
    PAY(title = "Оплатить", type = DealDetailsActionType.PRIMARY),
    CONFIRM_START(title = "Подтвердить передачу", type = DealDetailsActionType.PRIMARY),
    CONFIRM_COMPLETE(title = "Подтвердить возврат", type = DealDetailsActionType.PRIMARY),
    LEAVE_REVIEW(title = "Оставить отзыв", type = DealDetailsActionType.SECONDARY),
    CANCEL(title = "Отменить сделку", type = DealDetailsActionType.DANGER)
}

enum class DealDetailsActionType {
    PRIMARY,
    SECONDARY,
    DANGER
}