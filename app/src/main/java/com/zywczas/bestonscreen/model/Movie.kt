package com.zywczas.bestonscreen.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val posterPath: String,
    val title: String,
    val voteAverage: Double,
    val overview: String,
    val releaseDate: String,
    val genre1: String,
    val genre2: String,
    val genre3: String,
    val genre4: String,
    val genre5: String,
    val assignedGenresAmount: Int
) : Parcelable