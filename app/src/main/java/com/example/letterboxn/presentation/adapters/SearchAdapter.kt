package com.example.letterboxn.presentation.adapters

import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.databinding.FragmentSearchExploreBinding
import com.example.letterboxn.databinding.SampleSearchBinding
import com.example.letterboxn.domain.model.SearchItem

class SearchAdapter (
    val onClick: (Int) -> Unit
) : BaseAdapter<SampleSearchBinding>(SampleSearchBinding::inflate) {

    var movies: List<SearchItem> = emptyList()
    private var isMulti : Boolean = false

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindLight(binding: SampleSearchBinding, position: Int) {

        val searchItem = movies[position]

        with(binding) {
            if (isMulti) binding.chipMediaTypeSearch.visibility = View.VISIBLE
            Log.e("chip", isMulti.toString())
            Glide.with(imageSearchposter)
                .load("https://image.tmdb.org/t/p/w500" + searchItem.moviePoster)
                .placeholder(R.drawable.custom_poster_shape)
                .error(R.drawable.default_movie_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageSearchposter)
            textMovietitleSearch.text = searchItem.movieTitle
            if (searchItem.movieRating != 0f) {
                textRatingSearch.text = (searchItem.movieRating/2).toString().substring(0,3)
                ratingBarSearch.rating = (searchItem.movieRating/2)
            } else {
                textRatingSearch.text = "N/A"
                ratingBarSearch.visibility = View.GONE
            }
            textMoviereleasedateSearch.text = searchItem.movieReleaseDate
            chipMediaTypeSearch.text = if (searchItem.mediaType == "tv") "tv" else searchItem.mediaType?.first().toString()
            root.setOnClickListener {
                onClick(searchItem.movieId)
            }
        }
    }

    fun updateAdapter(newResponse: List<SearchItem>, isMulti : Boolean) {
        movies = newResponse
        this.isMulti = isMulti
        notifyDataSetChanged()
    }
}