package com.example.marketplace.presentation.listing

enum class ListingStep(
    val title: String,
    val shortTitle: String,
    val subtitle: String
) {
    Photos(
        title = "Фотографии",
        shortTitle = "Фото",
        subtitle = "Добавьте до 10 фотографий. Первая станет обложкой объявления."
    ),

    Description(
        title = "Описание вещи",
        shortTitle = "Описание",
        subtitle = "Подробное описание помогает арендаторам быстрее найти вашу вещь."
    ),

    Terms(
        title = "Стоимость и условия",
        shortTitle = "Условия",
        subtitle = "Укажите стоимость аренды, залог и место выдачи."
    )
}