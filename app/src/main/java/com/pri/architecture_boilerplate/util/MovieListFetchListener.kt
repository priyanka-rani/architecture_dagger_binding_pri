package com.pri.architecture_boilerplate.util

interface MovieListFetchListener {
    fun onMovieListFetched(endPoint: String, page: Int, listLoadedListener: MovieListLoadedListener)
}
