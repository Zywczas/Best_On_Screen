package com.zywczas.bestonscreen.model

import android.os.Parcel
import android.os.Parcelable

/**
 * General movie class to be used with UI. All movies from REST API and SQLite will be converted
 * to this class. But this class will be converted to MovieFromDB if a user adds it to "To Watch List"
 */
data class Movie (var id: Int?,
                  var popularity: Double?,
                  var voteCount: Int?,
                  var video: Boolean?,
                  var posterPath: String?,
                  var adult: Boolean?,
                  var backdropPath: String?,
                  var originalLanguage: String?,
                  var originalTitle: String?,
                  var title: String?,
                  var voteAverage: Double?,
                  var overview: String?,
                  var releaseDate: String?,
                  var genre1: String?,
                  var genre2: String?,
                  var genre3: String?,
                  var genre4: String?,
                  var genre5: String?,
                  var genresAmount: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(popularity)
        parcel.writeValue(voteCount)
        parcel.writeValue(video)
        parcel.writeString(posterPath)
        parcel.writeValue(adult)
        parcel.writeString(backdropPath)
        parcel.writeString(originalLanguage)
        parcel.writeString(originalTitle)
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

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }

}