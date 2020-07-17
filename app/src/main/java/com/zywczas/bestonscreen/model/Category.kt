package com.zywczas.bestonscreen.model

enum class Category {
    POPULAR {
        override fun toString(): String {
            return "POPULAR"
        }
    },

    TOP_RATED {
        override fun toString(): String {
            return "TOP_RATED"
        }
    },

    UPCOMING {
        override fun toString(): String {
            return "UPCOMING"
        }
    },

    EMPTY_LIVEDATA {
        override fun toString(): String {
            return "EMPTY_LIVEDATA"
        }
    };

    abstract override fun toString() : String

}
