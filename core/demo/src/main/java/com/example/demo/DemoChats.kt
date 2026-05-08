package com.example.core.demo

import com.example.core.demo.model.DemoChat
import com.example.core.demo.model.DemoChatMessage

object DemoChats {

    val all = listOf(
        DemoChat(
            id = DemoIds.CHAT_CANON_ID,
            dealId = DemoIds.DEAL_CANON_ID,
            itemId = DemoIds.ITEM_CANON_ID,
            renterId = DemoIds.CURRENT_USER_ID,
            ownerId = DemoIds.OWNER_MARIA_ID,
            lastMessage = "Здравствуйте! Можно арендовать на выходные?",
            lastMessageTime = "13:09",
            unreadCountForCurrentUser = 2,
            messages = listOf(
                DemoChatMessage.UserMessage(
                    id = "message_1",
                    senderId = DemoIds.OWNER_MARIA_ID,
                    text = "Супер! А залог какой?",
                    time = "16:15",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "message_2",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Залог 15 000 ₽, возвращается при сдаче в целости. Цена аренды 15 000 ₽ за 3 дня.",
                    time = "16:20",
                    isRead = true
                ),
                DemoChatMessage.UserMessage(
                    id = "message_3",
                    senderId = DemoIds.OWNER_MARIA_ID,
                    text = "Отлично, оформляю заявку!",
                    time = "16:25",
                    isRead = true
                ),
                DemoChatMessage.SystemMessage(
                    id = "message_4",
                    text = "Создана заявка на аренду"
                ),
                DemoChatMessage.SystemMessage(
                    id = "message_5",
                    text = "Заявка подтверждена"
                ),
                DemoChatMessage.DateDivider(
                    id = "date_1",
                    title = "22 апреля 2026 г."
                ),
                DemoChatMessage.UserMessage(
                    id = "message_6",
                    senderId = DemoIds.OWNER_MARIA_ID,
                    text = "Мне удобнее всего у метро. Можно завтра в 14:00?",
                    time = "19:30",
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
                    text = "Да, забрать можно сегодня после 18:00",
                    time = "12:20",
                    isRead = true
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
                DemoChatMessage.UserMessage(
                    id = "tent_message_1",
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
                DemoChatMessage.UserMessage(
                    id = "makita_message_1",
                    senderId = DemoIds.OWNER_IVAN_ID,
                    text = "Здравствуйте, хочу арендовать шуруповёрт на сутки",
                    time = "10:30",
                    isRead = false
                ),
                DemoChatMessage.UserMessage(
                    id = "makita_message_2",
                    senderId = DemoIds.CURRENT_USER_ID,
                    text = "Готов подтвердить аренду",
                    time = "10:42",
                    isRead = true
                )
            )
        )
    )
}