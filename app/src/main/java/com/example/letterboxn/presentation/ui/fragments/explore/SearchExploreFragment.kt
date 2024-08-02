package com.example.letterboxn.presentation.ui.fragments.explore

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.data.remote.api.TmdbApi
import com.example.letterboxn.databinding.FragmentSearchExploreBinding
import com.example.letterboxn.domain.model.SearchItem
import com.example.letterboxn.presentation.adapters.SearchAdapter
import com.example.letterboxn.presentation.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchExploreFragment :
    BaseFragment<FragmentSearchExploreBinding>(FragmentSearchExploreBinding::inflate) {

    @Inject
    lateinit var api: TmdbApi

    val viewmodel : SearchViewModel by viewModels()
    private val searchAdapter = SearchAdapter(
        onClick = {
            findNavController().navigate(SearchExploreFragmentDirections.actionSearchExploreFragmentToDetailsMovieFragment(it))
        }
    )

    private var isMultiSearch = false

    override fun onViewCreatedLight() {

        showKeyboardAuto()
        hideClearButton()
        enterPress()

        setAdapters()
//        updateAdapters()

        binding.floatingActionButtonSearch.setOnClickListener {
            if (binding.editTextSearch.text.isNullOrEmpty()) return@setOnClickListener
            doSearch()
        }
    }

    override fun observeChanges() {
        binding.imageSearchClear.setOnClickListener {
            binding.editTextSearch.text.clear()
        }
        binding.imageBackToExplore.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.switchMultiSearch.setOnClickListener {
            isMultiSearch = binding.switchMultiSearch.isChecked
        }
    }

    private fun doSearch() {
        binding.textSearchresults.visibility = View.VISIBLE
        hideKeyboardAuto()
        val movieToSearch = binding.editTextSearch.text.toString()
        if (isMultiSearch) {
            lifecycleScope.launch {
                try {
                    val responseMultiSearch = api.multiSearchMovies(movieToSearch).results.map {
                        SearchItem(
                            movieId = it.id,
                            movieTitle = it.title,
                            moviePoster = it.posterPath,
                            movieRating = it.voteAverage.toFloat(),
                            movieReleaseDate = it.releaseDate,
                            mediaType = it.mediaType
                        )
                    }
                    binding.textNoResults.visibility = if (responseMultiSearch.isEmpty()) View.VISIBLE else View.GONE
                    searchAdapter.updateAdapter(responseMultiSearch,true)
                }
                catch (e: Exception) {
                    Log.e("api", e.toString())
                }
            }
        }
        else {
            try {
                lifecycleScope.launch {
                    val responseSearch = api.searchMovies(movieToSearch).results.map {
                        SearchItem(
                            movieId = it.id,
                            movieTitle = it.title,
                            moviePoster = it.posterPath,
                            movieRating = it.voteAverage.toFloat(),
                            movieReleaseDate = it.releaseDate,
                            mediaType = "movie"
                        )
                    }
                    binding.textNoResults.visibility = if (responseSearch.isEmpty()) View.VISIBLE else View.GONE
                    searchAdapter.updateAdapter(responseSearch,false)
                }
            } catch (e: Exception) {
                Log.e("api", e.toString())
            }
        }
    }

    private fun hideClearButton() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.imageSearchClear.visibility =
                    if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun enterPress() {
        // clear the text on Enter key press
        binding.editTextSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                binding.textSearchresults.visibility = View.VISIBLE
                hideKeyboardAuto()
                doSearch()
                true
            } else {
                false
            }
        }
    }

    private fun showKeyboardAuto() {
        binding.editTextSearch.requestFocus()

        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.editTextSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboardAuto() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editTextSearch.windowToken, 0)
    }


//    private fun updateAdapters() {
//        lifecycleScope.launch {
//            viewmodel.results
//                .collect {
//                    searchAdapter.updateAdapter(it)
//                }
//        }
//    }
//
    private fun setAdapters() {
        binding.rvSearchExplore.adapter = searchAdapter
    }
}