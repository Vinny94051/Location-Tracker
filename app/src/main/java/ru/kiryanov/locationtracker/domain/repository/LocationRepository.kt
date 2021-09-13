package ru.kiryanov.locationtracker.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.kiryanov.locationtracker.domain.model.DomainLocation

interface LocationRepository {

    fun getLocationList(): Flow<List<DomainLocation>>

    suspend fun saveLocation(location: DomainLocation)
}