package sh.miles.aoc.utils

import kotlin.math.abs
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

fun Double.testTolerance(tolerance: Double): Boolean {
    println("$this ${Math.round(this)} ${this - Math.round(this)}")
    return abs(this - Math.round(this)) < tolerance
}
