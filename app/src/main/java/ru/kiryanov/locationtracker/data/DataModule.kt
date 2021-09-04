package ru.kiryanov.locationtracker.data

import dagger.Module
import ru.kiryanov.locationtracker.data.database.di.RoomModule
import ru.kiryanov.locationtracker.data.repository.dagger.RepositoryModule
import javax.inject.Singleton

@Module(
    includes = [
        RoomModule::class,
        RepositoryModule::class
    ]
)
interface DataModule