package com.adyen.android.assignment.api.model.search

data class RelatedPlaces(
    val children: List<Children> = listOf(),
    val parent: Parent = Parent()
)