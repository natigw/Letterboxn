package com.example.letterboxn.presentation.adapters

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.databinding.BottomsheetfragmentWatchlistBinding
import com.example.letterboxn.databinding.SampleWatchlistBinding
import com.example.letterboxn.domain.model.WatchlistItem

class BShWatchlistAdapter(
    val movies : List<WatchlistItem>,
    val bindinqa : BottomsheetfragmentWatchlistBinding,
    val onClick : (WatchlistItem) -> Unit
) : BaseAdapter<SampleWatchlistBinding>(SampleWatchlistBinding::inflate) {

    override fun getItemCount(): Int {
        bindinqa.textNoMoviesWatchlist.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
        return movies.size
    }

    override fun onBindLight(binding: SampleWatchlistBinding, position: Int) {
        val movieItem = movies[position]
        with(binding) {
            textMovietitleWatchlist.text = movieItem.movieTitle
            textLikecountWatchlist.text = numberFormatter(movieItem.likeCount.toLong())
            val rating = movieItem.rating / 2
            textRatingsozleWatchlist.text = rating.toString()
            ratingBarWatchlist.rating = rating
            textReleasedateWatchlist.text = movieItem.releaseDate
            Glide.with(imageMovieposterWatchlist)
                .load("https://image.tmdb.org/t/p/w500" + movieItem.moviePoster)
                .placeholder(R.drawable.custom_poster_shape)
                .error(R.drawable.default_movie_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageMovieposterWatchlist)
            root.setOnClickListener {
                onClick(movieItem)
            }
        }
    }

}