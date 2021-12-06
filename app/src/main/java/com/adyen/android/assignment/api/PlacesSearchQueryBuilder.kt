package com.adyen.android.assignment.api

class PlacesSearchQueryBuilder : PlacesQueryBuilder() {
    private var latitudeLongitude: String? = null
    private var section: String? = null
    private var sortByDistance : Boolean = true
    private var sortByPopularity : Boolean = true
    private var limit : Int = 50

    fun setLatitudeLongitude(latitude: Double, longitude: Double): PlacesSearchQueryBuilder {
        this.latitudeLongitude = "$latitude,$longitude"
        return this
    }

    fun setProperties(section: String, sortByDistance: Boolean = true, sortByPopularity: Boolean = true) : PlacesSearchQueryBuilder {
        this.section = section
        this.sortByDistance = sortByDistance
        this.sortByPopularity = sortByPopularity
        return this
    }

    override fun putQueryParams(queryParams: MutableMap<String, String>) {
        latitudeLongitude?.apply { queryParams["ll"] = this }
        section?.apply { queryParams["section"] = this }
        sortByDistance.apply{ queryParams["sortByDistance"] = "$this" }
        sortByPopularity.apply { queryParams["sortByPopularity"] = "$this" }
        limit.apply { queryParams["limit"] = "$this" }
    }
}
