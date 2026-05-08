package com.example.marketplace.data.repository

import com.example.core.demo.DemoScenario
import com.example.core.demo.model.DemoAvailabilityDay
import com.example.core.demo.model.DemoItem
import com.example.core.demo.model.DemoItemStatus
import com.example.marketplace.domain.model.CatalogCategory
import com.example.marketplace.domain.model.CatalogData
import com.example.marketplace.domain.model.CatalogItem
import com.example.marketplace.domain.model.ItemAvailabilityDay
import com.example.marketplace.domain.model.ItemDetails
import com.example.marketplace.domain.repository.CatalogRepository
import kotlinx.coroutines.delay

class FakeCatalogRepositoryImpl : CatalogRepository {

    private val favoriteItemIds = DemoScenario.items
        .filter { item -> item.isFavorite }
        .map { item -> item.id }
        .toMutableSet()

    override suspend fun getCatalog(): CatalogData {
        delay(250)

        return CatalogData(
            categories = DemoScenario.categories
                .filter { category -> category.isActive }
                .sortedBy { category -> category.sortOrder }
                .map { category ->
                    CatalogCategory(
                        id = category.id,
                        name = category.name
                    )
                },
            recommendedItems = DemoScenario.items
                .filter { item -> item.status == DemoItemStatus.ACTIVE }
                .map { item -> item.toCatalogItem() }
        )
    }

    override suspend fun getItemDetails(itemId: String): ItemDetails? {
        delay(250)

        val item = DemoScenario.findItemById(itemId) ?: return null
        val owner = DemoScenario.findUserById(item.ownerId)

        return ItemDetails(
            id = item.id,
            categoryId = item.categoryId,
            title = item.title,
            description = item.description,
            pricePerDay = item.pricePerDay,
            pricePerHour = item.pricePerHour,
            depositAmount = item.depositAmount,
            city = item.city,
            pickupLocation = item.pickupLocation,
            ownerId = item.ownerId,
            ownerName = owner?.fullName ?: owner?.nickname ?: "Пользователь",
            ownerRating = owner?.rating ?: 0f,
            reviewsCount = owner?.reviewsCount ?: 0,
            createdAt = item.createdAt,
            imageKeys = item.photoKeys.ifEmpty { listOf(item.imageKey) },
            imageUrls = emptyList(),
            availability = item.availability.map { day -> day.toDomain() },
            isFavorite = favoriteItemIds.contains(item.id),
            similarItems = DemoScenario.similarItemsFor(
                itemId = item.id,
                limit = 8
            ).map { similarItem ->
                similarItem.toCatalogItem()
            }
        )
    }

    override suspend fun toggleFavorite(itemId: String): Boolean {
        delay(100)

        return if (favoriteItemIds.contains(itemId)) {
            favoriteItemIds.remove(itemId)
            false
        } else {
            favoriteItemIds.add(itemId)
            true
        }
    }

    private fun DemoItem.toCatalogItem(): CatalogItem {
        return CatalogItem(
            id = id,
            title = title,
            pricePerDay = pricePerDay,
            pricePerHour = pricePerHour,
            location = city,
            imageKey = imageKey,
            imageUrl = null,
            isFavorite = favoriteItemIds.contains(id)
        )
    }

    private fun DemoAvailabilityDay.toDomain(): ItemAvailabilityDay {
        return ItemAvailabilityDay(
            date = date,
            isAvailable = isAvailable
        )
    }
}