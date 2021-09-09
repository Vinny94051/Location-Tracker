package ru.kiryanov.locationtracker.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kiryanov.locationtracker.domain.DomainLocation
import ru.kiryanov.locationtracker.domain.LocationRepository
import javax.inject.Inject

interface GetLocationsListUseCase {
     fun getLocationsList(): Flow<List<DomainLocation>>
}

class GetLocationListUseCaseImpl @Inject constructor(
    private val locationRepository: LocationRepository
) : GetLocationsListUseCase {

    override  fun getLocationsList(): Flow<List<DomainLocation>> {
        return locationRepository.getLocationList()
    }
}