package com.example.marketplace.presentation.listing

data class CreateListingUiState(
    val currentStep: ListingStep = ListingStep.Photos,

    val photos: List<ListingPhotoUi> = emptyList(),
    val isPhotosError: Boolean = false,

    val title: String = "",
    val description: String = "",

    val selectedCategory: ListingCategory? = null,

    val pricePerDay: String = "",
    val pricePerHour: String = "",

    val hasDeposit: Boolean = true,
    val depositAmount: String = "",

    val city: String = "",
    val pickupLocation: String = "",

    val draftItemId: String? = null,
    val isDraftSaved: Boolean = false,
    val hasUnsavedChanges: Boolean = false,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val currentStepNumber: Int
        get() = currentStep.ordinal + 1

    val totalSteps: Int
        get() = ListingStep.entries.size

    val progress: Float
        get() = currentStepNumber.toFloat() / totalSteps.toFloat()

    val isFirstStep: Boolean
        get() = currentStep == ListingStep.Photos

    val isLastStep: Boolean
        get() = currentStep == ListingStep.Terms
    val canGoNext: Boolean
        get() = when (currentStep) {
            ListingStep.Photos -> true

            ListingStep.Description ->
                title.isNotBlank() &&
                        selectedCategory != null

            ListingStep.Terms ->
                city.isNotBlank() &&
                        pickupLocation.isNotBlank() &&
                        hasAnyPrice &&
                        hasValidDeposit
        }

    val canSaveDraft: Boolean
        get() =
            title.isNotBlank() &&
                    selectedCategory != null

    val canPublish: Boolean
        get() =
            photos.isNotEmpty() &&
                    title.isNotBlank() &&
                    selectedCategory != null &&
                    city.isNotBlank() &&
                    pickupLocation.isNotBlank() &&
                    hasAnyPrice &&
                    hasValidDeposit

    val hasPhotos: Boolean
        get() = photos.isNotEmpty()

    private val hasAnyPrice: Boolean
        get() = pricePerDay.isNotBlank() || pricePerHour.isNotBlank()

    private val hasValidDeposit: Boolean
        get() = !hasDeposit || depositAmount.isNotBlank()

    companion object {
        const val MAX_PHOTOS = 10
    }
}

data class ListingPhotoUi(
    val id: String,
    val imageUrl: String? = null,
    val localUri: String? = null,
    val sortOrder: Int
) {
    val isCover: Boolean
        get() = sortOrder == 0
}