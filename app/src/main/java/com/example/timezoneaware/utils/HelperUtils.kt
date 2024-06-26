package com.example.timezoneaware.utils

class HelperUtils {

    companion object {
         fun getUserRoles(type: Int): String {
            when (type) {
                0 -> {
                    return "regular"
                }

                1 -> {
                    return "manager"
                }
                2 -> {
                    return "admin"
                }
            }
            return "No Info"
        }
    }
}