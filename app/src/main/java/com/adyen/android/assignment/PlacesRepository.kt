package com.adyen.android.assignment

import com.adyen.android.assignment.api.PlaceDetailsQueryBuilder
import com.adyen.android.assignment.api.PlacesService
import com.adyen.android.assignment.api.PlacesSearchQueryBuilder
import com.adyen.android.assignment.api.model.ResponseWrapper
import com.adyen.android.assignment.api.model.details.PlaceDetailsResponse
import com.adyen.android.assignment.api.model.search.PlacesSearchResponse
import com.adyen.android.assignment.utils.resolveMessage

class PlacesRepository {

    private val service = PlacesService.instance

    suspend fun fetchPlaces(latitude: Double = 52.376510, longitude : Double = 4.905890) : ResponseWrapper<PlacesSearchResponse> {
        try {
            val query = PlacesSearchQueryBuilder()
                .setLatitudeLongitude(latitude, longitude)
                .setProperties("food")
                .build()
            val response = service.getPlaces(query)
            return ResponseWrapper(response = response)
        } catch (e : Exception){
            e.printStackTrace()
            return ResponseWrapper(false, e.resolveMessage(), null)
        }
    }

    suspend fun getDetailsForId(fsqId : String) : ResponseWrapper<PlaceDetailsResponse> {
        try {
            val query = PlaceDetailsQueryBuilder().apply {
                fourSquareId = fsqId
                setFields()
            }.build()
            val response = service.getPlaceDetails(fsqId, query)
            return ResponseWrapper(response = response)
        } catch (e : Exception){
            e.printStackTrace()
            return ResponseWrapper(false, e.resolveMessage())
        }
    }

}