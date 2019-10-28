package com.pri.architecture_boilerplate.ui.movie_list

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.ViewModelProvider
import com.pri.architecture_boilerplate.api.NOW_PLAYING
import com.pri.architecture_boilerplate.api.TOP_RATED
import com.pri.architecture_boilerplate.api.UPCOMING
import com.pri.architecture_boilerplate.ui.common.BaseFragment
import com.pri.architecture_boilerplate.util.MovieListFetchListener
import com.pri.architecture_boilerplate.util.MovieListLoadedListener
import com.pri.architecture_boilerplate.vo.MovieCategory
import com.pri.architecture_boilerplate.R
import com.pri.architecture_boilerplate.api.model.MovieListResponseModel
import com.pri.architecture_boilerplate.databinding.ActivityMainBinding
import com.pri.architecture_boilerplate.vo.Resource
import com.pri.architecture_boilerplate.vo.Status
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule


class MovieCategoryListFragment : BaseFragment<ActivityMainBinding, MovieCategoryListViewModel>(), MovieListFetchListener {
    override val layoutId: Int
        get() = R.layout.activity_main
    override val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
                .get(MovieCategoryListViewModel::class.java)
    }


    private val listeners: HashMap<String, MovieListLoadedListener> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMovieList()
        viewModel.topMovieList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let { resource ->
                if (resource.status == Status.ERROR) {
                    if (isNetworkConnected)
                        resource.message?.let { it1 -> showSnack(it1) }
                    else {
                        showSnack(R.string.no_internet_error)
                    }
                } else {
                    movieListLoaded(TOP_RATED, resource)
                }
            }
        })
        viewModel.nowMovieList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let { resource ->
                if (resource.status == Status.ERROR) {
                    if (isNetworkConnected)
                        resource.message?.let { it1 -> showSnack(it1) }
                    else {
                        showSnack(R.string.no_internet_error)
                    }
                } else {
                    movieListLoaded(NOW_PLAYING, resource)
                }
            }
        })
        viewModel.upComingmovieList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let { resource ->
                if (resource.status == Status.ERROR) {
                    if (isNetworkConnected)
                        resource.message?.let { it1 -> showSnack(it1) }
                    else {
                        showSnack(R.string.no_internet_error)
                    }
                } else {
                    movieListLoaded(UPCOMING, resource)
                }
            }
        })
        viewModel.searchedMovieList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let { resource ->
                if (resource.status == Status.ERROR) {
                    if (isNetworkConnected)
                        resource.message?.let { it1 -> showSnack(it1) }
                    else {
                        showSnack(R.string.no_internet_error)
                    }
                } else {
                    val movieList = resource.data?.movieList
                    if (movieList.isNullOrEmpty()) {
                        viewDataBinding.rvSearchList.visibility = View.GONE

                    } else {
                        //                        listLoadedListener!!.onMovieListLoaded(movieList)
                        val adapter = MovieSearchAdapter(movieList)
                        viewDataBinding.rvSearchList.adapter = adapter
                        viewDataBinding.rvSearchList.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun movieListLoaded(endpoint: String, resource: Resource<MovieListResponseModel>) {
        val movieList = resource.data?.movieList
        if (movieList.isNullOrEmpty()) {
            resource.message?.let { showSnack(it) }
        } else {
            val listener = listeners.get(endpoint)
            listener?.onMovieListLoaded(resource.data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(requireActivity().componentName))

        // When using the support library, the setOnActionExpandListener() method is
// static and accepts the MenuItem object as an argument
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.search), object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                //the searchview has been closed
                viewDataBinding.rvAlbumList.visibility = View.VISIBLE
                viewDataBinding.rvSearchList.visibility = View.GONE
                return true  // Return true to collapse action view
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                viewDataBinding.rvAlbumList.visibility = View.GONE
                return true  // Return true to expand action view
            }
        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            var timer = Timer()

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                timer.cancel()
                val sleep = when (newText.length) {
                    1 -> 1000L
                    2, 3 -> 700L
                    4, 5 -> 500L
                    else -> 300L
                }
                timer = Timer()
                timer.schedule(sleep) {
                    if (!newText.isNullOrEmpty()) {
                        // search
                        searchMovieList(newText)
                    }
                }
                return true
            }
        })
        searchView.queryHint = getString(R.string.search_title)

    }

    private fun loadMovieList() {
        val movieListList: ArrayList<MovieCategory> = ArrayList()
        movieListList.add(MovieCategory("TOP RATED", TOP_RATED))
        movieListList.add(MovieCategory("NOW PLAYING", NOW_PLAYING))
        movieListList.add(MovieCategory("UPCOMING", UPCOMING))

        val adapter = MovieListAdapter(this@MovieCategoryListFragment, movieListList)
        viewDataBinding.rvAlbumList.adapter = adapter

    }

    override fun onMovieListFetched(endPoint: String, page: Int, listLoadedListener: MovieListLoadedListener) {
        viewModel.getMovies(endPoint, page)
        listeners[endPoint] = listLoadedListener
    }

    fun searchMovieList(query: String) {
        viewModel.searchMovies(query)
    }


/*    private fun clickSubMenuItem(navigateId: Int) {
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
            launchSingleTop = true
        }
        navController().navigate(navigateId, null, options)
    }*/
}
