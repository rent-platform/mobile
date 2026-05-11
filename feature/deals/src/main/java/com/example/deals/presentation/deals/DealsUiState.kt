package com.example.deals.presentation

import com.example.deals.domain.model.DealRole
import com.example.deals.domain.model.DealStatus

data class DealsUiState(
    val selectedRole: DealRole = DealRole.Renter,
    val selectedFilter: DealFilter = DealFilter.All,
    val renterDeals: List<DealListItemUi> = emptyList(),
    val ownerDeals: List<DealListItemUi> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val currentDeals: List<DealListItemUi>
        get() = when (selectedRole) {
            DealRole.Renter -> renterDeals
            DealRole.Owner -> ownerDeals
        }

    val filteredDeals: List<DealListItemUi>
        get() {
            if (selectedFilter == DealFilter.All) return currentDeals

            return currentDeals.filter { deal ->
                deal.status in selectedFilter.statuses
            }
        }

    val currentDealsCount: Int
        get() = currentDeals.size

    val description: String
        get() = when (selectedRole) {
            DealRole.Owner -> "Вещи, которые вы сдаёте в аренду: $currentDealsCount сделок"
            DealRole.Renter -> "Вещи, которые вы берёте у других: $currentDealsCount сделок"
        }

    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && filteredDeals.isEmpty()
}
data class DealListItemUi(
    val id: String,
    val itemId: String,
    val title: String,
    val dateRange: String,
    val totalPrice: String,
    val depositAmount: String?,
    val status: DealStatus,
    val pricingMode: DealPricingMode,
    val imageUrl: String? = null,
    val imageResId: Int? = null
)

enum class DealPricingMode(
    val title: String
) {
    Hour("Почасовая"),
    Day("Посуточная")
}

enum class DealFilter(
    val title: String,
    val statuses: Set<DealStatus>
) {
    All(
        title = "Все",
        statuses = emptySet()
    ),

    Requests(
        title = "Заявки",
        statuses = setOf(
            DealStatus.PENDING
        )
    ),

    Payment(
        title = "Оплата",
        statuses = setOf(
            DealStatus.CONFIRMED,
            DealStatus.PAYMENT_PENDING
        )
    ),

    Active(
        title = "В аренде",
        statuses = setOf(
            DealStatus.ACTIVE
        )
    ),

    Completed(
        title = "Завершённые",
        statuses = setOf(
            DealStatus.COMPLETED
        )
    ),

    Closed(
        title = "Закрытые",
        statuses = setOf(
            DealStatus.REJECTED,
            DealStatus.CANCELLED
        )
    )
}