package ru.kiryanov.locationtracker.data.repository.dagger

import dagger.Binds
import dagger.Module
import ru.kiryanov.locationtracker.data.repository.LocationRepositoryImpl
import ru.kiryanov.locationtracker.domain.repository.LocationRepository

@Module
interface RepositoryModule {

    @Binds
    fun bindLocationRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository
}