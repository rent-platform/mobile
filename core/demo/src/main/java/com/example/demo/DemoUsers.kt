package com.example.core.demo

import com.example.core.demo.model.DemoUser

object DemoUsers {

    val currentUser = DemoUser(
        id = DemoIds.CURRENT_USER_ID,
        fullName = "Сергей Иванов",
        nickname = "sergey_rent",
        avatarUrl = null,
        bio = "Сдаю технику и иногда арендую вещи для поездок",
        phone = "+7 900 123 45 67",
        email = "sergey@mail.ru",
        role = "user",
        rating = 4.8f,
        reviewsCount = 24,
        isPhoneVerified = true,
        isEmailVerified = true,
        isActive = true,
        registeredAt = "12.04.2026",
        updatedAt = "25.04.2026"
    )

    val ivan = DemoUser(
        id = DemoIds.OWNER_IVAN_ID,
        fullName = "Иван Петров",
        nickname = "ivan_phone",
        avatarUrl = null,
        bio = "Сдаю электронику в хорошем состоянии",
        phone = null,
        email = null,
        rating = 4.8f,
        reviewsCount = 12,
        registeredAt = "10.03.2026",
        updatedAt = "28.04.2026"
    )

    val alexey = DemoUser(
        id = DemoIds.OWNER_ALEXEY_ID,
        fullName = "Алексей Иванов",
        nickname = "alexey_events",
        avatarUrl = null,
        bio = "Техника для мероприятий и фотосессий",
        phone = null,
        email = null,
        rating = 5.0f,
        reviewsCount = 3,
        registeredAt = "15.03.2026",
        updatedAt = "25.04.2026"
    )

    val sergey = DemoUser(
        id = DemoIds.OWNER_SERGEY_ID,
        fullName = "Сергей Кузнецов",
        nickname = "sergey_auto",
        avatarUrl = null,
        bio = "Краткосрочная аренда транспорта",
        phone = null,
        email = null,
        rating = 4.6f,
        reviewsCount = 9,
        registeredAt = "20.03.2026",
        updatedAt = "21.04.2026"
    )

    val maria = DemoUser(
        id = DemoIds.OWNER_MARIA_ID,
        fullName = "Мария Волкова",
        nickname = "maria_home",
        avatarUrl = null,
        bio = "Сдаю товары для дома и туризма",
        phone = null,
        email = null,
        rating = 4.9f,
        reviewsCount = 18,
        registeredAt = "01.04.2026",
        updatedAt = "18.04.2026"
    )

    val dmitry = DemoUser(
        id = DemoIds.OWNER_DMITRY_ID,
        fullName = "Дмитрий Соколов",
        nickname = "dmitry_bike",
        avatarUrl = null,
        bio = "Велосипеды и спортинвентарь",
        phone = null,
        email = null,
        rating = 4.7f,
        reviewsCount = 21,
        registeredAt = "05.04.2026",
        updatedAt = "16.04.2026"
    )

    val all = listOf(
        currentUser,
        ivan,
        alexey,
        sergey,
        maria,
        dmitry
    )
}