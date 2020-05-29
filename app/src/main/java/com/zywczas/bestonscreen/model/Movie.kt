package com.zywczas.bestonscreen.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
//model class for API and database
@Entity (tableName = "movies")
class Movie() : Parcelable {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "id")
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    @Expose
    var popularity: Double? = null

    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    @Expose
    var voteCount: Int? = null

    @ColumnInfo(name = "video")
    @SerializedName("video")
    @Expose
    var video: Boolean? = null

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null

    @ColumnInfo(name = "adult")
    @SerializedName("adult")
    @Expose
    var adult: Boolean? = null

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null

    @ColumnInfo(name = "original_title")
    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null

    @Ignore
    @SerializedName("genre_ids")
    @Expose
    var genreIds: List<Int>? = null

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    var title: String? = null

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    @Expose
    var overview: String? = null

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null

    @ColumnInfo(name = "genre1")
    var genre1: String? = null

    @ColumnInfo(name = "genre2")
    var genre2: String? = null

    @ColumnInfo(name = "genre3")
    var genre3: String? = null

    @ColumnInfo(name = "genre4")
    var genre4: String? = null

    @ColumnInfo(name = "genre5")
    var genre5: String? = null

    @ColumnInfo(name = "genres_amount")
    var genresAmount: Int = 0

    /**
    this converter is required as API gives just 'ids' of genres instead of names,
    I could download names of genres using TMDBService fun getMovieDetails() but it is unnecessary for now
    as I don't need any more data from getMovieDetails(),
    this function is used in Repository class
     */
    fun convertGenres(genreIds: List<Int>){
        lateinit var genreTemp: String
        loop@ for (id in genreIds) {
            genreTemp = when (id) {
                28 -> "Action"
                12 -> "Adventure"
                16 -> "Animation"
                35 -> "Comedy"
                80 -> "Crime"
                99 -> "Documentary"
                18 -> "Drama"
                10751 -> "Family"
                14 -> "Fantasy"
                36 -> "History"
                27 -> "Horror"
                10402 -> "Music"
                9648 -> "Mystery"
                10749 -> "Romance"
                878 -> "Science Fiction"
                10770 -> "TV Movie"
                53 -> "Thriller"
                10752 -> "War"
                37 -> "Western"
                else -> { Log.d("error", "problem with genre converter")
                    continue@loop
                }
            }
            when (genresAmount) {
                0 -> {genre1 = genreTemp; genresAmount = 1}
                1 -> {genre2 = genreTemp; genresAmount = 2}
                2 -> {genre3 = genreTemp; genresAmount = 3}
                3 -> {genre4 = genreTemp; genresAmount = 4}
                4 -> {genre5 = genreTemp; genresAmount = 5; break@loop}
                else -> { Log.d("error", "problem with genre converter")
                    break@loop}
            }
        }

    }

    @Ignore
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
        genresAmount = parcel.readValue(Int::class.java.classLoader) as Int
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