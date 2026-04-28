package com.example.profile.presentation.profilesettings

sealed interface ProfileSettingsAction {
    data object NavigateToChangePassword : ProfileSettingsAction
    data object ProfileDeleted : ProfileSettingsAction

    data class ShowError(
        val message: String
    ) : ProfileSettingsAction
}