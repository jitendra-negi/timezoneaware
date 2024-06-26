package com.example.timezoneaware.utils


fun String.isEmailValid() =
    android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
