package com.pri.architecture_boilerplate.vo



class MovieCategory(var title: String, var endPointUrl: String){
    var movieList= ArrayList<Movie>()
    var totalPage:Int = 0
}