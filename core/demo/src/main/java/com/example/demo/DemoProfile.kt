package com.example.core.demo

import com.example.core.demo.model.DemoItemStatus
import com.example.core.demo.model.DemoProfileStats

object DemoProfile {

    val stats = DemoProfileStats(
        activeItemsCount = DemoItems.all.count {
            it.ownerId == DemoIds.CURRENT_USER_ID &&
                    it.status == DemoItemStatus.ACTIVE
        },
        draftItemsCount = DemoItems.all.count {
            it.ownerId == DemoIds.CURRENT_USER_ID &&
                    it.status == DemoItemStatus.DRAFT
        },
        moderationItemsCount = DemoItems.all.count {
            it.ownerId == DemoIds.CURRENT_USER_ID &&
                    it.status == DemoItemStatus.MODERATION
        },
        rejectedItemsCount = DemoItems.all.count {
            it.ownerId == DemoIds.CURRENT_USER_ID &&
                    it.status == DemoItemStatus.REJECTED
        },
        archivedItemsCount = DemoItems.all.count {
            it.ownerId == DemoIds.CURRENT_USER_ID &&
                    it.status == DemoItemStatus.ARCHIVED
        },
        rentedOutCount = DemoDeals.all.count {
            it.ownerId == DemoIds.CURRENT_USER_ID
        },
        rentedCount = DemoDeals.all.count {
            it.renterId == DemoIds.CURRENT_USER_ID
        },
        rentalHistoryCount = DemoDeals.all.count {
            it.ownerId == DemoIds.CURRENT_USER_ID ||
                    it.renterId == DemoIds.CURRENT_USER_ID
        }
    )
}