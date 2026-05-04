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
data object MarketplaceSearchFiltersDestination