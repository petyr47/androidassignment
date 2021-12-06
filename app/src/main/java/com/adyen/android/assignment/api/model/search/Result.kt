package com.adyen.android.assignment.api.model.search

data class Result(
    val categories: List<Category> = listOf(),
    val chains: List<Any> = listOf(),
    val distance: Int = 0,
    val fsq_id: String = "",
    val geocodes: Geocodes = Geocodes(),
    val location: Location = Location(),
    val name: String = "",
    val related_places: RelatedPlaces = RelatedPlaces(),
    val timezone: String = ""
)