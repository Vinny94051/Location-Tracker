package ru.kiryanov.locationtracker.domain.usecase

import ru.kiryanov.locationtracker.domain.DomainLocation
import ru.kiryanov.locationtracker.domain.LocationRepository
import javax.inject.Inject

interface GetLocationsListUseCase {
    suspend fun getLocationsList() : List<DomainLocation>
}

class GetLocationListUseCaseImpl @Inject constructor(
    private val locationRepository: LocationRepository
) : GetLocationsListUseCase {

    override suspend fun getLocationsList(): List<DomainLocation> {
        return locationRepository.getLocationList()
    }
}