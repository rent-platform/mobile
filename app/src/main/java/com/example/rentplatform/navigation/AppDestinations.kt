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