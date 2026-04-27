package com.example.profile.presentation.editprofile

sealed interface EditProfileEvent {
    data object BackClicked : EditProfileEvent
    data object GuestProfileClicked : EditProfileEvent
    data object UploadAvatarClicked : EditProfileEvent
    data object DeleteAvatarClicked : EditProfileEvent
    data object SaveClicked : EditProfileEvent

    data class NicknameChanged(val value: String) : EditProfileEvent
    data class FullNameChanged(val value: String) : EditProfileEvent
    data class EmailChanged(val value: String) : EditProfileEvent
    data class BioChanged(val value: String) : EditProfileEvent
}