package com.zywczas.bestonscreen.utilities

import android.content.Context
import android.util.Log
import android.widget.Toast


fun Context.showToast(message : String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun logD(message: String) = Log.d("film error", message)

fun logD(t: Throwable?) = Log.d("film error", "${t?.localizedMessage}")

