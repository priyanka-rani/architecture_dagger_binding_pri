package com.pri.architecture_boilerplate.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pri.architecture_boilerplate.vo.Movie

import java.io.Serializable

class MovieListResponseModel : Serializable {

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
    var movieList: List<Movie>? = null

    companion object {
        private const val serialVersionUID = 463478823370456371L
    }

}