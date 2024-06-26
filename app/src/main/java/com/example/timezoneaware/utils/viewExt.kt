package com.example.timezoneaware.utils

import android.view.View

fun View.show(display: Boolean) {
    if (display) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}