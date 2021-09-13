package ru.kiryanov.locationtracker.presentation

import android.content.Context
import android.util.Log
import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.kiryanov.locationtracker.domain.location.LocationTracker
import ru.kiryanov.locationtracker.domain.model.DomainLocation
import ru.kiryanov.locationtracker.domain.usecase.GetSavedLocationsListUseCase
import ru.kiryanov.locationtracker.utils.location.LocationService
import ru.kiryanov.locationtracker.utils.preference.SharedPreferencesManager
import vlnny.base.viewModel.BaseViewModel
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class LocationTrackerViewModel @Inject constructor(
    private val getSavedLocationsListUseCase: GetSavedLocationsListUseCase,
    private val locationTracker: LocationTracker,
    private val preferencesManager: SharedPreferencesManager
) : BaseViewModel() {

    init {
        initSavedLocationsFlow()
        initLocationUpdatesFlow()
    }

    private val _locations = MutableLiveData<List<DomainLocation>>()
    val locations: LiveData<List<DomainLocation>> get() = _locations

    fun isLocationUpdatingNow(): Boolean {
        return preferencesManager.get(LocationService.IS_SUBSCRIBED)
    }

    fun locationTrackingClicked(checked: Boolean) {
        if (checked) subscribeLocationUpdates() else unsubscribeLocationUpdates()
    }

    fun initLocationTracker(context: Context){
        locationTracker.initLocationTracker(context)
    }

    private fun initSavedLocationsFlow() {
        getSavedLocationsListUseCase.getLocationsList()
            .mapLatest { locations ->
                _locations.value = locations.distinctBy { it.date }
            }.launchIn(viewModelScope)
    }

    private fun initLocationUpdatesFlow() {
        locationTracker.locationUpdates
            .mapLatest { it.toDomainLocation() }
            .onEach { Log.e("ViewModel: ", "$it") }
            .launchIn(viewModelScope)
    }

    private fun subscribeLocationUpdates() {
        viewModelScope.launch {
            locationTracker.startLocationUpdates()
        }
    }

    private fun unsubscribeLocationUpdates() {
        locationTracker.stopLocationUpdates()
    }
}
