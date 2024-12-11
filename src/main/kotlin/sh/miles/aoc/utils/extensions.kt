package sh.miles.aoc.utils

import kotlin.math.log10
import kotlin.math.pow

fun Long.digits(): Int {
    return (log10(this.toDouble()) + 1).toInt()
}

fun Long.halve(digits: Int): List<Long> {
    val half = 10.0.pow((digits / 2).toDouble()).toInt()
    return listOf(this / half, this % half)
}

fun Long.halve(): List<Long> {
    return halve(digits())
}
