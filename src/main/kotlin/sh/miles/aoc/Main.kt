package sh.miles.aoc

import sh.miles.aoc.year.y2023.Days2023
import sh.miles.aoc.year.y2024.Days2024
import kotlin.io.path.Path

private const val year = 2024
private val years = mapOf(
    2023 to Days2023(),
    2024 to Days2024(),
)

fun main() {
    val currentYear = years[year]!!
    val day = currentYear.days[currentYear.currentDay]!!
    val file = Path("challenges", "$year", "day${currentYear.currentDay}.txt")
    val result = day.run(file);
    println("Result: $result")
}
