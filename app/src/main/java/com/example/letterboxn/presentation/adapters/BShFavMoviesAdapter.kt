package com.example.letterboxn.presentation.adapters

import android.content.SharedPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.data.remote.model.account.favoriteMovies.ResultFavoriteMovie
import com.example.letterboxn.databinding.SampleFavMoviesBinding

class BShFavMoviesAdapter (
    val shprefBackPoster : SharedPreferences,
    val shprefBackPosterIsDefault : SharedPreferences,
    val movies: List<ResultFavoriteMovie>,
    val onClick: (ResultFavoriteMovie) -> Unit
) : BaseAdapter<SampleFavMoviesBinding>(SampleFavMoviesBinding::inflate) {

    private val defaultImages = listOf(
        R.drawable.profile_default_back1,
        R.drawable.profile_default_back2,
        R.drawable.profile_default_back3,
        R.drawable.profile_default_back4
    )

    override fun getItemCount(): Int {
        return movies.size + defaultImages.size
    }

    override fun onBindLight(binding: SampleFavMoviesBinding, position: Int) {

        if (position < defaultImages.size) {
            Glide.with(binding.imageMoviePosterFavMoviesBSh)
                .load(defaultImages[position])
                .placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageMoviePosterFavMoviesBSh)
            binding.textMovieTitleFavMoviesBSh.text = "Default Image ${position + 1}"
            binding.root.setOnClickListener {
                shprefBackPosterIsDefault.edit().putBoolean("isDefault", true).apply()
                shprefBackPoster.edit().putString("poster", "${defaultImages[position]}").apply()
                onClick(ResultFavoriteMovie(
                    overview = "",
                    posterPath = "R.drawable.${defaultImages[position]}",
                    backdropPath = "R.drawable.${defaultImages[position]}",
                    title = "DefaultPoster ${position + 1}",
                    adult = true,
                    releaseDate = "",
                    id = 0,
                    video = false,
                    genreIds = listOf(0),
                    voteCount = 0,
                    originalTitle = "",
                    popularity = 0.0,
                    voteAverage = 0.0,
                    originalLanguage = ""
                ))
            }
        }
        else {
            val movie = movies[position - defaultImages.size]

            Glide.with(binding.imageMoviePosterFavMoviesBSh)
                .load("https://image.tmdb.org/t/p/w1280" + movie.posterPath)
                .placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageMoviePosterFavMoviesBSh)
            binding.textMovieTitleFavMoviesBSh.text = movie.title
            binding.root.setOnClickListener {
                shprefBackPosterIsDefault.edit().putBoolean("isDefault", false).apply()
                shprefBackPoster.edit().putString("poster", "https://image.tmdb.org/t/p/original" + movie.posterPath).apply()
                onClick(movie)
            }
        }
    }
}