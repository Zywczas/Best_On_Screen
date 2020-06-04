package com.zywczas.bestonscreen.model

import android.os.Parcel
import android.os.Parcelable

/**
 * General movie class to be used with UI. All movies from REST API and SQLite will be converted
 * to this class. But this class will be converted to MovieFromDB (and then to database) if a user
 * adds it to "To Watch List".
 */
class Movie () : Parcelable {

    var id: Int? = null
    var popularity: Double? = null
    var voteCount: Int? = null
    var video: Boolean? = null
    var posterPath: String? = null
    var adult: Boolean? = null
    var backdropPath: String? = null
    var originalLanguage: String? = null
    var originalTitle: String? = null
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

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        popularity = parcel.readValue(Double::class.java.classLoader) as? Double
        voteCount = parcel.readValue(Int::class.java.classLoader) as? Int
        video = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        posterPath = parcel.readString()
        adult = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        backdropPath = parcel.readString()
        originalLanguage = parcel.readString()
        originalTitle = parcel.readString()
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

    constructor(
         id: Int?,
         popularity: Double?,
         voteCount: Int?,
         video: Boolean?,
         posterPath: String?,
         adult: Boolean?,
         backdropPath: String?,
         originalLanguage: String?,
         originalTitle: String?,
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
        this.popularity = popularity
        this.voteCount = voteCount
        this.video = video
        this.posterPath = posterPath
        this.adult = adult
        this.backdropPath = backdropPath
        this.originalLanguage = originalLanguage
        this.originalTitle = originalTitle
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