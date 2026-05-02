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