package com.example.letterboxn.presentation.ui.fragments.explore

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.data.remote.api.MovieApi
import com.example.letterboxn.databinding.FragmentExploreBinding
import com.example.letterboxn.presentation.adapters.ExploreCategoriesAdapter
import com.example.letterboxn.presentation.adapters.MoviePagingAdapter
import com.example.letterboxn.presentation.viewmodels.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ExploreFragment : BaseFragment<FragmentExploreBinding>(bindingToInflate = FragmentExploreBinding::inflate) {

    @Inject
    lateinit var api: MovieApi

    private val viewmodel: ExploreViewModel by viewModels()

    private val adapter = MoviePagingAdapter {
        this.findNavController()
            .navigate(ExploreFragmentDirections.actionExploreFragmentToDetailsMovieFragment(it.movieId))
    }
//    private val adapter = ExploreMoviesAdapter {
//        findNavController().navigate(
//            ExploreFragmentDirections.actionExploreFragmentToDetailsMovieFragment(it.movieId)
//        )
//    }

    private val categoryAdapter = ExploreCategoriesAdapter(
        onClick = {
            findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToCategoryMoviesExploreBottomSheetFragment(it))
        }
    )

    override fun onViewCreatedLight() {

        viewmodel.moviesList.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        setAdapters()
        observe()
    }

    override fun observeChanges() {
        binding.buttonsearch.setOnClickListener {
            findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToSearchExploreFragment())
        }
    }

    private fun setAdapters() {
        binding.rvexplore.adapter = adapter
        binding.rvcategory.adapter = categoryAdapter
    }

    private fun observe() {
//        lifecycleScope.launch {
//            viewmodel.movies
//                .collect {
//                    adapter.updateAdapter(it)
//                }
//        }
        lifecycleScope.launch {
            viewmodel.categories
                .collect {
                    categoryAdapter.updateAdapter(it)
                }
        }
    }
}