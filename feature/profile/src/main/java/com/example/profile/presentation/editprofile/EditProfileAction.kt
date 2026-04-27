package com.example.profile.presentation.editprofile

sealed interface EditProfileAction {
    data object NavigateBack : EditProfileAction
    data object NavigateToGuestProfile : EditProfileAction
    data class ShowError(val message: String) : EditProfileAction
}