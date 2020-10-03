package com.zywczas.bestonscreen.model

enum class Category {

    TOP_RATED {
        override fun toString(): String {
            return "Top Rated"
        }
    },

    POPULAR {
        override fun toString(): String {
            return "Popular"
        }
    },

    UPCOMING {
        override fun toString(): String {
            return "Upcoming"
        }
    };

}
