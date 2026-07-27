package com.app.hatli.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val uid: String,
    val role: String,
    val phone: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: String,
    val createdAt: Long,
    val updatedAt: Long,
    val profileCompleted: Boolean,
    val nationalId: String? = null,
    val idImageUrl: String? = null,
    val selfieUrl: String? = null,
    val transportType: String? = null,
    val vehicleInfo: String? = null,
    val verificationStatus: String? = null,
)
