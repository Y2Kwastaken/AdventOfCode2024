package sh.miles.aoc

import sh.miles.aoc.day.DayOne
import sh.miles.aoc.day.DayThree
import sh.miles.aoc.day.DayTwo
import kotlin.io.path.Path

private const val day = 3;
private val days = mapOf(
    1 to DayOne,
    2 to DayTwo,
    3 to DayThree,
)

fun main() {
    val currentDay = days[day]!!;
    val file = Path("challenges", "day$day.txt");
    val result = currentDay.run(file);
    println("Result: $result")
}
