package sh.miles.aoc.day

import sh.miles.aoc.utils.ResultUnion
import java.nio.file.Path
import kotlin.io.path.readLines
import kotlin.math.absoluteValue

object DayTwo : Day {
    override fun run(file: Path): ResultUnion {
        val part1 = file.readLines().map { report -> report.split(" ").map { measure -> measure.toInt() } }
            .count { it.isConforming() }
        val part2 = file.readLines().map { report -> report.split(" ").map { measure -> measure.toInt() } }.count {
            for (i in it.indices) { // lol yoink
                if (it.filterIndexed { index, _ -> index != i }.isConforming()) return@count true
            }
            return@count false
        }

        return ResultUnion(part1, part2)
    }

    private fun List<Int>.isConforming(): Boolean {
        return (this.sorted() == this || this.sortedDescending() == this) && zipWithNext().all { (it.first - it.second).absoluteValue in 1..3 }
    }
}

