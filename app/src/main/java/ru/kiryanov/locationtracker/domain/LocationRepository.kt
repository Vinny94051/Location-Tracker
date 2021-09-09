package ru.kiryanov.locationtracker.domain

import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    fun getLocationList(): Flow<List<DomainLocation>>

    suspend fun saveLocation(location: DomainLocation)
}