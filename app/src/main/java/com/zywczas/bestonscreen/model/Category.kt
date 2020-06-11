package com.zywczas.bestonscreen.model

/**
 * Enum class to control categories of displayed movies: Popular, Upcoming, etc.
 */
enum class Category {
    POPULAR {
        override fun toString(): String {
            return "Popular"
        }
    }, UPCOMING {
        override fun toString(): String {
            return "Upcoming"
        }
    }, TOP_RATED {
        override fun toString(): String {
            return "Top rated"
        }
    }

}