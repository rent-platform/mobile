package com.example.core.demo

import com.example.core.demo.model.DemoCategory

object DemoCategories {

    val all = listOf(
        DemoCategory(
            id = DemoIds.CATEGORY_TOOLS_ID,
            name = "Инструменты",
            slug = "tools",
            sortOrder = 1
        ),
        DemoCategory(
            id = DemoIds.CATEGORY_ELECTRONICS_ID,
            name = "Электроника",
            slug = "electronics",
            sortOrder = 2
        ),
        DemoCategory(
            id = DemoIds.CATEGORY_PHOTO_VIDEO_ID,
            name = "Фото и видео",
            slug = "photo-video",
            sortOrder = 3
        ),
        DemoCategory(
            id = DemoIds.CATEGORY_SPORT_ID,
            name = "Спорт",
            slug = "sport",
            sortOrder = 4
        ),
        DemoCategory(
            id = DemoIds.CATEGORY_HOME_ID,
            name = "Товары для дома",
            slug = "home",
            sortOrder = 5
        ),
        DemoCategory(
            id = DemoIds.CATEGORY_TRANSPORT_ID,
            name = "Транспорт",
            slug = "transport",
            sortOrder = 6
        )
    )
}