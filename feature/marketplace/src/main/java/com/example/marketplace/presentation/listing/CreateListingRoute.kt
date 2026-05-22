package com.example.marketplace.presentation.listing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateListingRoute(
    onNavigateBack: () -> Unit,
    onListingPublished: (itemId: String) -> Unit,
) {
    val viewModel: CreateListingViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                CreateListingEvent.NavigateBack -> {
                    onNavigateBack()
                }

                is CreateListingEvent.ListingPublished -> {
                    onListingPublished(event.itemId)
                }

                is CreateListingEvent.ShowMessage -> {
                    // Позже SnackbarHostState.
                }
            }
        }
    }

    CreateListingScreen(
        state = state,

        onBackClick = {
            viewModel.onAction(CreateListingAction.BackClick)
        },

        onNextClick = {
            viewModel.onAction(CreateListingAction.NextClick)
        },

        onPublishClick = {
            viewModel.onAction(CreateListingAction.PublishClick)
        },

        onAddPhotoClick = {
            viewModel.onAction(CreateListingAction.AddPhotoClick)
        },

        onRemovePhotoClick = { photoId ->
            viewModel.onAction(
                CreateListingAction.RemovePhotoClick(photoId)
            )
        },

        onTitleChange = { value ->
            viewModel.onAction(
                CreateListingAction.TitleChanged(value)
            )
        },

        onDescriptionChange = { value ->
            viewModel.onAction(
                CreateListingAction.DescriptionChanged(value)
            )
        },

        onCategorySelected = { category ->
            viewModel.onAction(
                CreateListingAction.CategorySelected(category)
            )
        },

        onPricePerDayChange = { value ->
            viewModel.onAction(
                CreateListingAction.PricePerDayChanged(value)
            )
        },

        onPricePerHourChange = { value ->
            viewModel.onAction(
                CreateListingAction.PricePerHourChanged(value)
            )
        },

        onDepositEnabledChange = { value ->
            viewModel.onAction(
                CreateListingAction.DepositEnabledChanged(value)
            )
        },

        onDepositAmountChange = { value ->
            viewModel.onAction(
                CreateListingAction.DepositAmountChanged(value)
            )
        },

        onCityChange = { value ->
            viewModel.onAction(
                CreateListingAction.CityChanged(value)
            )
        },

        onPickupLocationChange = { value ->
            viewModel.onAction(
                CreateListingAction.PickupLocationChanged(value)
            )
        }
    )
}