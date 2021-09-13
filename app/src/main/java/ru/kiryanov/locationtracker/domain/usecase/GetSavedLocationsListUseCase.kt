package ru.kiryanov.locationtracker.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kiryanov.locationtracker.domain.model.DomainLocation
import ru.kiryanov.locationtracker.domain.repository.LocationRepository
import javax.inject.Inject

interface GetSavedLocationsListUseCase {
     fun getLocationsList(): Flow<List<DomainLocation>>
}

class GetSavedLocationListUseCaseImpl @Inject constructor(
    private val locationRepository: LocationRepository
) : GetSavedLocationsListUseCase {

    override  fun getLocationsList(): Flow<List<DomainLocation>> {
        return locationRepository.getLocationList()
    }
}