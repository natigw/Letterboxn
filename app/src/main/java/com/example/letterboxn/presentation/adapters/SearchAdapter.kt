package com.example.letterboxn.presentation.adapters

import android.util.Log
import android.view.View
import androidx.paging.LOG_TAG
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.databinding.FragmentSearchExploreBinding
import com.example.letterboxn.databinding.SampleSearchBinding
import com.example.letterboxn.domain.model.SearchItem

//class SearchAdapter(
//    val bindinqa : FragmentSearchExploreBinding,
//    val onClick: (Int) -> Unit
//) : BaseAdapter<SampleSearchBinding>(SampleSearchBinding::inflate) {
//
//    var movies: List<SearchItem> = emptyList()
//
//    override fun getItemCount(): Int {
//        if (movies.isEmpty()) bindinqa.textNoResults.visibility = View.VISIBLE
//        return movies.size
//    }
//
//    override fun onBindLight(binding: SampleSearchBinding, position: Int) {
//        val searchitem = movies[position]
//
//        with(binding) {
//            Glide.with(imageSearchposter)
//                .load("https://image.tmdb.org/t/p/w500" + searchitem.moviePoster)
//                .placeholder(R.drawable.custom_poster_shape)
//                .error(R.drawable.search_placeholder)
//.transition(DrawableTransitionOptions.withCrossFade())
//                .into(imageSearchposter)
//            textMovietitleSearch.text = searchitem.movieTitle
//            if (searchitem.movieRating != 0f) {
//                textRatingSearch.text = (searchitem.movieRating/2).toString().substring(0,3)
//                ratingBarSearch.rating = (searchitem.movieRating/2).toFloat()
//            } else {
//                textRatingSearch.text = "N/A"
//                ratingBarSearch.visibility = View.GONE
//            }
//            textMoviereleasedateSearch.text = searchitem.movieReleaseDate
//            root.setOnClickListener {
//                onClick(searchitem.movieId)
//            }
//        }
//    }
//
//    fun updateAdapter(newResponse: List<SearchItem>) {
//        movies = newResponse
//        notifyDataSetChanged()
//    }
//}


class SearchAdapter (
    val isMulti : Boolean,
    val bindinqa : FragmentSearchExploreBinding,
    var movies: List<SearchItem>,
    val onClick: (Int) -> Unit
) : BaseAdapter<SampleSearchBinding>(SampleSearchBinding::inflate) {

    override fun getItemCount(): Int {
        if (movies.isEmpty()) bindinqa.textNoResults.visibility = View.VISIBLE
        return movies.size
    }

    override fun onBindLight(binding: SampleSearchBinding, position: Int) {

        val searchitem = movies[position]

        with(binding) {
            if (isMulti) binding.chipMediaTypeSearch.visibility = View.VISIBLE
            Log.e("chip", isMulti.toString())
            Glide.with(imageSearchposter)
                .load("https://image.tmdb.org/t/p/w500" + searchitem.moviePoster)
                .placeholder(R.drawable.custom_poster_shape)
                .error(R.drawable.default_movie_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageSearchposter)
            textMovietitleSearch.text = searchitem.movieTitle
            if (searchitem.movieRating != 0f) {
                textRatingSearch.text = (searchitem.movieRating/2).toString().substring(0,3)
                ratingBarSearch.rating = (searchitem.movieRating/2)
            } else {
                textRatingSearch.text = "N/A"
                ratingBarSearch.visibility = View.GONE
            }
            textMoviereleasedateSearch.text = searchitem.movieReleaseDate
            chipMediaTypeSearch.text = if (searchitem.mediaType == "tv") "tv" else searchitem.mediaType?.first().toString()
            root.setOnClickListener {
                onClick(searchitem.movieId)
            }
        }
    }
}