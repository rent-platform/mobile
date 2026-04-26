package com.example.profile.presentation.profile

sealed interface ProfileEvent {
    object EditProfileClicked : ProfileEvent
    object RatingClicked : ProfileEvent
    object ActiveItemsClicked : ProfileEvent
    object ModerationItemsClicked : ProfileEvent
    object RejectedItemsClicked : ProfileEvent
    object DraftItemsClicked : ProfileEvent
    object ArchivedItemsClicked : ProfileEvent
    object MyRentalsClicked : ProfileEvent
    object RentalHistoryClicked : ProfileEvent
    object CreateItemClicked : ProfileEvent
    object RefreshProfile : ProfileEvent
    object LogoutClicked : ProfileEvent
}