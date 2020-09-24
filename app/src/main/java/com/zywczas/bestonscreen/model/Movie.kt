package com.zywczas.bestonscreen.model

import android.os.Parcel
import android.os.Parcelable
//todo sprawdzic czy jak dam @Parcelize to wystarczy zamiast dawanie ich metod
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
) : Parcelable {

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
