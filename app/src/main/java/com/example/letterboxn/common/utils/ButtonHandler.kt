package com.example.letterboxn.common.utils

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.example.letterboxn.R

fun blockButton(context: Context, progressBar: ProgressBar, button: Button) {
    progressBar.visibility = View.VISIBLE
    button.apply {
        isEnabled = false
        text = null
        setBackgroundColor(context.getColor(R.color.button_disabled))
    }
}

fun resetButton(context: Context, progressBar: ProgressBar, button: Button, buttonText: String, buttonColor: Int) {
    progressBar.visibility = View.INVISIBLE
    button.apply {
        isEnabled = true
        text = buttonText
        setBackgroundColor(context.getColor(buttonColor))
    }
}