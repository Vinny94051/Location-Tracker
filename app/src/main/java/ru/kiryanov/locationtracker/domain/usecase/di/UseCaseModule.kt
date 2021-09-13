package ru.kiryanov.locationtracker.domain.usecase.di

import dagger.Binds
import dagger.Module
import ru.kiryanov.locationtracker.domain.usecase.GetSavedLocationListUseCaseImpl
import ru.kiryanov.locationtracker.domain.usecase.GetSavedLocationsListUseCase
import ru.kiryanov.locationtracker.domain.usecase.SaveLocationUseCase
import ru.kiryanov.locationtracker.domain.usecase.SaveLocationUseCaseImpl

@Module
interface UseCaseModule {

    @Binds
    fun bindSaveLocationUseCase(saveLocationUseCaseImpl: SaveLocationUseCaseImpl): SaveLocationUseCase

    @Binds
    fun bindGetLocationsListUseCase(getLocationListUseCaseImpl: GetSavedLocationListUseCaseImpl): GetSavedLocationsListUseCase
}