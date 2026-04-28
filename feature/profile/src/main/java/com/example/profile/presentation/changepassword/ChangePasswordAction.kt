package com.example.profile.presentation.changepassword

sealed interface ChangePasswordAction {
    data object PasswordChanged : ChangePasswordAction
}