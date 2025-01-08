package com.example.letterboxn.presentation.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.databinding.SamplePopularFavMoviesBinding
import com.example.letterboxn.domain.model.MovieItem

class HomePopularMoviesAdapter (
    val onClick : (MovieItem)->Unit
) : BaseAdapter<SamplePopularFavMoviesBinding>(SamplePopularFavMoviesBinding::inflate){

    private var movies : List<MovieItem> = emptyList()

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindLight(binding: SamplePopularFavMoviesBinding, position: Int) {
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
    }

    fun updateAdapter(newMovies : List<MovieItem>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}