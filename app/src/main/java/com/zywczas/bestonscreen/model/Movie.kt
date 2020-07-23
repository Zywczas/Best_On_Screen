package com.zywczas.bestonscreen.model

import android.os.Parcel
import android.os.Parcelable

class Movie () : Parcelable {

    var id: Int = 0
    var posterPath: String = ""
    var title: String = ""
    var voteAverage: Double = 0.0
    var overview: String = ""
    var releaseDate: String = ""
    var genre1: String = ""
    var genre2: String = ""
    var genre3: String = ""
    var genre4: String = ""
    var genre5: String = ""
    var assignedGenresAmount: Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        posterPath = parcel.readString().toString()
        title = parcel.readString().toString()
        voteAverage = parcel.readDouble()
        overview = parcel.readString().toString()
        releaseDate = parcel.readString().toString()
        genre1 = parcel.readString().toString()
        genre2 = parcel.readString().toString()
        genre3 = parcel.readString().toString()
        genre4 = parcel.readString().toString()
        genre5 = parcel.readString().toString()
        assignedGenresAmount = parcel.readInt()
    }

    constructor(
        id: Int,
        posterPath: String,
        title: String,
        voteAverage: Double,
        overview: String,
        releaseDate: String,
        genre1: String,
        genre2: String,
        genre3: String,
        genre4: String,
        genre5: String,
        genresAmount: Int
    ) : this() {
        this.id = id
        this.posterPath = posterPath
        this.title = title
        this.voteAverage = voteAverage
        this.overview = overview
        this.releaseDate = releaseDate
        this.genre1 = genre1
        this.genre2 = genre2
        this.genre3 = genre3
        this.genre4 = genre4
        this.genre5 = genre5
        this.assignedGenresAmount = genresAmount
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(posterPath)
        parcel.writeString(title)
        parcel.writeDouble(voteAverage)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
        parcel.writeString(genre1)
        parcel.writeString(genre2)
        parcel.writeString(genre3)
        parcel.writeString(genre4)
        parcel.writeString(genre5)
        parcel.writeInt(assignedGenresAmount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}
