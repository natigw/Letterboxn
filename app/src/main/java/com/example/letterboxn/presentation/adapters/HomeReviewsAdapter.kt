package com.example.letterboxn.presentation.adapters

import android.text.Html
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.common.utils.randomInteger
import com.example.letterboxn.data.remote.model.person.ResultPerson
import com.example.letterboxn.data.remote.model.popularList.ResultPopularList
import com.example.letterboxn.databinding.SampleReviewBinding
import com.example.letterboxn.domain.model.ReviewWithMovieItem

class HomeReviewsAdapter (
    val onClick: (ReviewWithMovieItem) -> Unit
) : BaseAdapter<SampleReviewBinding>(SampleReviewBinding::inflate) {

    var results: List<ResultPopularList> = emptyList()
    var people: List<ResultPerson> = emptyList()

    override fun getItemCount(): Int {
        return results.size - 10
    }

    override fun onBindLight(binding: SampleReviewBinding, position: Int) {

        val useritem = people[position + 5]
        val postitem = results[position + 5]
        val randomNumber: Float = randomInteger(15, 41) / 10f  // *0.8

        with(binding) {
            textRecentmovietitle.text = postitem.name
            textRecentMoviereleasedate.text = postitem.firstAirDate.substring(0, 4)
            textRecentreviewbyhome.text = Html.fromHtml("Reviewed by <font color=\"#E9A6A6\">${useritem.name}</font>")
            textRecentcommentcount.text = numberFormatter(postitem.voteCount.toLong())
            textReviewratehome.text = randomNumber.toString()

            val saxtaReview = useritem.knownFor[0].overview
            textRecentReviewmaintext.text = saxtaReview.ifEmpty { "Movie was just perfect!" }
//            if (textRecentmaintext.lineCount < 4) textRecentreadmore.visibility = View.INVISIBLE
//            textRecentmaintext.maxLines = 4
////            val overviewLines = useritem.knownFor[0].overview.split("\n").size
////            if (overviewLines < 4) textRecentreadmore.visibility = View.INVISIBLE
            textRecentReviewmaintext.maxLines = 4

            Glide.with(imageRecentUserpp)
                .load("https://image.tmdb.org/t/p/h632" + useritem.profilePath)
                //.placeholder(R.drawable.usersample)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageRecentUserpp)
            Glide.with(imageRecentposter)
                .load("https://image.tmdb.org/t/p/w500" + postitem.posterPath)
                //.placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageRecentposter)
            root.setOnClickListener {
                onClick(
                    ReviewWithMovieItem(
                        authorName = useritem.name,
                        authorImage = useritem.profilePath,
                        movieId = postitem.id,
                        movieTitle = postitem.name,
                        moviePoster = postitem.posterPath,
                        movieReleaseDate = postitem.firstAirDate,
                        movieRating = postitem.voteAverage.toFloat() / 2,
                        review = useritem.knownFor[0].overview,
                        reviewRating = randomNumber,
                        commentCount = postitem.voteCount
                    )
                )
            }
        }

    }

//    fun updateAdapter(newReviews : List<ReviewWithMovieItem>){
//        reviews = newReviews
//        notifyDataSetChanged()
//    }

    fun updateAdapter(newResults : List<ResultPopularList>, newPeople : List<ResultPerson>) {
        results = newResults
        people = newPeople
        notifyDataSetChanged()
    }

}