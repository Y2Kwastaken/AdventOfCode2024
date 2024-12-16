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
    return abs(this - Math.round(this)) < tolerance
}

fun <E> List<E>.split(line: E): List<List<E>> {
    val collector = mutableListOf<List<E>>()
    var current = mutableListOf<E>()

    for (entry in this) {
        if (entry == line) {
            collector.add(current)
            current = mutableListOf<E>()
            continue
        }

        current.add(entry)
    }

    if (current.isNotEmpty()) {
        collector.add(current)
    }

    return collector
}

fun <E> List<E>.operate(operation: (E) -> Unit): List<E> {
    for (entry in this) {
        operation.invoke(entry)
    }

    return this
}
