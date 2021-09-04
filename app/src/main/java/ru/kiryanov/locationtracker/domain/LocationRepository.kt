package ru.kiryanov.locationtracker.domain

interface LocationRepository {

    suspend fun getLocationList(): List<DomainLocation>

    suspend fun saveLocation(location: DomainLocation)
}