package ru.kiryanov.locationtracker.presentation

import android.annotation.SuppressLint
import ru.kiryanov.locationtracker.domain.DomainLocation
import ru.kiryanov.locationtracker.utils.location.LocationResult
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("NewApi")
fun LocationResult.toDomainLocation(): DomainLocation? {
    return when (this) {
        is LocationResult.Success -> DomainLocation(
            location = location,
            date = createDate()
        )
        else -> null
    }
}

private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

@SuppressLint("NewApi", "SimpleDateFormat")
private fun createDate(): String {
    val sdf = SimpleDateFormat(DATE_FORMAT)
    return sdf.format(Calendar.getInstance().time)
}
