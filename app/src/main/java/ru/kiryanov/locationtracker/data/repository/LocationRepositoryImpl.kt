package ru.kiryanov.locationtracker.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kiryanov.locationtracker.data.database.dao.LocationDao
import ru.kiryanov.locationtracker.data.toDomainLocation
import ru.kiryanov.locationtracker.data.toLocationEntity
import ru.kiryanov.locationtracker.domain.DomainLocation
import ru.kiryanov.locationtracker.domain.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val locationDao: LocationDao) :
    LocationRepository {

    override suspend fun getLocationList(): List<DomainLocation> {
        return withContext(Dispatchers.IO) {
            locationDao.getAll().map { locationEntity ->
                locationEntity.toDomainLocation()
            }
        }
    }

    override suspend fun saveLocation(location: DomainLocation) {
        withContext(Dispatchers.IO) {
            locationDao.insertAll(location.toLocationEntity())
        }
    }
}