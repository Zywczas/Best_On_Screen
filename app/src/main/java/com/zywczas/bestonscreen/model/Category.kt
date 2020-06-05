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
    }, TO_WATCH {
        override fun toString(): String {
            return "To Watch List"
        }
    }
}