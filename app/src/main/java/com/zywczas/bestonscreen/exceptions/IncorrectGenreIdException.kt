package com.zywczas.bestonscreen.exceptions

class IncorrectGenreIdException(override val message: String) : Exception() {
    override fun getLocalizedMessage(): String? {
        return message
    }
}