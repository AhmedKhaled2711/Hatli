package com.app.hatli.core.data.mapper

import com.app.hatli.core.database.entity.UserProfileEntity
import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.UserAddress
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
    addressCity = address?.city,
    addressArea = address?.area,
    addressStreet = address?.street,
    addressBuilding = address?.building,
    addressFloor = address?.floor,
    addressApartment = address?.apartment,
    addressNotes = address?.notes,
    addressLatitude = address?.latitude,
    addressLongitude = address?.longitude,
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
    gender = runCatching { Gender.valueOf(gender) }.getOrDefault(Gender.Male),
    createdAt = createdAt,
    updatedAt = updatedAt,
    profileCompleted = profileCompleted,
    address = toAddressOrNull(),
    nationalId = nationalId,
    idImageUrl = idImageUrl,
    selfieUrl = selfieUrl,
    transportType = transportType?.let { TransportType.valueOf(it) },
    vehicleInfo = vehicleInfo,
    verificationStatus = verificationStatus?.let { VerificationStatus.valueOf(it) },
)

private fun UserProfileEntity.toAddressOrNull(): UserAddress? {
    if (addressCity.isNullOrBlank() &&
        addressArea.isNullOrBlank() &&
        addressStreet.isNullOrBlank() &&
        addressBuilding.isNullOrBlank()
    ) {
        return null
    }
    return UserAddress(
        city = addressCity.orEmpty(),
        area = addressArea.orEmpty(),
        street = addressStreet.orEmpty(),
        building = addressBuilding.orEmpty(),
        floor = addressFloor,
        apartment = addressApartment,
        notes = addressNotes,
        latitude = addressLatitude,
        longitude = addressLongitude,
    )
}
