package com.example.letterboxn.presentation.ui.bottomsheets

import android.content.SharedPreferences
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.common.utils.nancyToastError
import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseBottomSheetFragment
import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.databinding.BottomsheetfragmentFavPostersBinding
import com.example.letterboxn.presentation.adapters.BShFavMoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class FavPostersBottomSheetFragment : BaseBottomSheetFragment<BottomsheetfragmentFavPostersBinding>(BottomsheetfragmentFavPostersBinding::inflate) {

    @Inject
    lateinit var api: MovieApi

    @Inject
    @Named("UserBackPosterImage")
    lateinit var sharedPrefBackPoster: SharedPreferences

    @Inject
    @Named("UserBackPosterIsDefault")
    lateinit var sharedPrefBackPosterIsDefault: SharedPreferences

    override fun onViewCreatedLight() {
        lifecycleScope.launch {
            try {
                val movies = api.getFavoriteMovies().results
                binding.rvFavMovies.adapter = BShFavMoviesAdapter(
                    shprefBackPosterIsDefault = sharedPrefBackPosterIsDefault,
                    shprefBackPoster = sharedPrefBackPoster,
                    movies = movies,
                    onClick = {
                        findNavController().previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("changed", true)
                        findNavController().navigateUp()
                    }
                )
            } catch (e : Exception) {
                e.printStackTrace()
                nancyToastError(requireContext(), getString(R.string.unexpected_error_occurred))
            }
        }
    }
}