package com.pri.architecture_boilerplate.ui.movie_list

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.pri.architecture_boilerplate.R
import com.pri.architecture_boilerplate.api.model.MovieListResponseModel
import com.pri.architecture_boilerplate.databinding.ItemMainBinding
import com.pri.architecture_boilerplate.util.MovieListFetchListener
import com.pri.architecture_boilerplate.util.MovieListLoadedListener
import com.pri.architecture_boilerplate.vo.MovieCategory

import java.util.ArrayList

class MovieListAdapter(private val listener: MovieListFetchListener, private val data: List<MovieCategory>) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemMainBinding>(LayoutInflater.from(parent.context), R.layout.item_main, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val movieListViewModel = data[position]
        binding.data = movieListViewModel
        if (movieListViewModel.movieList.isEmpty()) {
            listener.onMovieListFetched(movieListViewModel.endPointUrl, 1, object : MovieListLoadedListener {
                override fun onMovieListLoaded(movieListResponse: MovieListResponseModel) {
                    movieListViewModel.movieList = ArrayList(movieListResponse.movieList!!)
                    movieListViewModel.totalPage = movieListResponse.totalPages!!
                    val movieAdapter = MovieAdapter(listener, movieListViewModel)
                    binding.rvMainList.adapter = movieAdapter
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(var binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)
}
