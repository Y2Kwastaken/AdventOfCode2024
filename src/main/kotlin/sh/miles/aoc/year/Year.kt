package sh.miles.aoc.year

interface Year {
    val currentDay: Int
    val days: Map<Int, Day>
}
