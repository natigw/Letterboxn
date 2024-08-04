package com.example.letterboxn.presentation.ui.bottomsheets

import android.content.SharedPreferences
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.common.base.BaseBottomSheetFragment
import com.example.letterboxn.common.utils.NancyToast
import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.databinding.BottomsheetfragmentFavPostersBinding
import com.example.letterboxn.presentation.adapters.BShFavMoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class FavPostersBottomSheetFragment :
    BaseBottomSheetFragment<BottomsheetfragmentFavPostersBinding>(BottomsheetfragmentFavPostersBinding::inflate) {

    @Inject
    lateinit var api: TmdbApi

    @Inject
    @Named("UserBackPosterImage")
    lateinit var shprefBackPoster: SharedPreferences

    @Inject
    @Named("UserBackPosterIsDefault")
    lateinit var shprefBackPosterIsDefault: SharedPreferences

    override fun onViewCreatedLight() {
        lifecycleScope.launch {
            try {
                val movies = api.getFavoriteMovies().results
                binding.rvFavMovies.adapter = BShFavMoviesAdapter(
                    shprefBackPosterIsDefault = shprefBackPosterIsDefault,
                    shprefBackPoster = shprefBackPoster,
                    movies = movies,
                    onClick = {
                        findNavController().previousBackStackEntry?.savedStateHandle
                            ?.set("changed", true)
                        findNavController().navigateUp()
                    }
                )
            } catch (e : Exception) {
                e.printStackTrace()
                NancyToast.makeText(requireContext(), "unexpected error occurred", NancyToast.LENGTH_SHORT, NancyToast.DEFAULT, false).show()
            }
        }
    }
}