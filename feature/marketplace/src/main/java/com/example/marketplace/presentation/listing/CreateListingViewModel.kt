package com.example.marketplace.presentation.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CreateListingViewModel : ViewModel() {

    private val _state = MutableStateFlow(CreateListingUiState())
    val state: StateFlow<CreateListingUiState> = _state.asStateFlow()

    private val _events = Channel<CreateListingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: CreateListingAction) {
        when (action) {
            CreateListingAction.BackClick -> onBackClick()

            CreateListingAction.NextClick -> onNextClick()

            CreateListingAction.PublishClick -> onPublishClick()

            CreateListingAction.AddPhotoClick -> onAddPhotoClick()

            is CreateListingAction.RemovePhotoClick -> {
                onRemovePhotoClick(action.photoId)
            }

            is CreateListingAction.TitleChanged -> {
                _state.update {
                    it.copy(
                        title = action.value,
                        hasUnsavedChanges = true,
                        errorMessage = null
                    )
                }
                saveDraftAfterRequiredFieldsIfPossible()
            }

            is CreateListingAction.DescriptionChanged -> {
                _state.update {
                    it.copy(
                        description = action.value,
                        hasUnsavedChanges = true,
                        errorMessage = null
                    )
                }
            }

            is CreateListingAction.CategorySelected -> {
                _state.update {
                    it.copy(
                        selectedCategory = action.category,
                        hasUnsavedChanges = true,
                        errorMessage = null
                    )
                }
                saveDraftAfterRequiredFieldsIfPossible()
            }

            is CreateListingAction.PricePerDayChanged -> {
                _state.update {
                    it.copy(
                        pricePerDay = action.value.onlyDigits(),
                        hasUnsavedChanges = true,
                        errorMessage = null
                    )
                }
            }

            is CreateListingAction.PricePerHourChanged -> {
                _state.update {
                    it.copy(
                        pricePerHour = action.value.onlyDigits(),
                        hasUnsavedChanges = true,
                        errorMessage = null
                    )
                }
            }

            is CreateListingAction.DepositEnabledChanged -> {
                _state.update {
                    it.copy(
                        hasDeposit = action.value,
                        depositAmount = if (action.value) it.depositAmount else "",
                        hasUnsavedChanges = true,
                        errorMessage = null
                    )
                }
            }

            is CreateListingAction.DepositAmountChanged -> {
                _state.update {
                    it.copy(
                        depositAmount = action.value.onlyDigits(),
                        hasUnsavedChanges = true,
                        errorMessage = null
                    )
                }
            }

            is CreateListingAction.CityChanged -> {
                _state.update {
                    it.copy(
                        city = action.value,
                        hasUnsavedChanges = true,
                        errorMessage = null
                    )
                }
            }

            is CreateListingAction.PickupLocationChanged -> {
                _state.update {
                    it.copy(
                        pickupLocation = action.value,
                        hasUnsavedChanges = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    private fun onBackClick() {
        val currentState = _state.value

        if (!currentState.isFirstStep) {
            _state.update {
                it.copy(
                    currentStep = previousStep(it.currentStep),
                    errorMessage = null
                )
            }
            return
        }

        viewModelScope.launch {
            saveDraftIfPossible()
            _events.send(CreateListingEvent.NavigateBack)
        }
    }

    private fun onNextClick() {
        val currentState = _state.value

        if (!currentState.canGoNext) {
            _state.update {
                it.copy(errorMessage = "Заполните обязательные поля")
            }
            return
        }

        if (currentState.currentStep == ListingStep.Description) {
            saveDraftAfterRequiredFieldsIfPossible()
        }

        _state.update {
            it.copy(
                currentStep = nextStep(it.currentStep),
                errorMessage = null
            )
        }
    }

    private fun onPublishClick() {
        val currentState = _state.value

        if (!currentState.hasPhotos) {
            _state.update {
                it.copy(
                    currentStep = ListingStep.Photos,
                    isPhotosError = true,
                    errorMessage = "Добавьте хотя бы одну фотографию, чтобы опубликовать объявление"
                )
            }
            return
        }

        if (!currentState.canPublish) {
            _state.update {
                it.copy(errorMessage = "Заполните обязательные поля")
            }
            return
        }

        viewModelScope.launch {
            publishListing()
        }
    }

    /**
     * Пока здесь заглушка.
     * Когда подключишь Photo Picker, вместо этого будешь открывать выбор фото в Route.
     */
    private fun onAddPhotoClick() {
        val currentState = _state.value

        if (currentState.photos.size >= CreateListingUiState.MAX_PHOTOS) {
            viewModelScope.launch {
                _events.send(
                    CreateListingEvent.ShowMessage(
                        "Можно добавить не больше ${CreateListingUiState.MAX_PHOTOS} фотографий"
                    )
                )
            }
            return
        }

        val nextSortOrder = currentState.photos.size

        val newPhoto = ListingPhotoUi(
            id = UUID.randomUUID().toString(),
            imageUrl = null,
            localUri = null,
            sortOrder = nextSortOrder
        )

        _state.update {
            it.copy(
                photos = it.photos + newPhoto,
                isPhotosError = false,
                hasUnsavedChanges = true,
                errorMessage = null
            )
        }
    }

    private fun onRemovePhotoClick(photoId: String) {
        _state.update { currentState ->
            val updatedPhotos = currentState.photos
                .filterNot { it.id == photoId }
                .mapIndexed { index, photo ->
                    photo.copy(sortOrder = index)
                }

            currentState.copy(
                photos = updatedPhotos,
                hasUnsavedChanges = true,
                errorMessage = null
            )
        }
    }

    private fun saveDraftAfterRequiredFieldsIfPossible() {
        val currentState = _state.value

        if (!currentState.canSaveDraft || currentState.draftItemId != null) {
            return
        }

        viewModelScope.launch {
            saveDraftIfPossible()
        }
    }

    /**
     * Пока имитация сохранения черновика.
     *
     * По твоей логике backend сможет сохранять черновик после:
     * - title
     * - selectedCategory
     *
     * Фото для черновика необязательны.
     */
    private suspend fun saveDraftIfPossible() {
        val currentState = _state.value

        if (!currentState.canSaveDraft) {
            return
        }

        if (!currentState.hasUnsavedChanges && currentState.draftItemId != null) {
            return
        }

        _state.update {
            it.copy(isLoading = true)
        }

        delay(350)

        val draftId = currentState.draftItemId ?: UUID.randomUUID().toString()

        _state.update {
            it.copy(
                draftItemId = draftId,
                isDraftSaved = true,
                hasUnsavedChanges = false,
                isLoading = false,
                errorMessage = null
            )
        }
    }

    /**
     * Пока имитация публикации.
     *
     * Позже здесь будет:
     * 1. saveDraftIfPossible()
     * 2. upload photos, если нужно
     * 3. sendToModeration(itemId)
     */
    private suspend fun publishListing() {
        _state.update {
            it.copy(isLoading = true)
        }

        saveDraftIfPossible()

        delay(500)

        val itemId = _state.value.draftItemId ?: UUID.randomUUID().toString()

        _state.update {
            it.copy(
                draftItemId = itemId,
                isLoading = false,
                isDraftSaved = true,
                hasUnsavedChanges = false,
                errorMessage = null
            )
        }

        _events.send(
            CreateListingEvent.ListingPublished(itemId = itemId)
        )
    }

    private fun nextStep(currentStep: ListingStep): ListingStep {
        return when (currentStep) {
            ListingStep.Photos -> ListingStep.Description
            ListingStep.Description -> ListingStep.Terms
            ListingStep.Terms -> ListingStep.Terms
        }
    }

    private fun previousStep(currentStep: ListingStep): ListingStep {
        return when (currentStep) {
            ListingStep.Photos -> ListingStep.Photos
            ListingStep.Description -> ListingStep.Photos
            ListingStep.Terms -> ListingStep.Description
        }
    }

    private fun String.onlyDigits(): String {
        return filter(Char::isDigit)
    }
}