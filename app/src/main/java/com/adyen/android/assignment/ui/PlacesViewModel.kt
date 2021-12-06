package com.adyen.android.assignment.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.PlacesRepository
import com.adyen.android.assignment.api.model.Resource
import com.adyen.android.assignment.api.model.details.PlaceDetailsResponse
import com.adyen.android.assignment.api.model.search.PlacesSearchResponse
import com.adyen.android.assignment.api.model.search.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PlacesViewModel : ViewModel() {

    val TAG = "PlacesViewModel"
    private val repository = PlacesRepository()
    val requestCode = 23
    var lastLatitude = 0.0
    var lastLongitude = 0.0

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationResult?.lastLocation?.let {
                // use latitude and longitude as per your need
                if (lastLatitude != it.latitude || lastLongitude != it.longitude){
                    fetchPlaces(it.latitude, it.longitude)
                }
                lastLongitude = it.longitude
                lastLatitude = it.latitude
            }
        }
    }


    // FusedLocationProviderClient - Main class for receiving location updates.
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var locationRequest = LocationRequest.create().apply {
        // Sets the desired interval for
        // Sets the desired interval for
        // active location updates.
        // This interval is inexact.
        interval = TimeUnit.MINUTES.toMillis(10)

        // Sets the fastest rate for active location updates.
        // This interval is exact, and your application will never
        // receive updates more frequently than this value
        fastestInterval = TimeUnit.MINUTES.toMillis(5)

        // Sets the maximum time when batched location
        // updates are delivered. Updates may be
        // delivered sooner than this interval
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)

        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    var selectedItem = Result()


    private val _places = MutableLiveData<Resource<PlacesSearchResponse>>()
    val places: LiveData<Resource<PlacesSearchResponse>>
        get() = _places

    private val _placeDetails = MutableLiveData<Resource<PlaceDetailsResponse>>()
    val placeDetails: LiveData<Resource<PlaceDetailsResponse>>
        get() = _placeDetails


    fun fetchPlaces(latitude: Double? = 52.376510, longitude : Double? = 4.905890) {
        _places.postValue(Resource.loading())
        viewModelScope.launch {
            val result = repository.fetchPlaces(latitude ?:  52.376510, longitude ?: 4.905890)
            if (result.success) {
                if (result.response?.results.isNullOrEmpty()) {
                    _places.postValue(Resource.error("No results found!"))
                } else {
                    _places.postValue(Resource.success(result.response, "success"))
                }
            } else {
                _places.postValue(Resource.error(result.message))
            }
        }
    }

    fun fetchPlaceDetails() {
        val id = selectedItem.fsq_id
        Log.d("details fetc", "ID::$id")
        viewModelScope.launch {
            val result = repository.getDetailsForId(id)
            if (result.success) {
                _placeDetails.postValue(Resource.success(result.response, "success"))
            } else {
                _placeDetails.postValue(Resource.error(result.message))
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        removeTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Location Callback removed.")
            } else {
                Log.d(TAG, "Failed to remove Location Callback.")
            }
        }
    }



}