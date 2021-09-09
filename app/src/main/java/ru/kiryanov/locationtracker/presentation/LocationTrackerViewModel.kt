package ru.kiryanov.locationtracker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import ru.kiryanov.locationtracker.domain.DomainLocation
import ru.kiryanov.locationtracker.domain.usecase.GetLocationsListUseCase
import vlnny.base.viewModel.BaseViewModel
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class LocationTrackerViewModel @Inject constructor(
    private val getLocationsListUseCase: GetLocationsListUseCase
) : BaseViewModel() {

    private val _locations = MutableLiveData<List<DomainLocation>>()
    val locations: LiveData<List<DomainLocation>> get() = _locations

    fun getLocations() {
        getLocationsListUseCase.getLocationsList()
            .mapLatest { locations ->
                _locations.value = locations.distinctBy { it.date }
            }.launchIn(viewModelScope)
    }
}
