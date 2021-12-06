package com.adyen.android.assignment.api.model.details

data class Location(
    val address: String = "",
    val country: String = "",
    val cross_street: String = "",
    val locality: String = "",
    val neighborhood: List<String> = listOf(),
    val postcode: String = "",
    val region: String = ""
)