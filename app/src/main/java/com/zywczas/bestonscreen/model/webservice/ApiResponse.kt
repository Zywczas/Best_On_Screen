package com.zywczas.bestonscreen.model.webservice

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiResponse(

    @SerializedName("results")
    @Expose
    var movies: List<MovieFromApi>? = null

)


