package com.app.hatli.core.data.mapper

import com.app.hatli.core.database.entity.UserProfileEntity
import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.UserProfile
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.model.VerificationStatus

fun UserProfile.toEntity(): UserProfileEntity = UserProfileEntity(
    uid = uid,
    role = role.name,
    phone = phone,
    firstName = firstName,
    lastName = lastName,
    email = email,
    gender = gender.name,
    createdAt = createdAt,
    updatedAt = updatedAt,
    profileCompleted = profileCompleted,
    nationalId = nationalId,
    idImageUrl = idImageUrl,
    selfieUrl = selfieUrl,
    transportType = transportType?.name,
    vehicleInfo = vehicleInfo,
    verificationStatus = verificationStatus?.name,
)

fun UserProfileEntity.toDomain(): UserProfile = UserProfile(
    uid = uid,
    role = UserRole.valueOf(role),
    phone = phone,
    firstName = firstName,
    lastName = lastName,
    email = email,
    gender = Gender.valueOf(gender),
    createdAt = createdAt,
    updatedAt = updatedAt,
    profileCompleted = profileCompleted,
    nationalId = nationalId,
    idImageUrl = idImageUrl,
    selfieUrl = selfieUrl,
    transportType = transportType?.let { TransportType.valueOf(it) },
    vehicleInfo = vehicleInfo,
    verificationStatus = verificationStatus?.let { VerificationStatus.valueOf(it) },
)
