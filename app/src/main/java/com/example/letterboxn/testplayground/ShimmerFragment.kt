package com.example.letterboxn.testplayground

import android.view.View
import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.databinding.FragmentShimmerBinding
import com.facebook.shimmer.ShimmerFrameLayout

class ShimmerFragment : BaseFragment<FragmentShimmerBinding>(FragmentShimmerBinding::inflate){

    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onViewCreatedLight() {
        shimmerFrameLayout = binding.shimmerViewContainer
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE
    }

    override fun observeChanges() {

    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
    }
}