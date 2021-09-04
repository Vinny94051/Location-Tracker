package ru.kiryanov.locationtracker.domain

import android.location.Location
import java.util.*

data class DomainLocation(
    val location: Location,
    val date: Date
)
