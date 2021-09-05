package ru.kiryanov.locationtracker.data

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import ru.kiryanov.locationtracker.data.database.entities.LocationEntity
import ru.kiryanov.locationtracker.domain.DomainLocation

import java.text.SimpleDateFormat

import java.util.*

internal fun LocationEntity.toDomainLocation() =
    DomainLocation(
        location = Location(LocationManager.GPS_PROVIDER)
            .apply {
                longitude = this@toDomainLocation.longitude
                latitude = this@toDomainLocation.latitude
            },
        date = date.fromEpochToDate()
    )

internal fun DomainLocation.toLocationEntity() =
    LocationEntity(
        latitude = location.latitude,
        longitude = location.longitude,
        date = date.toEpoch()
    )

internal const val ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

@SuppressLint("SimpleDateFormat")
internal fun String.fromEpochToDate(): Date {
    return SimpleDateFormat(ISO_8601_DATE_FORMAT).parse(this)
}

@SuppressLint("SimpleDateFormat")
internal fun Date?.toEpoch(): String {
    return SimpleDateFormat(ISO_8601_DATE_FORMAT).format(this ?: Date())
}