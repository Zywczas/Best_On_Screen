package com.zywczas.bestonscreen.utilities

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T> constructor(private val content: T) {

    private var hasBeenHandled = false

    open fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

}