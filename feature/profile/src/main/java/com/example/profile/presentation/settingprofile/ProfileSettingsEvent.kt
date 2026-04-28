package com.example.profile.presentation.profilesettings

sealed interface ProfileSettingsEvent {
    object ChangePasswordClicked : ProfileSettingsEvent

    object DeleteProfileClicked : ProfileSettingsEvent
    object ConfirmDeleteProfileClicked : ProfileSettingsEvent
    object DismissDeleteDialogClicked : ProfileSettingsEvent
}