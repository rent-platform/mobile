package com.example.profile.presentation.changepassword

sealed interface ChangePasswordEvent {
    data class CurrentPasswordChanged(val value: String) : ChangePasswordEvent
    data class NewPasswordChanged(val value: String) : ChangePasswordEvent
    data class ConfirmNewPasswordChanged(val value: String) : ChangePasswordEvent

    data object SaveClicked : ChangePasswordEvent
}