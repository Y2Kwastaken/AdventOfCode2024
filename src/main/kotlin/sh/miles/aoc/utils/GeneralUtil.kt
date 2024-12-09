package sh.miles.aoc.utils

fun <T> timeFunction(function: () -> T): T {
    val start = System.currentTimeMillis()
    val result = function.invoke()
    val end = System.currentTimeMillis()
    println("Took ${end - start}ms")
    return result
}
