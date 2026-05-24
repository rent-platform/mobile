package com.example.deals.presentation

import com.example.core.demo.DemoScenario
import com.example.core.demo.model.DemoDeal
import com.example.core.demo.model.DemoDealStatus
import com.example.core.demo.model.DemoPricingMode
import com.example.core.ui.toDemoDrawableRes
import com.example.deals.domain.model.DealStatus

fun DemoDeal.toDealListItemUi(): DealListItemUi {
    val item = DemoScenario.findItemById(itemId)

    return DealListItemUi(
        id = id,
        itemId = itemId,
        title = item?.title ?: "Объявление",
        dateRange = "${formatDemoDate(startDate)} — ${formatDemoDate(endDate)}",
        totalPrice = "${formatPrice(totalPrice)} ₽",
        depositAmount = if (depositAmount > 0) {
            "${formatPrice(depositAmount)} ₽"
        } else {
            null
        },
        status = status.toUi(),
        pricingMode = pricingMode.toUi(),
        imageUrl = null,
        imageResId = item?.imageKey?.toDemoDrawableRes()
    )
}

private fun DemoDealStatus.toUi(): DealStatus {
    return when (this) {
        DemoDealStatus.PENDING -> DealStatus.PENDING
        DemoDealStatus.CONFIRMED -> DealStatus.CONFIRMED
        DemoDealStatus.PAYMENT_PENDING -> DealStatus.PAYMENT_PENDING
        DemoDealStatus.PAID -> DealStatus.PAID
        DemoDealStatus.ACTIVE -> DealStatus.ACTIVE
        DemoDealStatus.COMPLETED -> DealStatus.COMPLETED
        DemoDealStatus.REJECTED -> DealStatus.REJECTED
        DemoDealStatus.CANCELLED -> DealStatus.CANCELLED
    }
}

private fun DemoPricingMode.toUi(): DealPricingMode {
    return when (this) {
        DemoPricingMode.HOUR -> DealPricingMode.Hour
        DemoPricingMode.DAY -> DealPricingMode.Day
    }
}

private fun formatPrice(value: Long): String {
    return value
        .toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()
}

private fun formatDemoDate(value: String): String {
    return when {
        value.startsWith("2026-04-17") -> "17 апр. 2026"
        value.startsWith("2026-04-18") -> "18 апр. 2026"
        value.startsWith("2026-04-21") -> "21 апр. 2026"
        value.startsWith("2026-04-22") -> "22 апр. 2026"
        value.startsWith("2026-04-23") -> "23 апр. 2026"
        value.startsWith("2026-04-24") -> "24 апр. 2026"
        value.startsWith("2026-04-25") -> "25 апр. 2026"
        value.startsWith("2026-04-26") -> "26 апр. 2026"
        value.startsWith("2026-04-27") -> "27 апр. 2026"
        value.startsWith("2026-04-28") -> "28 апр. 2026"
        value.startsWith("2026-04-29") -> "29 апр. 2026"
        else -> value.take(10)
    }
}
