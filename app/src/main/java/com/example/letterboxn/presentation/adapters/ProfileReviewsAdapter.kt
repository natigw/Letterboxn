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
import com.example.letterboxn.databinding.FragmentProfileBinding
import com.example.letterboxn.databinding.SampleReviewBinding
import com.example.letterboxn.domain.model.ReviewWithMovieItem
import kotlin.random.Random

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
            textRecentmovietitle.text = reviewItem.movieTitle
            textRecentMoviereleasedate.text = reviewItem.movieReleaseDate.substring(0, 4)
            textRecentreviewbyhome.text = Html.fromHtml("Reviewed by <font color=\"#E9A6A6\">${reviewItem.authorName}</font>")
            textRecentcommentcount.text = numberFormatter(randomNumber.toLong())
            textReviewratehome.text = (reviewItem.reviewRating).toString().substring(0,3)

            textRecentReviewmaintext.text = reviewItem.review
//            if (textRecentmaintext.lineCount < 4) textRecentreadmore.visibility = View.INVISIBLE
//            textRecentmaintext.maxLines = 4
////            val overviewLines = postitem.overview.split("\n").size
////            if (overviewLines < 4) textRecentreadmore.visibility = View.INVISIBLE
            textRecentReviewmaintext.maxLines = 4

            Glide.with(imageRecentUserpp)
                .load(reviewItem.authorImage)
                .placeholder(R.drawable.usersample)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageRecentUserpp)
            Glide.with(imageRecentposter)
                .load("https://image.tmdb.org/t/p/w500" + reviewItem.moviePoster)
                .placeholder(R.drawable.custom_poster_shape)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageRecentposter)
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