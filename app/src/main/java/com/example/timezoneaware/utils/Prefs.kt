package com.example.timezoneaware.utils

import android.content.Context
import android.content.SharedPreferences

class Prefs (context: Context) {
    val PREFS_FILENAME = "timezone_aware.prefs"
    val USER_TYPE = "isAdmin"
    val USER_ID = "userId"
    val IS_LOGGED_IN = "isLoggedIn"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var userTypePref: Int
        get() = prefs.getInt(USER_TYPE, 0)
        set(value) = prefs.edit().putInt(USER_TYPE, value).apply()

    var userIdPref: Int
        get() = prefs.getInt(USER_ID, 0)
        set(value) = prefs.edit().putInt(USER_ID, value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()
}