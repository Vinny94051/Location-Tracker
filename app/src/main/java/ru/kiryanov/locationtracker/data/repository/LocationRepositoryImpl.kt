package ru.kiryanov.locationtracker.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import ru.kiryanov.locationtracker.data.database.dao.LocationDao
import ru.kiryanov.locationtracker.data.toDomainLocation
import ru.kiryanov.locationtracker.data.toLocationEntity
import ru.kiryanov.locationtracker.domain.DomainLocation
import ru.kiryanov.locationtracker.domain.LocationRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LocationRepositoryImpl @Inject constructor(private val locationDao: LocationDao) :
    LocationRepository {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override fun getLocationList(): Flow<List<DomainLocation>> {
        return locationDao.getAll()
            .mapLatest { entities ->
                entities.map { locationEntity -> locationEntity.toDomainLocation() }
            }.flowOn(ioDispatcher)
    }

    override suspend fun saveLocation(location: DomainLocation) {
        withContext(ioDispatcher) {
            locationDao.insertAll(location.toLocationEntity())
        }
    }
}