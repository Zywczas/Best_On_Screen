package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.webservice.MovieFromApi

/**
 * Extention function to convert MovieFromApi to general Movie class.
 * @param movieFromApi MovieFromApi which is converted to Movie
 * @return Movie
 */
fun MovieFromApi.toMovie(movieFromApi: MovieFromApi) = Movie(
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
