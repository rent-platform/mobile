package com.example.marketplace.presentation.mapper

import com.example.core.ui.toDemoDrawableRes
import com.example.marketplace.R
import com.example.marketplace.domain.model.CatalogCategory
import com.example.marketplace.domain.model.CatalogItem
import com.example.marketplace.presentation.catalog.CatalogCategoryUi
import com.example.marketplace.presentation.catalog.CatalogItemUi

fun CatalogCategory.toUi(): CatalogCategoryUi {
    return CatalogCategoryUi(
        id = id,
        name = name
    )
}

fun CatalogItem.toUi(): CatalogItemUi {
    return CatalogItemUi(
        id = id,
        title = title,
        pricePerDay = pricePerDay,
        location = location,
        imageUrl = imageKey?.toDemoDrawableRes(),
        isFavorite = isFavorite
    )
}