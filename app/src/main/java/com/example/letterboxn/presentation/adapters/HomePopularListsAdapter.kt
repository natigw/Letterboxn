package com.example.letterboxn.presentation.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.databinding.SampleHomePopularlistsBinding
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.data.remote.model.person.ResultPerson
import com.example.letterboxn.data.remote.model.popularList.ResultPopularList
import com.example.letterboxn.domain.model.ListItem
import com.example.letterboxn.domain.model.ListItemEach
import com.example.letterboxn.domain.model.ListPersonItem
import com.example.letterboxn.domain.model.PopularListItem
import com.google.android.material.imageview.ShapeableImageView

class HomePopularListsAdapter(
    val onClick: (PopularListItem) -> Unit
) : BaseAdapter<SampleHomePopularlistsBinding>(SampleHomePopularlistsBinding::inflate) {

//    var movies: List<ListItem> = emptyList()
//    var people: List<ListPersonItem> = emptyList()
    var movies: List<ResultPopularList> = emptyList()
    var people: List<ResultPerson> = emptyList()

    override fun getItemCount(): Int {
        return movies.size/4
    }

    override fun onBindLight(binding: SampleHomePopularlistsBinding, position: Int) {
        val item = movies[position]
        val userItem = people[position]

        with(binding) {
            putImages(imageListOne, movies[position].posterPath)
            putImages(imageListTwo, movies[position + 9].posterPath)
            putImages(imageListThree, movies[position + 17].posterPath)
            putImages(imageListFour, movies[position + 25].posterPath)

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
                        movieTitle = movies[position].name,
                        moviePoster = movies[position].posterPath
                    ),
                    movie2 = ListItemEach(
                        movieTitle = movies[position + 9].name,
                        moviePoster = movies[position + 9].posterPath
                    ),
                    movie3 = ListItemEach(
                        movieTitle = movies[position + 17].name,
                        moviePoster = movies[position + 17].posterPath
                    ),
                    movie4 = ListItemEach(
                        movieTitle = movies[position + 25].name,
                        moviePoster = movies[position + 25].posterPath
                    ),
                    authorName = userItem.name,
                    authorImage = userItem.profilePath
                ))
            }
        }
//        with(binding) {
//            putImages(imageListOne, item.movieItems[0].moviePoster!!)
//            putImages(imageListTwo, item.movieItems[1].moviePoster!!)
//            putImages(imageListThree, item.movieItems[2].moviePoster!!)
//            putImages(imageListFour, item.movieItems[3].moviePoster!!)
//
//            textPopularlistusername.text = userItem.userName
//            textPopularlistLikecount.text = numberFormatter(item.likeCount.toLong())
//            textPopularlistCommentcount.text = numberFormatter(item.commentCount.toLong())
//            Glide.with(imagePopularlistUserpicture)
//                .load("https://image.tmdb.org/t/p/h632" + userItem.userImage)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(imagePopularlistUserpicture)
//
//            root.setOnClickListener {
//                onClick(PopularListItem(
//                    movie1 = ListItemEach(
//                        movieTitle = item.movieItems[0].movieTitle,
//                        moviePoster = item.movieItems[0].moviePoster
//                    ),
//                    movie2 = ListItemEach(
//                        movieTitle = item.movieItems[1].movieTitle,
//                        moviePoster = item.movieItems[1].moviePoster
//                    ),
//                    movie3 = ListItemEach(
//                        movieTitle = item.movieItems[2].movieTitle,
//                        moviePoster = item.movieItems[2].moviePoster
//                    ),
//                    movie4 = ListItemEach(
//                        movieTitle = item.movieItems[3].movieTitle,
//                        moviePoster = item.movieItems[3].moviePoster
//                    ),
//                    authorName = userItem.userName,
//                    authorImage = userItem.userImage
//                ))
//            }
//        }
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
//    fun updateAdapter(newMovies : List<ListItem>, newPeople : List<ListPersonItem>) {
//        movies = newMovies
//        people = newPeople
//        notifyDataSetChanged()
//    }
}