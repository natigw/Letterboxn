package com.example.letterboxn.presentation.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.databinding.SampleHomePopularlistsBinding
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.data.remote.model.person.ResultPerson
import com.example.letterboxn.data.remote.model.popularList.ResultPopularList
import com.example.letterboxn.domain.model.ListItemEach
import com.example.letterboxn.domain.model.PopularListItem
import com.google.android.material.imageview.ShapeableImageView

class HomePopularListsAdapter(
    val onClick: (PopularListItem) -> Unit
) : BaseAdapter<SampleHomePopularlistsBinding>(SampleHomePopularlistsBinding::inflate) {

    var movies: List<ResultPopularList> = emptyList()
    var people: List<ResultPerson> = emptyList()

    override fun getItemCount(): Int {
        return movies.size - 4
    }

    override fun onBindLight(binding: SampleHomePopularlistsBinding, position: Int) {
        val item = movies[position]
        val userItem = people[position]

        with(binding) {
            putImages(imageListOne, item.posterPath)
            putImages(imageListTwo, movies[position + 1].posterPath)
            putImages(imageListThree, movies[position + 2].posterPath)
            putImages(imageListFour, movies[position + 3].posterPath)

            textPopularlistusername.text = userItem.name
            textPopularlistLikecount.text = numberFormatter(item.voteCount.toLong())
            textPopularlistCommentcount.text = numberFormatter(item.voteAverage.toLong())
            Glide.with(imagePopularlistUserpicture)
                .load("https://image.tmdb.org/t/p/h632" + userItem.profilePath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imagePopularlistUserpicture)

            root.setOnClickListener {
                onClick(PopularListItem(
                    movie1 = ListItemEach(
                        movieId = movies[position].id,
                        movieTitle = movies[position].name,
                        moviePoster = movies[position].posterPath
                    ),
                    movie2 = ListItemEach(
                        movieId = movies[position + 1].id,
                        movieTitle = movies[position + 1].name,
                        moviePoster = movies[position + 1].posterPath
                    ),
                    movie3 = ListItemEach(
                        movieId = movies[position + 2].id,
                        movieTitle = movies[position + 2].name,
                        moviePoster = movies[position + 2].posterPath
                    ),
                    movie4 = ListItemEach(
                        movieId = movies[position + 3].id,
                        movieTitle = movies[position + 3].name,
                        moviePoster = movies[position + 3].posterPath
                    ),
                    authorName = userItem.name,
                    authorImage = userItem.profilePath
                ))
            }
        }
    }

    private fun putImages(dest : ShapeableImageView, moviePoster: String) {
        Glide.with(dest)
            .load("https://image.tmdb.org/t/p/w500" + moviePoster)
            //.placeholder(R.drawable.custom_poster_shape)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(dest)
    }

    fun updateAdapter(newMovies : List<ResultPopularList>, newPeople : List<ResultPerson>) {
        movies = newMovies
        people = newPeople
        notifyDataSetChanged()
    }
}