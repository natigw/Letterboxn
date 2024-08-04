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

class MovieDetailsReviewsAdapter : BaseAdapter<SampleReviewMoviedetailsBinding>(SampleReviewMoviedetailsBinding::inflate) {

    var reviews : List<ReviewWithoutMovieItem> = emptyList()
    private var showAll = false

    override fun getItemCount(): Int {
        return if (showAll) reviews.size else minOf(reviews.size, 5)
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
            } else {
                ReviewratingBar.rating = rating
            }

            textReviewmain.text = reviewItem.review
            val lines = reviewItem.review.length / 84
            textReviewmain.maxLines = 5

            Glide.with(imageReviewUserpp)
                .load("https://image.tmdb.org/t/p/h632" + reviewItem.authorImage)
                .placeholder(R.drawable.usersample)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.usersample)
                .into(imageReviewUserpp)

            textReviewreadmore.visibility = if (lines < 5) View.INVISIBLE else View.VISIBLE

            root.setOnClickListener {
                if (lines > 5 && textReviewmain.maxLines < lines) {
                    textReviewmain.maxLines += 5
                }
                if (textReviewreadmore.text == "Read less") {
                    textReviewmain.maxLines = 5
                    textReviewreadmore.text = "Read more..."
                }
                if (textReviewmain.maxLines >= lines) {
                    textReviewreadmore.text = "Read less"
                }
            }
        }
    }

    fun updateAdapter(newReviews: List<ReviewWithoutMovieItem>, showAll: Boolean = false) {
        reviews = newReviews
        this.showAll = showAll
        notifyDataSetChanged()
    }
}