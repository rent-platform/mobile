package com.example.profile.presentation.editprofile

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profile.domain.ProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState(isLoading = true))
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _actions = MutableSharedFlow<EditProfileAction>()
    val actions: SharedFlow<EditProfileAction> = _actions.asSharedFlow()

    private val fullNameRegex = Regex("^[A-Za-zА-Яа-яЁё\\s\\-]+$")

    init {
        loadProfile()
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            EditProfileEvent.BackClicked -> navigateBack()

            EditProfileEvent.GuestProfileClicked -> {
                viewModelScope.launch {
                    _actions.emit(EditProfileAction.NavigateToGuestProfile)
                }
            }

            EditProfileEvent.UploadAvatarClicked -> {
                viewModelScope.launch {
                    _actions.emit(
                        EditProfileAction.ShowError(
                            "Загрузка аватара пока не подключена"
                        )
                    )
                }
            }

            EditProfileEvent.DeleteAvatarClicked -> {
                _uiState.update {
                    it.copy(
                        avatarUrl = null,
                        errorMessage = null
                    )
                }

                viewModelScope.launch {
                    _actions.emit(
                        EditProfileAction.ShowError(
                            "Аватар будет удалён после сохранения"
                        )
                    )
                }
            }

            EditProfileEvent.SaveClicked -> saveProfile()

            is EditProfileEvent.NicknameChanged -> {
                _uiState.update {
                    it.copy(
                        nickname = event.value,
                        nicknameError = null,
                        errorMessage = null
                    )
                }
            }

            is EditProfileEvent.FullNameChanged -> {
                _uiState.update {
                    it.copy(
                        fullName = event.value,
                        fullNameError = null,
                        errorMessage = null
                    )
                }
            }

            is EditProfileEvent.EmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = event.value,
                        emailError = null,
                        errorMessage = null
                    )
                }
            }

            is EditProfileEvent.BioChanged -> {
                _uiState.update {
                    it.copy(
                        bio = event.value,
                        bioError = null,
                        errorMessage = null
                    )
                }
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            runCatching {
                repository.getMyProfile()
            }.onSuccess { profile ->
                _uiState.update {
                    it.copy(
                        nickname = profile.nickname.orEmpty(),
                        fullName = profile.fullName,
                        email = profile.email.orEmpty(),
                        bio = profile.bio.orEmpty(),
                        avatarUrl = profile.avatarUrl,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }.onFailure { throwable ->
                val message = throwable.message ?: "Не удалось загрузить профиль"

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }

                _actions.emit(EditProfileAction.ShowError(message))
            }
        }
    }

    private fun saveProfile() {
        val currentState = _uiState.value

        val trimmedNickname = currentState.nickname.trim()
        val trimmedFullName = currentState.fullName.trim()
        val trimmedEmail = currentState.email.trim()
        val trimmedBio = currentState.bio.trim()

        val nicknameError = validateNickname(trimmedNickname)
        val fullNameError = validateFullName(trimmedFullName)
        val emailError = validateEmail(trimmedEmail)
        val bioError = validateBio(trimmedBio)

        if (
            nicknameError != null ||
            fullNameError != null ||
            emailError != null ||
            bioError != null
        ) {
            _uiState.update {
                it.copy(
                    nicknameError = nicknameError,
                    fullNameError = fullNameError,
                    emailError = emailError,
                    bioError = bioError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaving = true,
                    errorMessage = null
                )
            }

            runCatching {
                repository.updateMyProfile(
                    fullName = trimmedFullName,
                    email = trimmedEmail.ifBlank { null },
                    bio = trimmedBio.ifBlank { null },
                    avatarUrl = currentState.avatarUrl
                )
            }.onSuccess {
                _uiState.update {
                    it.copy(isSaving = false)
                }

                _actions.emit(EditProfileAction.NavigateBack)
            }.onFailure { throwable ->
                val message = throwable.message ?: "Не удалось сохранить профиль"

                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = message
                    )
                }

                _actions.emit(EditProfileAction.ShowError(message))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _actions.emit(EditProfileAction.NavigateBack)
        }
    }

    private fun validateNickname(value: String): String? {
        return when {
            value.isBlank() -> null
            value.length > 50 -> "Никнейм должен быть не длиннее 50 символов"
            else -> null
        }
    }

    private fun validateFullName(value: String): String? {
        return when {
            value.length !in 2..100 -> "Имя должно быть от 2 до 100 символов"
            !fullNameRegex.matches(value) -> "Имя может содержать только буквы, пробелы и дефисы"
            else -> null
        }
    }

    private fun validateEmail(value: String): String? {
        return when {
            value.isBlank() -> null
            value.length > 255 -> "Email должен быть не длиннее 255 символов"
            !Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Введите корректный email"
            else -> null
        }
    }

    private fun validateBio(value: String): String? {
        return when {
            value.length > 1000 -> "Описание должно быть не длиннее 1000 символов"
            else -> null
        }
    }
}