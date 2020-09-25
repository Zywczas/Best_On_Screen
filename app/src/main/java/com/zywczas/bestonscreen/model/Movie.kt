package com.zywczas.bestonscreen.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var id: Int = 0,
    var posterPath: String = "",
    var title: String = "",
    var voteAverage: Double = 0.0,
    var overview: String = "",
    var releaseDate: String = "",
    var genre1: String = "",
    var genre2: String = "",
    var genre3: String = "",
    var genre4: String = "",
    var genre5: String = "",
    var assignedGenresAmount: Int = 0
) : Parcelable