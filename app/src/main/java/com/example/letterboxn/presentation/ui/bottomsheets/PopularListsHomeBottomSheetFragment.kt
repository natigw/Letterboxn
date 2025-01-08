package com.example.letterboxn.presentation.ui.bottomsheets

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseBottomSheetFragment
import com.example.letterboxn.databinding.BottomsheetfragmentPopularListsHomeBinding
import com.google.android.material.imageview.ShapeableImageView

class PopularListsHomeBottomSheetFragment : BaseBottomSheetFragment<BottomsheetfragmentPopularListsHomeBinding>(BottomsheetfragmentPopularListsHomeBinding::inflate) {

    val args : PopularListsHomeBottomSheetFragmentArgs by navArgs()

    override fun onViewCreatedLight() {

        val item = args.listitem

        with(binding) {
            putImages(imageBShit1, item.movie1.moviePoster)
            putImages(imageBShit2, item.movie2.moviePoster)
            putImages(imageBShit3, item.movie3.moviePoster)
            putImages(imageBShit4, item.movie4.moviePoster)
            textBshitmovietitle1.text = item.movie1.movieTitle
            textBshitmovietitle2.text = item.movie2.movieTitle
            textBshitmovietitle3.text = item.movie3.movieTitle
            textBshitmovietitle4.text = item.movie4.movieTitle
            chipBShitAuthorname.text = item.listAuthorName
            imageBShitAuthorprofile.load("https://image.tmdb.org/t/p/h632" + item.listAuthorImage) {
                crossfade(true)
                placeholder(R.drawable.custom_poster_shape)
                error(R.drawable.placeholder_user)
            }
//            imageOnClick(imageBShit1, item.movie1.movieId)
//            imageOnClick(imageBShit2, item.movie2.movieId)
//            imageOnClick(imageBShit3, item.movie3.movieId)
//            imageOnClick(imageBShit4, item.movie4.movieId)
        }

    }

    private fun putImages(dest: ShapeableImageView, moviePoster : String?) {
        Glide.with(dest)
            .load("https://image.tmdb.org/t/p/w500" + moviePoster)
            .placeholder(R.drawable.custom_poster_shape)
            .error(R.drawable.default_movie_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(dest)
    }

    private fun imageOnClick(image : ShapeableImageView, movieId : Int) {
        image.setOnClickListener {
            findNavController().navigate(PopularListsHomeBottomSheetFragmentDirections.actionPopularListsHomeBottomSheetFragmentToDetailsMovieFragment(movieId))
        }
    }

}