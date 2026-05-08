package com.example.core.demo

import com.example.core.demo.model.DemoDeal
import com.example.core.demo.model.DemoDealStatus
import com.example.core.demo.model.DemoPricingMode

object DemoDeals {

    val all = listOf(
        DemoDeal(
            id = DemoIds.DEAL_CANON_ID,
            itemId = DemoIds.ITEM_CANON_ID,
            renterId = DemoIds.CURRENT_USER_ID,
            ownerId = DemoIds.OWNER_MARIA_ID,
            startDate = "2026-04-23T10:00:00.000Z",
            endDate = "2026-04-25T18:00:00.000Z",
            pricingMode = DemoPricingMode.DAY,
            pricePerDaySnapshot = 5000,
            pricePerHourSnapshot = null,
            totalPrice = 15000,
            depositAmount = 15000,
            status = DemoDealStatus.CONFIRMED,
            rejectionReason = null,
            createdAt = "2026-04-22T16:30:00.000Z",
            updatedAt = "2026-04-22T17:20:00.000Z"
        ),
        DemoDeal(
            id = DemoIds.DEAL_DRILL_ID,
            itemId = DemoIds.ITEM_DRILL_ID,
            renterId = DemoIds.CURRENT_USER_ID,
            ownerId = DemoIds.OWNER_DMITRY_ID,
            startDate = "2026-04-24T18:00:00.000Z",
            endDate = "2026-04-26T18:00:00.000Z",
            pricingMode = DemoPricingMode.DAY,
            pricePerDaySnapshot = 2000,
            pricePerHourSnapshot = null,
            totalPrice = 4000,
            depositAmount = 6000,
            status = DemoDealStatus.ACTIVE,
            rejectionReason = null,
            createdAt = "2026-04-23T12:00:00.000Z",
            updatedAt = "2026-04-24T18:00:00.000Z"
        ),
        DemoDeal(
            id = DemoIds.DEAL_TENT_ID,
            itemId = DemoIds.ITEM_TENT_ID,
            renterId = DemoIds.OWNER_MARIA_ID,
            ownerId = DemoIds.CURRENT_USER_ID,
            startDate = "2026-04-18T12:00:00.000Z",
            endDate = "2026-04-21T12:00:00.000Z",
            pricingMode = DemoPricingMode.DAY,
            pricePerDaySnapshot = 1500,
            pricePerHourSnapshot = null,
            totalPrice = 4500,
            depositAmount = 5000,
            status = DemoDealStatus.COMPLETED,
            rejectionReason = null,
            createdAt = "2026-04-17T09:00:00.000Z",
            updatedAt = "2026-04-21T18:00:00.000Z"
        ),
        DemoDeal(
            id = DemoIds.DEAL_MAKITA_ID,
            itemId = DemoIds.ITEM_MAKITA_ID,
            renterId = DemoIds.OWNER_IVAN_ID,
            ownerId = DemoIds.CURRENT_USER_ID,
            startDate = "2026-04-27T10:00:00.000Z",
            endDate = "2026-04-28T10:00:00.000Z",
            pricingMode = DemoPricingMode.DAY,
            pricePerDaySnapshot = 1200,
            pricePerHourSnapshot = null,
            totalPrice = 1200,
            depositAmount = 4000,
            status = DemoDealStatus.PENDING,
            rejectionReason = null,
            createdAt = "2026-04-26T10:42:00.000Z",
            updatedAt = "2026-04-26T10:42:00.000Z"
        )
    )
}