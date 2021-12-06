package com.adyen.android.assignment.api.model

data class ResponseWrapper<T>(
    val success : Boolean = true,
    val message : String = "",
    val response : T? = null
)