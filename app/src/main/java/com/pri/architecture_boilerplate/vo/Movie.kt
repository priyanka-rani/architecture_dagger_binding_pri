package com.pri.architecture_boilerplate.vo

import androidx.room.Entity
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.pri.architecture_boilerplate.db.CBTypeConverters

import java.io.Serializable
@Entity(primaryKeys = ["id"], tableName = "movie")
@TypeConverters(CBTypeConverters::class)
class Movie : Serializable {

    @SerializedName("vote_count")
    @Expose
    var voteCount: Int? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("video")
    @Expose
    var video: Boolean? = null
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("popularity")
    @Expose
    var popularity: Double? = null
    @SerializedName("poster_path")
    @Expose
    var posterPath: String? = null
    @SerializedName("original_language")
    @Expose
    var originalLanguage: String? = null
    @SerializedName("original_title")
    @Expose
    var originalTitle: String? = null
    @SerializedName("genre_ids")
    @Expose
    var genreIds: List<Int>? = null
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String? = null
    @SerializedName("adult")
    @Expose
    var adult: Boolean? = null
    @SerializedName("overview")
    @Expose
    var overview: String? = null
    @SerializedName("release_date")
    @Expose
    var releaseDate: String? = null
    val year: String?
        get() {
            try {
                return releaseDate!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            } catch (e: Exception) {
                return releaseDate
            }

        }

    companion object {
        private const val serialVersionUID = 4413737257470429717L
    }

}
