package com.pri.architecture_boilerplate.ui.movie_list

import android.hardware.usb.UsbEndpoint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.pri.architecture_boilerplate.api.NOW_PLAYING
import com.pri.architecture_boilerplate.api.TOP_RATED
import com.pri.architecture_boilerplate.api.UPCOMING
import com.pri.architecture_boilerplate.repository.AuthRepository
import com.pri.architecture_boilerplate.util.AbsentLiveData
import javax.inject.Inject

open class MovieCategoryListViewModel @Inject constructor(authRepository: AuthRepository) :
        ViewModel() {
    private val topRatedMovies: MutableLiveData<Int> = MutableLiveData()
    private val nowMovies: MutableLiveData<Int> = MutableLiveData()
    private val upcomingMovies: MutableLiveData<Int> = MutableLiveData()
    private val searchQuery: MutableLiveData<String> = MutableLiveData()
    val topMovieList = Transformations.switchMap(topRatedMovies){
        if(it == null) AbsentLiveData.create()
        else authRepository.getMovieList(TOP_RATED, it)
    }
     val nowMovieList = Transformations.switchMap(nowMovies){
        if(it == null) AbsentLiveData.create()
        else authRepository.getMovieList(NOW_PLAYING, it)
    }
     val upComingmovieList = Transformations.switchMap(upcomingMovies){
        if(it == null) AbsentLiveData.create()
        else authRepository.getMovieList(UPCOMING, it)
    }

    val searchedMovieList = Transformations.switchMap(searchQuery){
        if(it == null) AbsentLiveData.create()
        else authRepository.searchMovieList(it)
    }

    fun getMovies(endpoint: String, page: Int) {
        when(endpoint){
            TOP_RATED-> topRatedMovies.value = page
            NOW_PLAYING-> nowMovies.value = page
            UPCOMING-> upcomingMovies.value = page
        }
    }
    fun searchMovies(query:String) {
        searchQuery.postValue(query)
    }

}
