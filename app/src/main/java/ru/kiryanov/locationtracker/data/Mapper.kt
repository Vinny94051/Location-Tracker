package ru.kiryanov.locationtracker.data

import android.location.Location
import android.location.LocationManager
import ru.kiryanov.locationtracker.data.database.entities.LocationEntity
import ru.kiryanov.locationtracker.domain.DomainLocation
import vlnny.base.ext.currentDate
import vlnny.base.ext.toDate
import java.time.LocalDateTime
import java.util.*

internal fun LocationEntity.toDomainLocation() =
    DomainLocation(
        location = Location(LocationManager.GPS_PROVIDER)
            .apply {
                longitude = this@toDomainLocation.longitude
                latitude = this@toDomainLocation.latitude
            },
        date = stringToDate(date)
    )

internal fun DomainLocation.toLocationEntity() =
    LocationEntity(
        latitude = location.latitude,
        longitude = location.longitude,
        date = dateToString(date)
    )

internal fun stringToDate(date: String): Date = currentDate.toDate()
internal fun dateToString(date: Date): String = currentDate.toString()