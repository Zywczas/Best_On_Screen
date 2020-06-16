package com.zywczas.bestonscreen.utilities

import javax.inject.Inject

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T> @Inject constructor(private val content: T) {

    var hasBeenHandled = false
    private set // allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled() : T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

}