package com.example.letterboxn.presentation.ui.bottomsheets

import android.content.SharedPreferences
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.common.base.BaseBottomSheetFragment
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
            val movies = api.getFavoriteMovies().results
            binding.rvFavMovies.adapter = BShFavMoviesAdapter(
                shprefBackPosterIsDefault = shprefBackPosterIsDefault,
                shprefBackPoster = shprefBackPoster,
                movies = movies,
                onClick = {
                    findNavController().popBackStack()
//                    findNavController().previousBackStackEntry?.savedStateHandle
//                        ?.set("changed", true)
//                    findNavController().navigateUp()
                }
            )
        }
    }

}




//    private val viewmodel: BShFavMoviesViewModel by viewModels()
//
//    private lateinit var favAdapter: BShFavMoviesAdapter
//
//    override fun onViewCreatedLight() {
//        shprefBackPoster = requireContext().getSharedPreferences("userBackPosterImage", Context.MODE_PRIVATE)
//        shprefBackPosterIsDefault = requireContext().getSharedPreferences("userBackPosterIsDefault", Context.MODE_PRIVATE)
//        setupAdapter()
//        setRv()
//        observeChanges()
//    }
//
//    private fun setupAdapter() {
//        favAdapter = BShFavMoviesAdapter(
//            shprefBackPosterIsDefault = shprefBackPosterIsDefault,
//            shprefBackPoster = shprefBackPoster,
//            onClick = {
//                findNavController().popBackStack()
//            }
//        )
//    }
//
//    private fun setRv() {
//        binding.rvFavMovies.adapter = favAdapter
//    }
//
//    private fun observeChanges() {
//        lifecycleScope.launch {
//            viewmodel.posters.collect {
//                favAdapter.updateAdapter(it)
//            }
//        }
//    }
//}


//@AndroidEntryPoint
//class FavMoviesBottomSheetFragment : BaseBottomSheetFragment<BottomsheetfragmentFavMoviesBinding>(BottomsheetfragmentFavMoviesBinding::inflate) {
//
//    @Inject
//    @Named("UserBackPosterImage")
//    lateinit var shprefBackPoster : SharedPreferences
//
//    @Inject
//    @Named("UserBackPosterIsDefault")
//    lateinit var shprefBackPosterIsDefault : SharedPreferences
//
//    val viewmodel : BShFavMoviesViewModel by viewModels()
//
//    val favAdapter = BShFavMoviesAdapter (
//        shprefBackPosterIsDefault = shprefBackPosterIsDefault,
//        shprefBackPoster = shprefBackPoster,
//        onClick = {
//            findNavController().popBackStack()
//        })
//
//    override fun onViewCreatedLight() {
//        setRv()
//        observe()
//    }
//
//    fun observe() {
//        lifecycleScope.launch {
//            viewmodel.posters
//                .collect {
//                    favAdapter.updateAdapter(it)
//                }
//        }
//    }
//
//    fun setRv() {
//        binding.rvFavMovies.adapter = favAdapter
//    }
//
//}