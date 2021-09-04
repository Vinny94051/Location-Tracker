package ru.kiryanov.locationtracker.domain

import dagger.Module
import ru.kiryanov.locationtracker.domain.usecase.di.UseCaseModule

@Module(
    includes = [
        UseCaseModule::class
    ]
)
interface DomainModule