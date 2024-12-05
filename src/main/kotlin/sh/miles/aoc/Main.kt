package sh.miles.aoc

import sh.miles.aoc.day.DayOne
import sh.miles.aoc.day.DayTwo
import kotlin.io.path.Path

private const val day = 2;
private val days = mapOf(
    1 to DayOne,
    2 to DayTwo,
)

fun main() {
    val currentDay = days[day]!!;
    val file = Path("challenges", "day$day.txt");
    val result = currentDay.run(file);
    println("Result: $result")
}
