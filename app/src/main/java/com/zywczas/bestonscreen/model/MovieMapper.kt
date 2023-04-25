package com.zywczas.bestonscreen.model

import com.zywczas.bestonscreen.model.db.LocalMovie
import com.zywczas.bestonscreen.model.webservice.NetworkMovie

fun NetworkMovie.toMovie(): Movie = Movie(
    id = this.id ?: 0,
    posterPath = this.posterPath ?: "",
    title = this.title ?: "",
    voteAverage = this.voteAverage ?: 0.0,
    overview = this.overview ?: "",
    releaseDate = this.releaseDate ?: "",
    genresDescription = this.getGenresDescription()
)

fun LocalMovie.toMovie(): Movie = Movie(
    id = this.id,
    posterPath = this.posterPath,
    title = this.title,
    voteAverage = this.voteAverage,
    overview = this.overview,
    releaseDate = this.releaseDate,
    genresDescription = this.genresDescription
)

fun Movie.toLocalMovie(): LocalMovie = LocalMovie(
    id = this.id,
    posterPath = this.posterPath,
    title = this.title,
    voteAverage = this.voteAverage,
    overview = this.overview,
    releaseDate = this.releaseDate,
    genresDescription = this.genresDescription
)
