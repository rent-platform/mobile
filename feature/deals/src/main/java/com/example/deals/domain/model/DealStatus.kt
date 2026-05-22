package com.example.deals.domain.model

enum class DealStatus(val title: String) {
    PENDING("Ожидает подтверждения"),
    CONFIRMED("Подтверждена"),
    PAYMENT_PENDING("Ожидает оплаты"),
    ACTIVE("В аренде"),
    COMPLETED("Завершена"),
    REJECTED("Отклонена"),
    CANCELLED("Отменена")
}