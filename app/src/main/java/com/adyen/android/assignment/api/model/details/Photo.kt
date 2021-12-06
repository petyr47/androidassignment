package com.adyen.android.assignment.api.model.details

data class Photo(
    val classifications: List<String> = listOf(),
    val created_at: String = "",
    val height: Int = 0,
    val prefix: String = "",
    val suffix: String = "",
    val width: Int = 0
) {

    fun getPhotoUrl() : String = prefix + "original" + suffix

}