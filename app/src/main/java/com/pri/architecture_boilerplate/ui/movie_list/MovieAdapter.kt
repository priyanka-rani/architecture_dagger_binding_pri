package com.pri.architecture_boilerplate.ui.movie_list

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.pri.architecture_boilerplate.R
import com.pri.architecture_boilerplate.databinding.LayoutMovieBinding
import com.pri.architecture_boilerplate.util.MovieListFetchListener
import com.pri.architecture_boilerplate.vo.MovieCategory
import com.squareup.picasso.Picasso

import com.pri.architecture_boilerplate.api.IMAGE_BASE_URL
import com.pri.architecture_boilerplate.api.model.MovieListResponseModel
import com.pri.architecture_boilerplate.util.MovieListLoadedListener

class MovieAdapter internal constructor(private val listener: MovieListFetchListener?, private val data: MovieCategory) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    internal var isLoading = false

    private val visibleThreshold = 5
    internal var curPage = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutMovieBinding>(LayoutInflater.from(parent.context), R.layout.layout_movie, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        if (position == data.movieList.size) {
            binding.ivThumb.setImageResource(R.drawable.ic_load)
            binding.tvName.setText(R.string.loading)
            binding.tvYear.text = ""
        } else {
            val movie = data.movieList[position]
            binding.data = movie
            val url = IMAGE_BASE_URL + movie.backdropPath!!
            Picasso.get().load(url).into(binding.ivThumb)
        }
    }

    override fun getItemCount(): Int {
        return if (curPage < data.totalPage)
            data.movieList.size + 1
        else
            data.movieList.size
    }


    inner class ViewHolder(var binding: LayoutMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                val totalItemCount = linearLayoutManager!!.itemCount
                val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold && curPage < data.totalPage) {
                    if (listener != null) {
                        isLoading = true
                        listener.onMovieListFetched(data.endPointUrl, ++curPage, object: MovieListLoadedListener{
                            override fun onMovieListLoaded(movieList: MovieListResponseModel) {
                                Handler().postDelayed({
                                    //   remove progress item
                                    val prevSize = data.movieList.size
                                    data.movieList.addAll(movieList.movieList!!)
                                    notifyItemRangeInserted(prevSize, movieList.movieList!!.size)
                                    isLoading = false
                                }, 2000)
                            }

                        })
                    }
                }
            }
        })
    }
}
