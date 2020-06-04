package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.localstore.MovieFromDB
import com.zywczas.bestonscreen.model.webservice.MovieFromApi

/**
 * Extension function to convert MovieFromApi to general Movie class.
 * @param movieFromApi MovieFromApi which is converted to Movie
 * @return Movie
 */
fun toMovie(movieFromApi: MovieFromApi) = Movie(
    movieFromApi.id,
    movieFromApi.popularity,
    movieFromApi.voteCount,
    movieFromApi.video,
    movieFromApi.posterPath,
    movieFromApi.adult,
    movieFromApi.backdropPath,
    movieFromApi.originalLanguage,
    movieFromApi.originalTitle,
    movieFromApi.title,
    movieFromApi.voteAverage,
    movieFromApi.overview,
    movieFromApi.releaseDate,
    movieFromApi.genre1,
    movieFromApi.genre2,
    movieFromApi.genre3,
    movieFromApi.genre4,
    movieFromApi.genre5,
    movieFromApi.genresAmount
)

/**
 * Extension function to convert MovieFromDB to general Movie class.
 * @param movieFromDB MovieFromDB which is converted to Movie
 * @return Movie
 */
    fun toMovie(movieFromDB: MovieFromDB) = Movie(
        movieFromDB.id,
        movieFromDB.popularity,
        movieFromDB.voteCount,
        movieFromDB.video,
        movieFromDB.posterPath,
        movieFromDB.adult,
        movieFromDB.backdropPath,
        movieFromDB.originalLanguage,
        movieFromDB.originalTitle,
        movieFromDB.title,
        movieFromDB.voteAverage,
        movieFromDB.overview,
        movieFromDB.releaseDate,
        movieFromDB.genre1,
        movieFromDB.genre2,
        movieFromDB.genre3,
        movieFromDB.genre4,
        movieFromDB.genre5,
        movieFromDB.genresAmount
    )

/**
 * Extension function to convert Movie to MovieFromDB SQL entity class.
 * @param movie Movie which is converted to Movie
 * @return Movie
 */
    fun toMovieFromDB(movie: Movie) = MovieFromDB(
        movie.id,
        movie.popularity,
        movie.voteCount,
        movie.video,
        movie.posterPath,
        movie.adult,
        movie.backdropPath,
        movie.originalLanguage,
        movie.originalTitle,
        movie.title,
        movie.voteAverage,
        movie.overview,
        movie.releaseDate,
        movie.genre1,
        movie.genre2,
        movie.genre3,
        movie.genre4,
        movie.genre5,
        movie.genresAmount
    )
