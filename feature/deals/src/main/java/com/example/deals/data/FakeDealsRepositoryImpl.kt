package com.example.deals.data

import com.example.core.demo.DemoChats
import com.example.core.demo.DemoIds
import com.example.core.demo.DemoScenario
import com.example.core.demo.model.DemoDealStatus
import com.example.core.ui.toDemoDrawableRes
import com.example.deals.domain.DealsRepository
import com.example.deals.domain.model.DealDetails
import com.example.deals.domain.model.DealItemDetails
import com.example.deals.domain.model.DealRole
import com.example.deals.domain.model.DealStatus
import com.example.deals.presentation.DealListItemUi
import com.example.deals.presentation.toDealListItemUi
import kotlinx.coroutines.delay

class FakeDealsRepositoryImpl : DealsRepository {

    override suspend fun getRenterDeals(): List<DealListItemUi> {
        delay(300)

        return DemoScenario.myRenterDeals
            .sortedByDescending { it.createdAt }
            .map { deal -> deal.toDealListItemUi() }
    }

    override suspend fun getOwnerDeals(): List<DealListItemUi> {
        delay(300)

        return DemoScenario.myOwnerDeals
            .sortedByDescending { it.createdAt }
            .map { deal -> deal.toDealListItemUi() }
    }

    override suspend fun getDealDetails(dealId: String): DealDetails {
        delay(200)

        val deal = DemoScenario.findDealById(dealId)
            ?: error("Сделка не найдена")

        val item = DemoScenario.findItemById(deal.itemId)
            ?: error("Товар сделки не найден")

        val chatId = DemoChats.all
            .firstOrNull { chat -> chat.dealId == deal.id }
            ?.id

        val role = if (deal.ownerId == DemoIds.CURRENT_USER_ID) {
            DealRole.Owner
        } else {
            DealRole.Renter
        }

        val counterpartyId = when (role) {
            DealRole.Owner -> deal.renterId
            DealRole.Renter -> deal.ownerId
        }

        val counterparty = DemoScenario.findUserById(counterpartyId)

        return DealDetails(
            id = deal.id,
            item = DealItemDetails(
                id = item.id,
                title = item.title,
                description = item.description,
                imageResId = item.imageKey.toDemoDrawableRes(),
                city = item.city,
                pickupLocation = item.pickupLocation,
                pricePerDay = item.pricePerDay,
                pricePerHour = item.pricePerHour,
                depositAmount = item.depositAmount
            ),
            status = deal.status.toDomainStatus(),
            role = role,
            renterId = deal.renterId,
            ownerId = deal.ownerId,
            counterpartyName = counterparty?.nickname ?: "Пользователь",
            counterpartyAvatarResId = null,
            startDate = deal.startDate,
            endDate = deal.endDate,
            totalPrice = deal.totalPrice,
            depositAmount = deal.depositAmount,
            rejectionReason = deal.rejectionReason,
            chatId = chatId,
            startConfirmedByMe = deal.status == DemoDealStatus.ACTIVE || deal.status == DemoDealStatus.COMPLETED,
            startConfirmedByOther = deal.status == DemoDealStatus.ACTIVE || deal.status == DemoDealStatus.COMPLETED,
            completeConfirmedByMe = deal.status == DemoDealStatus.COMPLETED,
            completeConfirmedByOther = deal.status == DemoDealStatus.COMPLETED,
            reviewLeftByMe = false,
            isPaymentPaid = deal.status == DemoDealStatus.ACTIVE || deal.status == DemoDealStatus.COMPLETED,
        )
    }
}

private fun DemoDealStatus.toDomainStatus(): DealStatus {
    return when (this) {
        DemoDealStatus.PENDING -> DealStatus.PENDING
        DemoDealStatus.CONFIRMED -> DealStatus.CONFIRMED
        DemoDealStatus.PAYMENT_PENDING -> DealStatus.PAYMENT_PENDING
        DemoDealStatus.ACTIVE -> DealStatus.ACTIVE
        DemoDealStatus.COMPLETED -> DealStatus.COMPLETED
        DemoDealStatus.REJECTED -> DealStatus.REJECTED
        DemoDealStatus.CANCELLED -> DealStatus.CANCELLED
    }
}