package com.example.letterboxn.common.utils

import kotlin.math.ln
import kotlin.math.pow
import kotlin.random.Random

fun numberFormatter(count: Long): String {
    if (count < 1000) return "" + count
    val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f%c", count / 1000.0.pow(exp.toDouble()), "kMBTP"[exp - 1])
}

fun numberFormatterSpaced(count: Long): String {
    if (count < 1000) return "" + count
    val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
    val value = count / 1000.0.pow(exp.toDouble())
    return String.format("%.1f%c", value, "KMBTP"[exp - 1])
}

fun randomInteger(min: Int, max: Int): Int {
    return Random.nextInt(min, max)
}