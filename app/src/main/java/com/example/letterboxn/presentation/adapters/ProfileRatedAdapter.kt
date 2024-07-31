package com.example.letterboxn.presentation.adapters

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.data.remote.model.account.ratedMovies.ResultRatedMovie
import com.example.letterboxn.databinding.FragmentProfileBinding
import com.example.letterboxn.databinding.SampleRecentWatchedProfileBinding
import com.example.letterboxn.domain.model.RatedMovieItem

class ProfileRatedAdapter(
    val bindinqa : FragmentProfileBinding,
    val onClick: (Int) -> Unit
) : BaseAdapter<SampleRecentWatchedProfileBinding>(SampleRecentWatchedProfileBinding::inflate) {

    var movies: List<RatedMovieItem> = emptyList()

    override fun getItemCount(): Int {
        bindinqa.textNoRecentWatchedProfile.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
        return movies.size
    }

    override fun onBindLight(binding: SampleRecentWatchedProfileBinding, position: Int) {
        val postItem = movies[position]
        with(binding) {
            Glide.with(imageMovieposterWatchedProfile.context)
                .load("https://image.tmdb.org/t/p/w500" + postItem.moviePoster)
                .placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageMovieposterWatchedProfile)
            ratingBarWatchedProfile.rating = postItem.rating
            root.setOnClickListener {
                onClick(postItem.movieId)
            }
        }
    }

    fun updateAdapter(newMovies : List<RatedMovieItem>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}