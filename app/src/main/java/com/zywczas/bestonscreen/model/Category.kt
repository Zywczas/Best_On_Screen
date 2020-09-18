package com.zywczas.bestonscreen.model

enum class Category {

    POPULAR {
        override fun toString(): String {
            return "Popular"
        }
    },
    TOP_RATED {
        override fun toString(): String {
            return "Top Rated"
        }
    },
    UPCOMING {
        override fun toString(): String {
            return "Upcoming"
        }
    }

}
