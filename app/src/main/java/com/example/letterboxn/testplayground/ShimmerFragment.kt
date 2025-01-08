package com.example.letterboxn.testplayground

import com.example.letterboxn.common.base.BaseFragment
import com.example.letterboxn.common.utils.startShimmer
import com.example.letterboxn.common.utils.stopShimmer
import com.example.letterboxn.databinding.FragmentShimmerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShimmerFragment : BaseFragment<FragmentShimmerBinding>(FragmentShimmerBinding::inflate) {

    override fun onViewCreatedLight() {
        binding.buttonStartShimmer.setOnClickListener {
            startShimmer(binding.shimmerView)
        }
        binding.buttonStopShimmer.setOnClickListener {
            stopShimmer(binding.shimmerView)
        }
    }

    override fun onResume() {
        super.onResume()
        startShimmer(binding.shimmerView)
    }

    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerView)
    }
}