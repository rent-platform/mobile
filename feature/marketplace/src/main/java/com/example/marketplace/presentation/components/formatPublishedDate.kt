package com.example.marketplace.presentation.components

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatPublishedDate(createdAt: String): String {
    return runCatching {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        val outputFormat = SimpleDateFormat("d MMMM", Locale("ru")).apply {
            timeZone = TimeZone.getDefault()
        }

        val date = inputFormat.parse(createdAt)

        date?.let(outputFormat::format).orEmpty()
    }.getOrDefault(createdAt)
}