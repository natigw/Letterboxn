package com.example.letterboxn.presentation.ui.bottomsheets

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.common.base.BaseBottomSheetFragment
import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.databinding.BottomsheetfragmentWatchlistBinding
import com.example.letterboxn.domain.model.WatchlistItem
import com.example.letterboxn.presentation.adapters.BShWatchlistAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WatchlistBottomSheetFragment : BaseBottomSheetFragment<BottomsheetfragmentWatchlistBinding>(BottomsheetfragmentWatchlistBinding::inflate) {

    @Inject
    lateinit var api : TmdbApi

//    val watchlistAdapter = binding.rvWatchlist.adapter

    override fun onViewCreatedLight() {
        lifecycleScope.launch {
            val response = api.getWatchlist()
            binding.rvWatchlist.adapter = BShWatchlistAdapter(
                bindinqa = binding,
                movies = response.results.map {
                    WatchlistItem(
                        movieId = it.id,
                        movieTitle = it.title,
                        moviePoster = it.posterPath,
                        likeCount = it.voteCount,
                        rating = it.voteAverage.toFloat(),
                        releaseDate = it.releaseDate,
                    )
                },
                onClick = {
                    findNavController().navigate(WatchlistBottomSheetFragmentDirections.actionWatchlistBottomSheetFragmentToDetailsMovieFragment(it.movieId))
                }
            )
        }
    }
}