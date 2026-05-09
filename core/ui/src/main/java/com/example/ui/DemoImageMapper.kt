package com.example.core.ui

import com.example.ui.R

fun String.toDemoDrawableRes(): Int? {
    return when (this) {
        "pocofon" -> R.drawable.pocofon
        "bike" -> R.drawable.bike
        "soup" -> R.drawable.soup
        "isu152" -> R.drawable.isu152
        "wolksvagencar" -> R.drawable.wolksvagencar
        "camera" -> R.drawable.camera
        "drill" -> R.drawable.drill
        "tent" -> R.drawable.tent
        "makita" -> R.drawable.makita
        else -> null
    }
}