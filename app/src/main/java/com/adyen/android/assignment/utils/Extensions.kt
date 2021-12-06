package com.adyen.android.assignment.utils

import org.json.JSONObject
import retrofit2.HttpException
import java.net.UnknownHostException

fun Exception.resolveMessage() : String {
    var message = ""
    try {
        if (this is HttpException) {
            when{
                this.code() == 500 ->{
                    message = "Oops! Something went wrong on our servers"
                }
                this.code() == 401 -> {
                    message = "Unauthorized Operation"
                }
                else -> {
                    message = JSONObject(this.response()?.errorBody()?.string()).getString("message")
                }
            }
        } else if (this is UnknownHostException) {
            message = "Could not connect to network, please check your internet connection"
        } else {
            message = this.message ?: "The application has encountered an unknown error"
        }
    } catch (e : Exception) {
        message = "The application has encountered an unknown error"
    }
    return message
}