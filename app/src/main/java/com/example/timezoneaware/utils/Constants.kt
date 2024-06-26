package com.example.timezoneaware.utils

class Constants {

    companion object {

        const val BASE_URL = "https://timezone-server.herokuapp.com/"

        const val TAG: String = "AppDebug"

        const val NETWORK_TIMEOUT = 6000L
        const val CACHE_TIMEOUT = 2000L
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing

        const val GALLERY_REQUEST_CODE = 201
        const val PERMISSIONS_REQUEST_READ_STORAGE: Int = 301
        const val ERROR_UNKNOWN = "Unknown error"
        const val NETWORK_ERROR = "IOException"
        const val NETWORK_ERROR_UNKNOWN = "Unknown network error"
        const val NETWORK_ERROR_TIMEOUT = "Network timeout"

    }
}