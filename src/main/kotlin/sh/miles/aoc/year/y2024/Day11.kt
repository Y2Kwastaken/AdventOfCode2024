package sh.miles.aoc.year.y2024

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.digits
import sh.miles.aoc.utils.halve
import sh.miles.aoc.utils.timeFunction
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day11 : Day {
    private val PART_ONE_RULES = listOf<(String) -> List<String>>(
        { listOf("1") },
        { digits -> digits.chunked(digits.length / 2).map { it.toLong().toString() } },
        { listOf((it.toLong() * 2024).toString()) }
    )

    private data class Stone(val currentSplit: Int, val stone: Long)

    override fun run(file: Path): ResultUnion {
        val line = file.readLines()[0]
        val partOne = timeFunction { line.split(" ").map { it.toLong() }.sumOf { stone -> splitCacheCount(25, 0, stone, mutableMapOf()) } }
        val partTwo = timeFunction { line.split(" ").map { it.toLong() }.sumOf { stone -> splitCacheCount(75, 0, stone, mutableMapOf()) } }

        return ResultUnion(partOne, partTwo)
    }

    private fun splitCacheCount(splitLimit: Int, currentSplit: Int, stone: Long, tracker: MutableMap<Stone, Long>): Long {
        if (splitLimit == currentSplit) {
            return 1
        }

        val stoneObj = Stone(currentSplit, stone)
        if (tracker.containsKey(stoneObj)) {
            return tracker[stoneObj]!!
        }

        tracker[stoneObj] = applyRule(stone).sumOf { splitCacheCount(splitLimit, currentSplit + 1, it, tracker) }
        return tracker[stoneObj]!!
    }

    private fun applyRule(number: Long): List<Long> {
        var digits: Int
        if (number == 0L) {
            return listOf(1L)
        } else if (number.digits().also { digits = it } % 2 == 0) {
            return number.halve(digits)
        } else {
            return listOf(number * 2024)
        }
    }

}
