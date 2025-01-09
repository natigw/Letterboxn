package com.example.letterboxn.presentation.ui.bottomsheets

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.letterboxn.common.utils.nancyToastWarning
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseBottomSheetFragment
import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.data.remote.model.account.ratedMovies.rateMovie.RequestAddRating
import com.example.letterboxn.databinding.BottomsheetfragmentRateMovieBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RateMovieBottomSheetFragment : BaseBottomSheetFragment<BottomsheetfragmentRateMovieBinding>(BottomsheetfragmentRateMovieBinding::inflate) {

    @Inject
    lateinit var api : MovieApi

    private val args : RateMovieBottomSheetFragmentArgs by navArgs()

    private var requestStatus = false

    override fun onViewCreatedLight() {

        binding.textMovieTitleBshRateMovie.text = args.movieTitle

        binding.buttonRateMovieDone.setOnClickListener {
            val rating = binding.ratingBarBshRateMovie.rating
            if (rating == 0f) {
                nancyToastWarning(requireContext(), getString(R.string.please_rate_movie))
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val request = api.addRateMovie(
                    movieId = args.movieId,
                    requestRateMovie = RequestAddRating(value = rating)
                )
                requestStatus = request.success
            }
            val handled = findNavController().previousBackStackEntry?.savedStateHandle
            handled?.set("result", rating)
            handled?.set("requestStatus", requestStatus)
            findNavController().navigateUp()
        }
    }

}