package com.example.notifications

import androidx.core.app.Person

data class Messages(
    val message: String,
    val user: Person?,
    val time: Long = System.currentTimeMillis()
)