package ru.kiryanov.locationtracker.dagger

import dagger.Module

@Module(
    includes = [
        UtilsModule::class
    ]
)
interface AppModule