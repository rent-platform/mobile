package com.example.deals.presentation

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
        get() = !isLoading && errorMessage == null && currentDeals.isEmpty()
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

enum class DealRole(
    val title: String
) {
    Owner("Я сдаю"),
    Renter("Я арендую")
}

enum class DealStatus(
    val title: String
) {
    Pending("Ожидает"),
    Confirmed("Подтверждена"),
    Active("В аренде"),
    Completed("Завершена"),
    Rejected("Отклонена"),
    Cancelled("Отменена")
}

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
    Active(
        title = "Активные",
        statuses = setOf(DealStatus.Active)
    ),
    Confirmed(
        title = "Подтверждённые",
        statuses = setOf(DealStatus.Confirmed)
    ),
    Completed(
        title = "Завершённые",
        statuses = setOf(DealStatus.Completed)
    ),
    Rejected(
        title = "Отклонённые",
        statuses = setOf(DealStatus.Rejected, DealStatus.Cancelled)
    )
}