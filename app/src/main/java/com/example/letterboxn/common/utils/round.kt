package com.example.letterboxn.common.utils

import kotlin.math.pow

fun roundDouble(number: Double, toStep: Int = 2): Double {
    val rounded = Math.round(number * 10.0.pow(toStep)) / 10.0.pow(toStep)
    return rounded
}