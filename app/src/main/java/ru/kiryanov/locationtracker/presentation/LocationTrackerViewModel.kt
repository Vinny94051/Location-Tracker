package ru.kiryanov.locationtracker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kiryanov.locationtracker.utils.location.LocationResult
import ru.kiryanov.locationtracker.utils.location.LocationTracker
import vlnny.base.viewModel.BaseViewModel
import javax.inject.Inject

class LocationTrackerViewModel @Inject constructor(
    private val locationTracker: LocationTracker
) : BaseViewModel() {

    private val _location = MutableLiveData<LocationResult>()
    val location: LiveData<LocationResult> get() = _location

    fun initLocationTracker() {
        locationTracker.setLocationListener { locationResult ->
            _location.value = locationResult
        }
    }
}