package com.adyen.android.assignment.api.model.search

data class PlacesSearchResponse(
    val context: Context = Context(),
    val results: List<Result> = listOf()
)