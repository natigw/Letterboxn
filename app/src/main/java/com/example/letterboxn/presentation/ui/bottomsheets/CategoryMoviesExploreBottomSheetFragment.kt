package com.example.letterboxn.presentation.ui.bottomsheets

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.letterboxn.common.base.BaseBottomSheetFragment
import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.databinding.BottomsheetfragmentCategoryMoviesExploreBinding
import com.example.letterboxn.domain.model.MoviesByCategoryItem
import com.example.letterboxn.presentation.adapters.ExploreMoviesByCategoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoryMoviesExploreBottomSheetFragment : BaseBottomSheetFragment<BottomsheetfragmentCategoryMoviesExploreBinding>(BottomsheetfragmentCategoryMoviesExploreBinding::inflate) {

    @Inject
    lateinit var api : MovieApi

    private val args : CategoryMoviesExploreBottomSheetFragmentArgs by navArgs()

    private val categoryAdapter = ExploreMoviesByCategoryAdapter(
        onClick = {
            findNavController().navigate(CategoryMoviesExploreBottomSheetFragmentDirections.actionCategoryMoviesExploreBottomSheetFragmentToDetailsMovieFragment(it.movieId))
        }
    )

    override fun onViewCreatedLight() {

        binding.textCategoryTitleBSh.text = args.categoryItem.categoryName
        viewLifecycleOwner.lifecycleScope.launch {
            val response = api.getMovies(page = 2).results
            val moviesGroupedByGenre = response
                .flatMap { movie -> movie.genreIds.map { genreId -> genreId to movie } }
                .groupBy({ it.first }, { it.second })

            val list = moviesGroupedByGenre[args.categoryItem.categoryId] ?: emptyList()

            val result = list.map {
                MoviesByCategoryItem(
                    movieId = it.id,
                    movieTitle = it.title,
                    moviePoster = it.posterPath,
                    rating = it.voteAverage.toFloat() / 2,
                    likeCount = it.voteCount,
                )
            }
            binding.textNoMoviesCateg.visibility = if (result.isEmpty()) View.VISIBLE else View.GONE
            categoryAdapter.updateAdapter(result)
            setAdapters()
        }
    }

    private fun setAdapters() {
        binding.rvMoviesByCategories.adapter = categoryAdapter
    }
}