package com.example.letterboxn.presentation.adapters

import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.databinding.SampleExplorePosterBinding
import com.example.letterboxn.domain.model.MovieItem
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class ExploreMoviesAdapter(
    val onClick : (MovieItem)->Unit
) : BaseAdapter<SampleExplorePosterBinding>(SampleExplorePosterBinding::inflate){

    var movies : List<MovieItem> = emptyList()

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindLight(binding: SampleExplorePosterBinding, position: Int) {
        val movieitem = movies[position]

        binding.textMovienameExplore.text = movieitem.movieTitle
        Glide.with(binding.imageMovieExplore)
            .load("https://image.tmdb.org/t/p/w500" + movieitem.moviePoster)
            .placeholder(R.drawable.custom_poster_shape)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageMovieExplore)
        binding.root.setOnClickListener{
            onClick(movieitem)
        }
    }

    fun updateAdapter(newMovies : List<MovieItem>) {
        movies = newMovies
        notifyDataSetChanged()
    }

}



//class ExploreMoviesAdapter(
//    val posts: List<ResultPopularMovie>,
//    val onClick : (ResultPopularMovie)->Unit
//) : BaseAdapter<SampleExplorePosterBinding>(SampleExplorePosterBinding::inflate){
//    override fun getItemCount(): Int {
//        return posts.size
//    }
//
//    override fun onBindLight(binding: SampleExplorePosterBinding, position: Int) {
//        val postitem = posts[position]
//        binding.textMovienameExplore.text = postitem.title
//        Glide.with(binding.imageMovieExplore)
//            .load("https://image.tmdb.org/t/p/w500" + postitem.posterPath)
//            .placeholder(R.drawable.custom_poster_120x80)
//.transition(DrawableTransitionOptions.withCrossFade())
//            .into(binding.imageMovieExplore)
//        binding.root.setOnClickListener{
//            onClick(postitem)
//        }
//    }
//
//}