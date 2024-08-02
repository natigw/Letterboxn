package com.example.letterboxn.presentation.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.databinding.SampleBsheetCategoryBinding
import com.example.letterboxn.domain.model.MoviesByCategoryItem

class ExploreMoviesByCategoryAdapter(
    val onClick : (MoviesByCategoryItem) -> Unit
) : BaseAdapter<SampleBsheetCategoryBinding>(SampleBsheetCategoryBinding::inflate) {

    var movies : List<MoviesByCategoryItem> = emptyList()

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindLight(binding: SampleBsheetCategoryBinding, position: Int) {
        val movieItem = movies[position]
        with(binding){
            textTitleCateg.text = movieItem.movieTitle
            if (movieItem.rating.toString().length > 4) textRatingSozleCateg.text = movieItem.rating.toString().substring(0,4)
            else textRatingSozleCateg.text = movieItem.rating.toString()
            ratingBarCategoryMovie.rating = movieItem.rating
            textLikecountCateg.text = numberFormatter(movieItem.likeCount.toLong())
            Glide.with(imagePosterCateg)
                .load("https://image.tmdb.org/t/p/w500" + movieItem.moviePoster)
                .placeholder(R.drawable.custom_poster_shape)
                .error(R.drawable.default_movie_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imagePosterCateg)
            root.setOnClickListener {
                onClick(movieItem)
            }
        }
    }

    fun updateAdapter(newMovies : List<MoviesByCategoryItem>) {
        movies = newMovies
        notifyDataSetChanged()
    }

}