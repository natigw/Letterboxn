package com.example.letterboxn.presentation.ui.activities

import com.example.letterboxn.R
import com.example.letterboxn.common.base.BaseActivity
import com.example.letterboxn.databinding.ActivityOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnBoardingBinding>(ActivityOnBoardingBinding::inflate) {

    override fun onCreateLight() {
        window.navigationBarColor = getColor(R.color.white)
    }
}