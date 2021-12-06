package com.adyen.android.assignment.api.model.search

data class Icon(
    val prefix: String = "",
    val suffix: String = ""
) {

    fun getIconUrl() = prefix + "88" + suffix

}