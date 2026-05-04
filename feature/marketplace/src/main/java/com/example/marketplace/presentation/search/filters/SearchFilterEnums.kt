package com.example.marketplace.presentation.search.filters

enum class SearchFilterCity(
    val title: String
) {
    NOVOSIBIRSK("Новосибирск"),
    MOSCOW("Москва"),
    SAINT_PETERSBURG("Санкт-Петербург"),
    ASTANA("Астана"),
    ALMATY("Алматы")
}

enum class SearchFilterCategory(
    val id: Long,
    val title: String
) {
    CLOTHES(
        id = 1L,
        title = "Одежда"
    ),
    TOOLS(
        id = 2L,
        title = "Инструменты"
    ),
    SPORT(
        id = 3L,
        title = "Спорт"
    ),
    HOME_APPLIANCES(
        id = 4L,
        title = "Бытовая техника"
    ),
    PHOTO_VIDEO(
        id = 5L,
        title = "Фото и видео"
    ),
    TOURISM(
        id = 6L,
        title = "Туризм"
    )
}