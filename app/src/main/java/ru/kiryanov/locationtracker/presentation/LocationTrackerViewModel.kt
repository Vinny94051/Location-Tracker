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
import vlnny.base.viewModel.BaseViewModel
import java.util.*
import javax.inject.Inject

class LocationTrackerViewModel @Inject constructor(
    private val locationTracker: LocationTracker,
    private val saveLocationUseCase: SaveLocationUseCase,
    private val getLocationsListUseCase: GetLocationsListUseCase
) : BaseViewModel() {

    private val _locations = MutableLiveData<List<DomainLocation>>()
    val locations: LiveData<List<DomainLocation>> get() = _locations

    fun getLocations() {
        viewModelScope.launch {
            _locations.value = getLocationsListUseCase
                .getLocationsList().distinctBy { it.date }
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
            date = Calendar.getInstance().time
        )
        else -> null
    }
}
