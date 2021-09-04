package ru.kiryanov.locationtracker.domain.usecase

import ru.kiryanov.locationtracker.domain.DomainLocation
import ru.kiryanov.locationtracker.domain.LocationRepository
import javax.inject.Inject

interface SaveLocationUseCase {
    suspend fun saveLocation(location: DomainLocation)
}

class SaveLocationUseCaseImpl @Inject constructor(
    private val locationRepository: LocationRepository
) : SaveLocationUseCase {

    override suspend fun saveLocation(location: DomainLocation) {
        locationRepository.saveLocation(location)
    }
}