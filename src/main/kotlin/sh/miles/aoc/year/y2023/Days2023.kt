package sh.miles.aoc.year.y2023

import sh.miles.aoc.year.Day
import sh.miles.aoc.year.Year

class Days2023 : Year {
    override val currentDay: Int = 2
    override val days: Map<Int, Day> = mapOf(
        2 to Day2,
        8 to Day8,
    )
}
