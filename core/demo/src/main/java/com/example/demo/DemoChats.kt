package com.example.core.demo

import com.example.core.demo.model.DemoChat
import com.example.core.demo.model.DemoChatMessage

object DemoChats {

    val all = listOf(
        DemoChat(
            id = DemoIds.CHAT_POCOFON_ID,
            dealId = null,
            itemId = DemoIds.ITEM_POCOFON_ID,
            renterId = DemoIds.CURRENT_USER_ID,
            ownerId = DemoIds.OWNER_IVAN_ID,
            lastMessage = "Здравствуйте! Телефон ещё доступен на завтра?",
            lastMessageTime = "только что",
            unreadCountForCurrentUser = 0,
            messages = listOf(
                DemoChatMessage.DateDivider(
                    id = "pocofon_date_1",
                    title = "Сегодня"
                ),
                DemoChatMessage.UserMessage(
                    id = "pocofon_message_1",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Здравствуйте! Телефон ещё доступен на завтра?",
                    time = "только что",
                    isRead = false
                )
            )
        ),

        DemoChat(
            id = DemoIds.CHAT_CANON_ID,
            dealId = DemoIds.DEAL_CANON_ID,
            itemId = DemoIds.ITEM_CANON_ID,
            renterId = DemoIds.CURRENT_USER_ID,
            ownerId = DemoIds.OWNER_MARIA_ID,
            lastMessage = "Да, можно. На какие даты хотите забронировать?",
            lastMessageTime = "13:09",
            unreadCountForCurrentUser = 2,
            messages = listOf(
                DemoChatMessage.DateDivider(
                    id = "canon_date_1",
                    title = "22 апреля 2026 г."
                ),
                DemoChatMessage.UserMessage(
                    id = "canon_message_1",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Здравствуйте! Можно арендовать фотоаппарат на выходные?",
                    time = "13:02",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "canon_message_2",
                    senderId = DemoIds.OWNER_MARIA_ID,
                    text = "Здравствуйте! Да, на выходные свободен.",
                    time = "13:05",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "canon_message_3",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Супер! А залог какой?",
                    time = "13:06",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "canon_message_4",
                    senderId = DemoIds.OWNER_MARIA_ID,
                    text = "Залог 15 000 ₽, возвращается после проверки камеры. Стоимость аренды 15 000 ₽ за 3 дня.",
                    time = "13:08",
                    isRead = false
                ),
                DemoChatMessage.UserMessage(
                    id = "canon_message_5",
                    senderId = DemoIds.OWNER_MARIA_ID,
                    text = "Да, можно. На какие даты хотите забронировать?",
                    time = "13:09",
                    isRead = false
                ),
                DemoChatMessage.SystemMessage(
                    id = "canon_system_1",
                    text = "Создана заявка на аренду"
                ),
                DemoChatMessage.SystemMessage(
                    id = "canon_system_2",
                    text = "Заявка подтверждена"
                )
            )
        ),

        DemoChat(
            id = DemoIds.CHAT_BIKE_ID,
            dealId = DemoIds.DEAL_BIKE_ID,
            itemId = DemoIds.ITEM_BIKE_ID,
            renterId = DemoIds.CURRENT_USER_ID,
            ownerId = DemoIds.OWNER_DMITRY_ID,
            lastMessage = "Да, велосипед свободен. Могу передать у Красного проспекта.",
            lastMessageTime = "18:35",
            unreadCountForCurrentUser = 1,
            messages = listOf(
                DemoChatMessage.DateDivider(
                    id = "bike_date_1",
                    title = "28 апреля 2026 г."
                ),
                DemoChatMessage.UserMessage(
                    id = "bike_message_1",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Здравствуйте! Велосипед горный доступен завтра?",
                    time = "18:20",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "bike_message_2",
                    senderId = DemoIds.OWNER_DMITRY_ID,
                    text = "Здравствуйте! Да, завтра свободен на весь день.",
                    time = "18:23",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "bike_message_3",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Хочу взять с 10:00 до вечера. Залог 3 000 ₽, верно?",
                    time = "18:27",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "bike_message_4",
                    senderId = DemoIds.OWNER_DMITRY_ID,
                    text = "Да, всё верно. Аренда 700 ₽ за день, залог 3 000 ₽.",
                    time = "18:31",
                    isRead = true
                ),
                DemoChatMessage.SystemMessage(
                    id = "bike_system_1",
                    text = "Создана заявка на аренду"
                ),
                DemoChatMessage.UserMessage(
                    id = "bike_message_5",
                    senderId = DemoIds.OWNER_DMITRY_ID,
                    text = "Да, велосипед свободен. Могу передать у Красного проспекта.",
                    time = "18:35",
                    isRead = false
                )
            )
        ),

        DemoChat(
            id = DemoIds.CHAT_DRILL_ID,
            dealId = DemoIds.DEAL_DRILL_ID,
            itemId = DemoIds.ITEM_DRILL_ID,
            renterId = DemoIds.CURRENT_USER_ID,
            ownerId = DemoIds.OWNER_DMITRY_ID,
            lastMessage = "Да, забрать можно сегодня после 18:00",
            lastMessageTime = "Вчера",
            unreadCountForCurrentUser = 0,
            messages = listOf(
                DemoChatMessage.DateDivider(
                    id = "drill_date_1",
                    title = "23 апреля 2026 г."
                ),
                DemoChatMessage.UserMessage(
                    id = "drill_message_1",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Здравствуйте! Дрель ещё доступна?",
                    time = "12:10",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "drill_message_2",
                    senderId = DemoIds.OWNER_DMITRY_ID,
                    text = "Здравствуйте! Да, доступна. Нужна на день или на несколько дней?",
                    time = "12:13",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "drill_message_3",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "На два дня. Нужно собрать мебель, поэтому заберу вечером и верну послезавтра.",
                    time = "12:18",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "drill_message_4",
                    senderId = DemoIds.OWNER_DMITRY_ID,
                    text = "Хорошо. Залог 6 000 ₽, аренда 2 000 ₽ в день.",
                    time = "12:19",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "drill_message_5",
                    senderId = DemoIds.OWNER_DMITRY_ID,
                    text = "Да, забрать можно сегодня после 18:00",
                    time = "12:20",
                    isRead = true
                ),
                DemoChatMessage.SystemMessage(
                    id = "drill_system_1",
                    text = "Заявка подтверждена"
                ),
                DemoChatMessage.SystemMessage(
                    id = "drill_system_2",
                    text = "Аренда началась"
                )
            )
        ),

        DemoChat(
            id = DemoIds.CHAT_TENT_ID,
            dealId = DemoIds.DEAL_TENT_ID,
            itemId = DemoIds.ITEM_TENT_ID,
            renterId = DemoIds.OWNER_MARIA_ID,
            ownerId = DemoIds.CURRENT_USER_ID,
            lastMessage = "Спасибо, всё вернула в хорошем состоянии",
            lastMessageTime = "21 апр.",
            unreadCountForCurrentUser = 1,
            messages = listOf(
                DemoChatMessage.DateDivider(
                    id = "tent_date_1",
                    title = "17 апреля 2026 г."
                ),
                DemoChatMessage.UserMessage(
                    id = "tent_message_1",
                    senderId = DemoIds.OWNER_MARIA_ID,
                    text = "Здравствуйте! Палатка на 4 места доступна с 18 по 21 апреля?",
                    time = "09:10",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "tent_message_2",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Здравствуйте! Да, свободна. В комплекте палатка, чехол и колышки.",
                    time = "09:18",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "tent_message_3",
                    senderId = DemoIds.OWNER_MARIA_ID,
                    text = "Отлично, тогда оформляю заявку. Забрать можно в Академгородке?",
                    time = "09:22",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "tent_message_4",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Да, можно. После подтверждения напишу точный адрес.",
                    time = "09:25",
                    isRead = true
                ),
                DemoChatMessage.SystemMessage(
                    id = "tent_system_1",
                    text = "Создана заявка на аренду"
                ),
                DemoChatMessage.SystemMessage(
                    id = "tent_system_2",
                    text = "Заявка подтверждена"
                ),
                DemoChatMessage.DateDivider(
                    id = "tent_date_2",
                    title = "21 апреля 2026 г."
                ),
                DemoChatMessage.UserMessage(
                    id = "tent_message_5",
                    senderId = DemoIds.OWNER_MARIA_ID,
                    text = "Здравствуйте! Вернула палатку, всё сложила в чехол.",
                    time = "17:45",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "tent_message_6",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Спасибо, сейчас проверю комплектность.",
                    time = "17:55",
                    isRead = true
                ),
                DemoChatMessage.SystemMessage(
                    id = "tent_system_3",
                    text = "Аренда завершена"
                ),
                DemoChatMessage.UserMessage(
                    id = "tent_message_7",
                    senderId = DemoIds.OWNER_MARIA_ID,
                    text = "Спасибо, всё вернула в хорошем состоянии",
                    time = "18:10",
                    isRead = false
                )
            )
        ),

        DemoChat(
            id = DemoIds.CHAT_MAKITA_ID,
            dealId = DemoIds.DEAL_MAKITA_ID,
            itemId = DemoIds.ITEM_MAKITA_ID,
            renterId = DemoIds.OWNER_IVAN_ID,
            ownerId = DemoIds.CURRENT_USER_ID,
            lastMessage = "Готов подтвердить аренду",
            lastMessageTime = "10:42",
            unreadCountForCurrentUser = 3,
            messages = listOf(
                DemoChatMessage.DateDivider(
                    id = "makita_date_1",
                    title = "26 апреля 2026 г."
                ),
                DemoChatMessage.UserMessage(
                    id = "makita_message_1",
                    senderId = DemoIds.OWNER_IVAN_ID,
                    text = "Здравствуйте, хочу арендовать шуруповёрт на сутки.",
                    time = "10:30",
                    isRead = false
                ),
                DemoChatMessage.UserMessage(
                    id = "makita_message_2",
                    senderId = DemoIds.OWNER_IVAN_ID,
                    text = "Можно забрать сегодня вечером?",
                    time = "10:31",
                    isRead = false
                ),
                DemoChatMessage.UserMessage(
                    id = "makita_message_3",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Здравствуйте! Да, можно после 19:00.",
                    time = "10:40",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "makita_message_4",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Готов подтвердить аренду",
                    time = "10:42",
                    isRead = true
                ),
                DemoChatMessage.SystemMessage(
                    id = "makita_system_1",
                    text = "Создана заявка на аренду"
                )
            )
        )
    )
}