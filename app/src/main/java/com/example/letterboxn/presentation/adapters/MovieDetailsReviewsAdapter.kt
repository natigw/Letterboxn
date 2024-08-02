package com.example.letterboxn.presentation.adapters

import android.text.Html
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.databinding.SampleReviewMoviedetailsBinding
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.domain.model.ReviewWithoutMovieItem

class MovieDetailsReviewsAdapter(
    val onClick: (ReviewWithoutMovieItem) -> Unit
) : BaseAdapter<SampleReviewMoviedetailsBinding>(SampleReviewMoviedetailsBinding::inflate) {

    var reviews : List<ReviewWithoutMovieItem> = emptyList()

    override fun getItemCount(): Int {
        return if (reviews.size > 10) 10 else reviews.size
    }

    override fun onBindLight(binding: SampleReviewMoviedetailsBinding, position: Int) {

        val reviewItem = reviews[position]

        with(binding) {
            textReviewby.text = Html.fromHtml("Reviewed by <font color=\"#E9A6A6\">${reviewItem.authorName}</font>")
            textReviewcommentcount.text = numberFormatter(reviewItem.commentCount.toLong())
            val rating = reviewItem.reviewRating
            if (rating == 0f) {
                ReviewratingBar.visibility = View.INVISIBLE
                textNoRatingsDetails.visibility = View.VISIBLE
            }
            else ReviewratingBar.rating = rating

            textReviewmain.text = reviewItem.review
//            if (textRecentmaintext.lineCount < 5) textRecentreadmore.visibility = View.INVISIBLE
//            textRecentmaintext.maxLines = 5
////            val overviewLines = useritem.knownFor[0].overview.split("\n").size
////            if (overviewLines < 5) textRecentreadmore.visibility = View.INVISIBLE
            textReviewmain.maxLines = 5

            Glide.with(imageReviewUserpp)
                .load("https://image.tmdb.org/t/p/h632" + reviewItem.authorImage)
                .placeholder(R.drawable.usersample)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.usersample)
                .into(imageReviewUserpp)
            root.setOnClickListener {
                onClick(reviewItem)
            }
        }

    }

    fun updateAdapter(newReviews : List<ReviewWithoutMovieItem>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}