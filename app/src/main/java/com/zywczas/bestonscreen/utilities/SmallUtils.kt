package com.zywczas.bestonscreen.utilities

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Extension function replacing Toast.makeText(...)
 */
fun Context.showToast(message : String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun logD(message: String) = Log.d("film error", message)
fun logD(t: Throwable?) = Log.d("film error", "${t?.localizedMessage}")

/**
 * Extension function allowing to observe Live Data once and remove Observer straight away to
 * not allow user to create multiple observers by clicking the same button few times or by changing
 * categories.
 */
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver( this)
        }
    })
}


