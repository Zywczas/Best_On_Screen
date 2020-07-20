package com.zywczas.bestonscreen.model.webservice

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ApiResponse {
    @SerializedName("page")
    @Expose
    var page: Int? = null

    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null

    @SerializedName("results")
    @Expose
    var movies: List<MovieFromApi>? = null
}