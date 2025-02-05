package com.example.letterboxn.presentation.ui.fragments.explore

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentSearchExploreBinding
import com.example.letterboxn.presentation.adapters.SearchAdapter
import com.example.letterboxn.presentation.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchExploreFragment :
    BaseFragment<FragmentSearchExploreBinding>(FragmentSearchExploreBinding::inflate) {

    private val viewmodel by viewModels<SearchViewModel>()
    private val searchAdapter = SearchAdapter(
        onClick = {
            findNavController().navigate(
                SearchExploreFragmentDirections.actionSearchExploreFragmentToDetailsMovieFragment(
                    it
                )
            )
        }
    )

    private var isMultiSearch = false

    override fun onViewCreatedLight() {

        showKeyboardAuto()
        hideClearButton()
        enterPress()

        setAdapters()
//        updateAdapters()
    }

    private fun setAdapters() {
        binding.rvSearchExplore.adapter = searchAdapter
    }

    private fun updateAdapters() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (isMultiSearch){
                viewmodel.multiSearchResults.collect {
                    searchAdapter.updateAdapter(it, true)
                }
            }
            else {
                viewmodel.searchResults.collect {
                    searchAdapter.updateAdapter(it, false)
                }
            }
        }
    }

    override fun clickListeners() {
        binding.imageBackToExplore.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.floatingActionButtonSearch.setOnClickListener {
            if (binding.editTextSearch.text.isNullOrEmpty())
                return@setOnClickListener
            val movieToSearch = binding.editTextSearch.text.toString().trim()
            doSearch(movieToSearch)
        }
        binding.imageSearchClear.setOnClickListener {
            binding.editTextSearch.text.clear()
        }
        binding.switchMultiSearch.setOnClickListener {
            isMultiSearch = binding.switchMultiSearch.isChecked
        }
    }

    private fun doSearch(movieToSearch: String) {
        binding.textSearchresults.visibility = View.VISIBLE
        hideKeyboardAuto()
        if (isMultiSearch) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewmodel.multiSearchResults.collectLatest {
                    binding.textNoResults.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                    searchAdapter.updateAdapter(it, true)
                }
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                viewmodel.searchResults.collectLatest {
                    binding.textNoResults.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                    searchAdapter.updateAdapter(it, true)
                }
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
//                doSearch() //TODO -> bunu duzelt
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
}