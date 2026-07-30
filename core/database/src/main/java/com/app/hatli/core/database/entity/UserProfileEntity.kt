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
    val addressCity: String? = null,
    val addressArea: String? = null,
    val addressStreet: String? = null,
    val addressBuilding: String? = null,
    val addressFloor: String? = null,
    val addressApartment: String? = null,
    val addressNotes: String? = null,
    val addressLatitude: Double? = null,
    val addressLongitude: Double? = null,
    val nationalId: String? = null,
    val idImageUrl: String? = null,
    val selfieUrl: String? = null,
    val transportType: String? = null,
    val vehicleInfo: String? = null,
    val verificationStatus: String? = null,
)
