package com.udacity.asteroidradar.util

import android.view.View


fun View.visibleIf(condition: Boolean) = if (condition) {
    visibility = View.VISIBLE
} else {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}