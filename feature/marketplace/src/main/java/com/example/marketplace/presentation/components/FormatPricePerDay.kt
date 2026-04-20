package com.example.marketplace.presentation.components

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun formatPricePerDay(price: Long): String {
    val symbols = DecimalFormatSymbols().apply {
        groupingSeparator = ' '
    }

    val formatter = DecimalFormat("#,###", symbols)
    return "${formatter.format(price)}\u202F ₽\u202F/\u202Fдень"
}