package com.example.core.demo

import com.example.core.demo.model.DemoDealStatus
import com.example.core.demo.model.DemoItemStatus

object DemoScenario {

    val currentUser = DemoUsers.currentUser

    val users = DemoUsers.all
    val categories = DemoCategories.all
    val items = DemoItems.all
    val deals = DemoDeals.all
    val chats = DemoChats.all
    val profileStats = DemoProfile.stats

    val favoriteItems
        get() = items.filter { it.isFavorite }

    val activeItems
        get() = items.filter { it.status == DemoItemStatus.ACTIVE }

    val myItems
        get() = items.filter { it.ownerId == DemoIds.CURRENT_USER_ID }

    val myRenterDeals
        get() = deals.filter { it.renterId == DemoIds.CURRENT_USER_ID }

    val myOwnerDeals
        get() = deals.filter { it.ownerId == DemoIds.CURRENT_USER_ID }

    val renterChats
        get() = chats.filter { it.renterId == DemoIds.CURRENT_USER_ID }

    val ownerChats
        get() = chats.filter { it.ownerId == DemoIds.CURRENT_USER_ID }

    fun findUserById(id: String) = users.firstOrNull { it.id == id }

    fun findCategoryById(id: Long) = categories.firstOrNull { it.id == id }

    fun findItemById(id: String) = items.firstOrNull { it.id == id }

    fun findDealById(id: String) = deals.firstOrNull { it.id == id }

    fun findChatById(id: String) = chats.firstOrNull { it.id == id }

    fun similarItemsFor(itemId: String, limit: Int = 8) =
        items
            .filter { it.id != itemId }
            .filter { it.status == DemoItemStatus.ACTIVE }
            .take(limit)

    fun dealsByStatus(status: DemoDealStatus) =
        deals.filter { it.status == status }
}