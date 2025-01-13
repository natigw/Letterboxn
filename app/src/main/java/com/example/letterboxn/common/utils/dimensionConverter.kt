package com.example.letterboxn.common.utils

import android.content.res.Resources

fun dpToPx(dp: Float): Float {
    return dp * Resources.getSystem().displayMetrics.density
}