package com.zywczas.bestonscreen.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.zywczas.bestonscreen.model.Movie


class MovieApiResponse {
    @SerializedName("page")
    @Expose
    var page: Int? = null

    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null

    @SerializedName("results")
    @Expose
    var movies: List<Movie>? = null
}