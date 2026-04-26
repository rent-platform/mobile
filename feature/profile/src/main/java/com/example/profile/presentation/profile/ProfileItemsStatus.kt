package com.example.profile.presentation.profile

enum class ProfileItemsStatus(
    val backendValue: String
) {
    ACTIVE("active"),
    MODERATION("moderation"),
    REJECTED("rejected"),
    DRAFT("draft"),
    ARCHIVED("archived")
}