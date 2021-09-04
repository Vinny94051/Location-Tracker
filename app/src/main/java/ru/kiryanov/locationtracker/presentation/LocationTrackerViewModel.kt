package ru.kiryanov.locationtracker.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kiryanov.locationtracker.domain.DomainLocation
import ru.kiryanov.locationtracker.domain.usecase.GetLocationsListUseCase
import ru.kiryanov.locationtracker.domain.usecase.SaveLocationUseCase
import ru.kiryanov.locationtracker.utils.location.LocationResult
import ru.kiryanov.locationtracker.utils.location.LocationTracker
import vlnny.base.ext.currentDate
import vlnny.base.ext.toDate
import vlnny.base.viewModel.BaseViewModel
import java.time.LocalDateTime
import javax.inject.Inject

class LocationTrackerViewModel @Inject constructor(
    private val locationTracker: LocationTracker,
    private val saveLocationUseCase: SaveLocationUseCase,
    private val getLocationsListUseCase: GetLocationsListUseCase
) : BaseViewModel() {

    private val _location = MutableLiveData<LocationResult>()
    val location: LiveData<LocationResult> get() = _location

    private val _locations = MutableLiveData<List<DomainLocation>>()
    val locations: LiveData<List<DomainLocation>> get() = _locations

    init {
        getLocations()
    }

    fun initLocationTracker() {
        locationTracker.setLocationListener { locationResult ->
            saveLocation(locationResult)
            _location.value = locationResult
        }
    }

    private fun getLocations() {
        viewModelScope.launch {
            _locations.value = getLocationsListUseCase
                .getLocationsList()
        }
    }

    private fun saveLocation(location: LocationResult) {
        location.toDomainLocation()?.let {
            viewModelScope.launch {
                saveLocationUseCase.saveLocation(it)
            }
        }
    }
}

@SuppressLint("NewApi")
fun LocationResult.toDomainLocation(): DomainLocation? {
    return when (this) {
        is LocationResult.Success -> DomainLocation(
            location = this.location,
            date = currentDate.toDate()
        )
        else -> null
    }
}
