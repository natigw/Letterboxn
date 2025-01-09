package com.example.letterboxn.presentation.ui.fragments.explore

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentExploreBinding
import com.example.letterboxn.presentation.adapters.ExploreCategoriesAdapter
import com.example.letterboxn.presentation.adapters.MoviePagingAdapter
import com.example.letterboxn.presentation.viewmodels.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExploreFragment : BaseFragment<FragmentExploreBinding>(FragmentExploreBinding::inflate) {

    private val viewmodel by viewModels<ExploreViewModel>()

    private val movieAdapter = MoviePagingAdapter {
        this.findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToDetailsMovieFragment(it.movieId))
    }

    private val categoryAdapter = ExploreCategoriesAdapter(
        onClick = {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToCategoryMoviesExploreBottomSheetFragment(it)
            )
        }
    )

    override fun onViewCreatedLight() {
        setAdapters()
        updateAdapters()
    }

    override fun observeChanges() {
        binding.buttonsearch.setOnClickListener {
            findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToSearchExploreFragment())
        }

        viewmodel.moviesList.observe(viewLifecycleOwner) {
            movieAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun setAdapters() {
        binding.rvexplore.adapter = movieAdapter
        binding.rvcategory.adapter = categoryAdapter
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.categories.collect {
                categoryAdapter.updateAdapter(it)
            }
        }
    }
}