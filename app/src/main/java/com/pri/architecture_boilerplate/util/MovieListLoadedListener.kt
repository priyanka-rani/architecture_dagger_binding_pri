package com.pri.architecture_boilerplate.util

import com.pri.architecture_boilerplate.api.model.MovieListResponseModel


interface MovieListLoadedListener {
    fun onMovieListLoaded(movieListResponse: MovieListResponseModel)
}
