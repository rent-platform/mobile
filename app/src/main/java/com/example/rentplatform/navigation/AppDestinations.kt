package com.example.rentplatform.navigation

import kotlinx.serialization.Serializable

@Serializable
object CatalogDestination

@Serializable
object AuthorizationDestination

@Serializable
object RegistrationDestination

@Serializable
data class ItemDetailsDestination(
    val itemId: String
)

@Serializable
object MainShellDestination

@Serializable
object ProfileEntryDestination

@Serializable
object EditProfileDestination

@Serializable
object ProfileSettingDestination

@Serializable
object ChangePasswordDestination

@Serializable
data class RentRequestDestination(
    val itemId: String
)

@Serializable
object FavoritesEntryDestination

@Serializable
object DealsEntryDestination

@Serializable
object MarketplaceSearchInputDestination

@Serializable
data class MarketplaceSearchResultsDestination(
    val query: String
)

@Serializable
data class MarketplaceSearchFiltersDestination(
    val source: MarketplaceSearchFiltersSource
)

@Serializable
enum class MarketplaceSearchFiltersSource {
    CATALOG,
    SEARCH_RESULTS
}

@Serializable
object CreateListingDestination

@Serializable
object ChatEntryDestination

@Serializable
data class ChatDetailsDestination(
    val chatId: String
)

@Serializable
data class DealDetailsDestination(
    val dealId: String
)