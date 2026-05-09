package com.example.marketplace.presentation.search.filters
import java.io.Serializable

data class SearchFiltersResult(
    val categoryId: Long?,
    val categoryTitle: String?,
    val city: String?,
    val minPricePerDay: Long?,
    val maxPricePerDay: Long?,
    val minPricePerHour: Long?,
    val maxPricePerHour: Long?,
    val onlyAvailableNow: Boolean
) : Serializable