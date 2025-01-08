package com.example.letterboxn.presentation.adapters

import android.text.Html
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.common.utils.roundDouble
import com.example.letterboxn.databinding.SampleReviewBinding
import com.example.letterboxn.domain.model.home.recentReviews.ReviewWithMovieItem

class HomeReviewsAdapter(
    val onClick: (ReviewWithMovieItem) -> Unit
) : BaseAdapter<SampleReviewBinding>(SampleReviewBinding::inflate) {

    private var reviews: List<ReviewWithMovieItem> = emptyList()

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindLight(binding: SampleReviewBinding, position: Int) {

        val review = reviews[position]

        with(binding) {
            textMovieTitleRecentReview.text = review.movieTitle
            textReviewByRecentReview.text = Html.fromHtml("Reviewed by <font color=\"#E9A6A6\">${review.authorName}</font>")
            textMainTextRecentReview.text = review.review
            textRateRecentReview.text = "${review.reviewRating}"
            textCommentCountRecentReview.text = numberFormatter(review.commentCount.toLong())
            textReleaseDateRecentReview.text = review.movieReleaseDate

//            if (textRecentmaintext.lineCount < 4) textRecentreadmore.visibility = View.INVISIBLE
//            textRecentmaintext.maxLines = 4
////            val overviewLines = useritem.knownFor[0].overview.split("\n").size
////            if (overviewLines < 4) textRecentreadmore.visibility = View.INVISIBLE
            textMainTextRecentReview.maxLines = 4

            Glide.with(imageUserPictureRecentReview)
                .load("https://image.tmdb.org/t/p/h632${review.authorImage}")
                //.placeholder(R.drawable.usersample)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageUserPictureRecentReview)
            Glide.with(imagePosterRecentReview)
                .load("https://image.tmdb.org/t/p/w500${review.moviePoster}")
                //.placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imagePosterRecentReview)
            root.setOnClickListener {
                onClick(review)
            }
        }

    }

    fun updateAdapter(newReviews : List<ReviewWithMovieItem>){
        reviews = newReviews
        notifyDataSetChanged()
    }
}