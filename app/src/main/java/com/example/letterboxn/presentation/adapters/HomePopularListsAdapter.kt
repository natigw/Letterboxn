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

//class HomePopularlistsAdapter(
//    val onClick: (ListItem) -> Unit
//) : BaseAdapter<SampleHomePopularlistsBinding>(SampleHomePopularlistsBinding::inflate) {
//
//    var data: List<ListItem> = emptyList()
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    override fun onBindLight(binding: SampleHomePopularlistsBinding, position: Int) {
//        val listitem = data[position]
//
//        with(binding) {
//            Glide.with(imageListOne)
//                .load("https://image.tmdb.org/t/p/w500" + listitem.movieItems[0])
//                .placeholder(R.drawable.custom_poster_shape)
//.transition(DrawableTransitionOptions.withCrossFade())
//                .into(imageListOne)
//            Glide.with(imageListTwo)
//                .load("https://image.tmdb.org/t/p/w500" + listitem.movieItems[0])
//                .placeholder(R.drawable.custom_poster_shape)
//.transition(DrawableTransitionOptions.withCrossFade())
//                .into(imageListTwo)
//            Glide.with(imageListThree)
//                .load("https://image.tmdb.org/t/p/w500" + listitem.movieItems[0])
//                .placeholder(R.drawable.custom_poster_shape)
//.transition(DrawableTransitionOptions.withCrossFade())
//                .into(imageListThree)
//            Glide.with(imageListFour)
//                .load("https://image.tmdb.org/t/p/w500" + listitem.movieItems[0])
//                .placeholder(R.drawable.custom_poster_shape)
//.transition(DrawableTransitionOptions.withCrossFade())
//                .into(imageListFour)
//
//            textPopularlistName.text = listitem.listTitle
//            textPopularlistusername.text = listitem.authorName
//            textPopularlistLikecount.text = numberFormatter(listitem.likeCount.toLong())
//            textPopularlistCommentcount.text = numberFormatter(listitem.commentCount.toLong())
//            Glide.with(imagePopularlistUserpicture)
//                .load("https://image.tmdb.org/t/p/h632" + listitem.authorImage)
//                .placeholder(R.drawable.usersample)
//.transition(DrawableTransitionOptions.withCrossFade())
//                .into(imagePopularlistUserpicture)
//            root.setOnClickListener {
//                onClick(listitem)
//            }
//        }
//    }
//
//    fun updateAdapter(newList : List<ListItem>) {
//        data = newList
//        notifyDataSetChanged()
//    }
//
//}



class HomePopularListsAdapter(
    val movies: List<ResultPopularList>,
    val people: List<ResultPerson>,
    val onClick: (PopularListItem) -> Unit
) : BaseAdapter<SampleHomePopularlistsBinding>(SampleHomePopularlistsBinding::inflate) {


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
}