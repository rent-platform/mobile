package com.example.core.demo.model

data class DemoCategory(
    val id: Long,
    val name: String,
    val slug: String,
    val sortOrder: Int,
    val isActive: Boolean = true
)