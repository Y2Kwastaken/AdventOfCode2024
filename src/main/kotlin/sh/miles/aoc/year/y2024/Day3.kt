package sh.miles.aoc.year.y2024

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.year.Day
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.io.path.readLines

object Day3 : Day {
    val PART_ONE_REGEX = "mul\\([0-9]{1,3},[0-9]{1,3}\\)".toRegex()
    val PART_TWO_REGEX = "mul\\([0-9]{1,3},[0-9]{1,3}\\)|don't\\(\\)|do\\(\\)".toRegex()
    val enabled = AtomicBoolean(true)

    override fun run(file: Path): ResultUnion {
        val partOne = file.readLines().sumOf { parsePartOne(it) }
        val start = System.currentTimeMillis()
        val partTwo = file.readLines().sumOf { parsePartTwo(it) }
        println("Function two took ${System.currentTimeMillis() - start}ms")

        return ResultUnion(partOne, partTwo)
    }

    private fun parsePartTwo(line: String): Int {
        val matches = PART_TWO_REGEX.findAll(line)
        var count = 0

        for (match in matches) {
            val value = match.value
            if (value == "don't()") {
                enabled.set(false)
            } else if (value == "do()") {
                enabled.set(true)
            } else {
                val stripped = value.drop(4).replace(")", "").split(",")
                if (enabled.get()) {
                    count += (stripped[0].toInt() * stripped[1].toInt())
                }
            }
        }

        return count
    }

    private fun parsePartOne(line: String): Int {
        val matches = PART_ONE_REGEX.findAll(line)
        var count = 0;
        for (match in matches) {
            for (groupValue in match.groupValues) {
                val stripped = groupValue.drop(4).replace(")", "").split(",")
                count += (stripped[0].toInt() * stripped[1].toInt())
            }
        }

        return count
    }

}
