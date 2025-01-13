package com.example.letterboxn.common.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding>(private val bindingInflater: (LayoutInflater)->VB) : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun onCreateLight()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        _binding = bindingInflater(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                left = systemBars.left,
                top = systemBars.top,
                right = systemBars.right,
                bottom = 0
            )
            insets
        }
        onCreateLight()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}