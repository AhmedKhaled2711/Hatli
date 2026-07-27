package com.app.hatli.core.model

/**
 * Pure domain model — no Android, Retrofit, or Room annotations.
 */
data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
)
