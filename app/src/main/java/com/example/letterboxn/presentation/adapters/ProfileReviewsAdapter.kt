package com.example.letterboxn.presentation.adapters

import android.text.Html
import android.view.View
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseAdapter
import com.example.letterboxn.common.utils.numberFormatter
import com.example.letterboxn.common.utils.randomInteger
import com.example.letterboxn.databinding.SampleReviewBinding
import com.example.letterboxn.domain.model.home.recentReviews.ReviewWithMovieItem

class ProfileReviewsAdapter (
    val onClick: (ReviewWithMovieItem) -> Unit
) : BaseAdapter<SampleReviewBinding>(SampleReviewBinding::inflate) {

    var reviews: MutableList<ReviewWithMovieItem> = mutableListOf()

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindLight(binding: SampleReviewBinding, position: Int) {

        val reviewItem = reviews[position]
        val randomNumber: Float = randomInteger(15, 41) / 10f

        with(binding) {
            root.startAnimation(AnimationUtils.loadAnimation(root.context, R.anim.recyclerview_animation))
            textMovieTitleRecentReview.text = reviewItem.movieTitle
            textReleaseDateRecentReview.text = reviewItem.movieReleaseDate.substring(0, 4)
            textReviewByRecentReview.text = Html.fromHtml("Reviewed by <font color=\"#E9A6A6\">${reviewItem.authorName}</font>")
            textCommentCountRecentReview.text = numberFormatter(randomNumber.toLong())
            textRateRecentReview.text = (reviewItem.reviewRating).toString().substring(0,3)

            textMainTextRecentReview.text = reviewItem.review
//            if (textMainTextRecentReview.lineCount < 4) textReadMoreRecentReview.visibility = View.INVISIBLE
//            textMainTextRecentReview.maxLines = 4
//            val overviewLines = reviewItem.review?.split("\n")?.size
//            if (overviewLines != null) if (overviewLines < 4) textReadMoreRecentReview.visibility = View.INVISIBLE
            textMainTextRecentReview.maxLines = 4

            Glide.with(imageUserPictureRecentReview)
                .load(reviewItem.authorImage)
                .placeholder(R.drawable.placeholder_user)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageUserPictureRecentReview)
            Glide.with(imagePosterRecentReview)
                .load("https://image.tmdb.org/t/p/w500" + reviewItem.moviePoster)
                .placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imagePosterRecentReview)
            root.setOnClickListener {
                onClick(reviewItem)
            }
        }
    }

    fun deleteItem(position: Int): ReviewWithMovieItem {
        val removedReview = reviews.removeAt(position)
        notifyItemRemoved(position)
        return removedReview
    }

    fun restoreItem(review: ReviewWithMovieItem, position: Int) {
        reviews.add(position, review)
        notifyItemInserted(position)
    }

    fun updateAdapter(newReviews : MutableList<ReviewWithMovieItem>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}