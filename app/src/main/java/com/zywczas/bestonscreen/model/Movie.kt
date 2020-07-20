package com.zywczas.bestonscreen.model

import android.os.Parcel
import android.os.Parcelable

class Movie () : Parcelable {

    var id: Int? = null
    var posterPath: String? = null
    var title: String? = null
    var voteAverage: Double? = null
    var overview: String? = null
    var releaseDate: String? = null
    var genre1: String? = null
    var genre2: String? = null
    var genre3: String? = null
    var genre4: String? = null
    var genre5: String? = null
    var genresAmount: Int? = null

    constructor(
        id: Int?,
        posterPath: String?,
        title: String?,
        voteAverage: Double?,
        overview: String?,
        releaseDate: String?,
        genre1: String?,
        genre2: String?,
        genre3: String?,
        genre4: String?,
        genre5: String?,
        genresAmount: Int?
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
        this.genresAmount = genresAmount
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        posterPath = parcel.readString()
        title = parcel.readString()
        voteAverage = parcel.readValue(Double::class.java.classLoader) as? Double
        overview = parcel.readString()
        releaseDate = parcel.readString()
        genre1 = parcel.readString()
        genre2 = parcel.readString()
        genre3 = parcel.readString()
        genre4 = parcel.readString()
        genre5 = parcel.readString()
        genresAmount = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(posterPath)
        parcel.writeString(title)
        parcel.writeValue(voteAverage)
        parcel.writeString(overview)
        parcel.writeString(releaseDate)
        parcel.writeString(genre1)
        parcel.writeString(genre2)
        parcel.writeString(genre3)
        parcel.writeString(genre4)
        parcel.writeString(genre5)
        parcel.writeValue(genresAmount)
    }

    override fun describeContents(): Int {
        return 0
    }

}