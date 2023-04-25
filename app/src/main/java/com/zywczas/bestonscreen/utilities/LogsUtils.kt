package com.zywczas.bestonscreen.utilities

import android.util.Log

private const val LOG_TAG = "BestOnScreenTag"

fun Any.logD(e: Throwable) {
    Log.d(LOG_TAG, "${this.javaClass.simpleName}: $e")
}
