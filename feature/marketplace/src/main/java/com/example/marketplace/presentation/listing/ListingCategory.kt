package com.example.marketplace.presentation.listing

enum class ListingCategory(
    val id: Long,
    val displayName: String
) {
    Tools(
        id = 1L,
        displayName = "Инструменты"
    ),
    Electronics(
        id = 2L,
        displayName = "Электроника"
    ),
    PhotoVideo(
        id = 3L,
        displayName = "Фото и видео"
    ),
    Sport(
        id = 4L,
        displayName = "Спорт"
    ),
    Home(
        id = 5L,
        displayName = "Товары для дома"
    ),
    Transport(
        id = 6L,
        displayName = "Транспорт"
    )
}