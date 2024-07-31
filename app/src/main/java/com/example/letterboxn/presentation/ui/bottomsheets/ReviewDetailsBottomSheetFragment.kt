package com.example.letterboxn.presentation.ui.bottomsheets

import android.text.Html
import android.view.View
import androidx.navigation.fragment.navArgs
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseBottomSheetFragment
import com.example.letterboxn.databinding.BottomsheetfragmentReviewDetailsBinding

class ReviewDetailsBottomSheetFragment : BaseBottomSheetFragment<BottomsheetfragmentReviewDetailsBinding>(BottomsheetfragmentReviewDetailsBinding::inflate) {

    private val args: ReviewDetailsBottomSheetFragmentArgs by navArgs()

    override fun onViewCreatedLight() {
        with(binding) {
            textAuthornameBSh.text = args.reviewItem.authorName
            Glide.with(imageAuthorppBSh)
                .load("https://image.tmdb.org/t/p/h632" + args.reviewItem.authorImage)
                .placeholder(R.drawable.usersample)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.usersample)
                .into(imageAuthorppBSh)
            textMovietitleBSh.text = args.reviewItem.movieTitle
            binding.imageMovieposterBSh.load("https://image.tmdb.org/t/p/w500" + args.reviewItem.moviePoster) {
                crossfade(true)
                placeholder(R.drawable.custom_poster_shape)
                error(R.drawable.custom_poster_shape)
            }
            if (args.reviewItem.movieRating != 0f) {
                textMovieratingBSh.text = args.reviewItem.movieRating.toString().substring(0, 3)
                ratingBarMovieBSh.rating = args.reviewItem.movieRating
            } else {
                textMovieratingBSh.text = "Rating: N/A"
                ratingBarMovieBSh.visibility = View.GONE
            }
            textMoviereleasedateBSh.text = args.reviewItem.movieReleaseDate
            textReviewdetailsBSh.text = args.reviewItem.review
            val reviewRating = (args.reviewItem.reviewRating).toString().substring(0, 3)
            textReviewratingBSh.text = Html.fromHtml("<b>$reviewRating</b><small>/4</small>")
            ratingBarReviewBSh.rating = args.reviewItem.reviewRating
            textCommentcountBSh.text = "${args.reviewItem.commentCount} replies"
        }
    }
}