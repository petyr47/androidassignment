package com.adyen.android.assignment.api.model.details

data class PlaceDetailsResponse(
    val categories: List<Category> = listOf(),
    val description: String = "",
    val geocodes: Geocodes = Geocodes(),
    val location: Location = Location(),
    val name: String = "",
    val photos: List<Photo> = listOf(),
    val price: Int = 0,
    val rating: Double = 0.0,
    val tel: String = "",
    val timezone: String = "",
    val website: String = "",
    val fsq_id : String = ""
)