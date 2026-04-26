package com.example.profile.presentation.profile

sealed interface ProfileAction {

    object NavigateToEditProfile : ProfileAction

    object NavigateToRating : ProfileAction

    data class NavigateToMyItems(
        val status: ProfileItemsStatus
    ) : ProfileAction

    object NavigateToMyRentals : ProfileAction

    object NavigateToRentalHistory : ProfileAction

    object NavigateToCreateItem : ProfileAction
    object Logout : ProfileAction
}