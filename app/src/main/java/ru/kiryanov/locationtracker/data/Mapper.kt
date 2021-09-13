package ru.kiryanov.locationtracker.data

import android.location.Location
import android.location.LocationManager
import ru.kiryanov.locationtracker.data.database.entities.LocationEntity
import ru.kiryanov.locationtracker.domain.model.DomainLocation
import java.util.*

internal fun LocationEntity.toDomainLocation(): DomainLocation {
    return DomainLocation(
        location = Location(LocationManager.GPS_PROVIDER)
            .apply {
                longitude = this@toDomainLocation.longitude
                latitude = this@toDomainLocation.latitude
            },
        date = date
    )
}

internal fun DomainLocation.toLocationEntity() =
    LocationEntity(
        latitude = location.latitude,
        longitude = location.longitude,
        date = date
    )
