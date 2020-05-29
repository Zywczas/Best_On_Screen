package com.zywczas.bestonscreen.model

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