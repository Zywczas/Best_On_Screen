package com.zywczas.bestonscreen.model

enum class Category(category: String) {
    POPULAR ("Popular") {
        override fun toString(): String {
            return "Popular"
        }
    },

    TOP_RATED ("Top Rated") {
        override fun toString(): String {
            return "Top Rated"
        }
    },

    UPCOMING ("Upcoming") {
        override fun toString(): String {
            return "Upcoming"
        }
    },

    EMPTY_LIVEDATA ("empty"){
        override fun toString(): String {
            return "empty"
        }
    };

    abstract override fun toString() : String

}
