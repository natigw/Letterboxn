package com.example.letterboxn.presentation.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.databinding.SamplePopularFavMovieBinding
import com.example.letterboxn.domain.model.MovieItem

class ProfileFavMoviesAdapter (
    val onClick : (MovieItem)->Unit,
    val onLongClick: (Int, Int) -> Unit
) : BaseAdapter<SamplePopularFavMovieBinding>(SamplePopularFavMovieBinding::inflate){

    var movies : MutableList<MovieItem> = mutableListOf()

    override fun getItemCount(): Int {
        return if (movies.size > 10) 10 else movies.size
    }

    override fun onBindLight(binding: SamplePopularFavMovieBinding, position: Int) {
        val movie = movies[position]

        Glide.with(binding.imageMoviePopularFav)
            .load("https://image.tmdb.org/t/p/w500" + movie.moviePoster)
            .placeholder(R.drawable.custom_poster_shape)
            .error(R.drawable.default_movie_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageMoviePopularFav)
        binding.root.setOnClickListener{
            onClick(movie)
        }
        binding.root.setOnLongClickListener {
            onLongClick(position, movie.movieId)
            true
        }
    }

    fun removeItem(position: Int) {
        movies.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun updateAdapter(newMovies : MutableList<MovieItem>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}