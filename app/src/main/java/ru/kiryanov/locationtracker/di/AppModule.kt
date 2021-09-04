package ru.kiryanov.locationtracker.di

import dagger.Module
import ru.kiryanov.locationtracker.data.DataModule
import ru.kiryanov.locationtracker.domain.DomainModule
import ru.kiryanov.locationtracker.utils.di.UtilsModule
import javax.inject.Singleton

@Module(
    includes = [
        DataModule::class,
        DomainModule::class,
        UtilsModule::class
    ]
)
interface AppModule