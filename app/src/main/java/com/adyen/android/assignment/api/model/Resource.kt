package com.adyen.android.assignment.api.model

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?, msg: String? ): Resource<T> {
            return Resource(Status.SUCCESS, data, msg)
        }

        fun <T> error(msg: String?, data: T? = null): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }

    fun resolveMessage() : String {
        return if (message.isNullOrBlank()) {
            "An Unknown error has occurred"
        } else {
            message
        }
    }
}

/**
sets status for response from api calls
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}