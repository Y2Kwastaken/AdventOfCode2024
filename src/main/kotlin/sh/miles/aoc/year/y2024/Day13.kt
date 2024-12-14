package sh.miles.aoc.year.y2024

import sh.miles.aoc.utils.math.ComplexMath
import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.testTolerance
import sh.miles.aoc.utils.timeFunctionNano
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day13 : Day {
    private const val ADDITION = 10000000000000
    private const val DOUBLE_TOLERANCE = 0.001

    override fun run(file: Path): ResultUnion {
        val machines = file.readLines().filter { it.trim().isNotBlank() }.chunked(3)
            .map { line -> ClawMachine.parse(line) { it } }

        return ResultUnion(
            timeFunctionNano { machines.sumOf { goal(it) } },
            timeFunctionNano { machines.map { it.withPrize(it.prize.withAddition(ADDITION)) }.sumOf { goal(it) } }
        )
    }

    private fun goal(machine: ClawMachine): Long {
        val matrix = machine.arrayGen()

        ComplexMath.applyGaussJordan(matrix)
        val first = matrix[0][matrix[0].size - 1]
        val second = matrix[1][matrix[1].size - 1]

        if (first < 0 || second < 0) {
            return 0
        }

        if (!first.testTolerance(DOUBLE_TOLERANCE) || !second.testTolerance(DOUBLE_TOLERANCE)) {
            return 0
        }

        return (Math.round(first) * 3) + (Math.round(second) * 1)
    }

    private data class Button(val x: Int, val y: Int) {
        constructor(input: List<Int>) : this(input[0], input[1])
    }

    private data class Prize(val x: Long, val y: Long) {
        constructor(input: List<Long>) : this(input[0], input[1])

        fun withAddition(num: Long): Prize {
            return Prize(x + num, y + num)
        }
    }

    private data class ClawMachine(val buttons: Pair<Button, Button>, val prize: Prize) {

        fun arrayGen(): Array<DoubleArray> {
            return arrayOf(
                doubleArrayOf(buttons.first.x.toDouble(), buttons.second.x.toDouble(), prize.x.toDouble()),
                doubleArrayOf(buttons.first.y.toDouble(), buttons.second.y.toDouble(), prize.y.toDouble())
            )
        }

        fun withPrize(prize: Prize): ClawMachine {
            return ClawMachine(this.buttons, prize)
        }

        companion object {
            fun parse(lines: List<String>, modify: (Prize) -> Prize): ClawMachine {
                val a = Button(lines[0].trim().split(":")[1].split(",").map { it.split("+")[1].toInt() })
                val b = Button(lines[1].trim().split(":")[1].split(",").map { it.split("+")[1].toInt() })
                val prize = Prize(lines[2].trim().split(":")[1].split(",").map { it.split("=")[1].toLong() })
                return ClawMachine(Pair(a, b), modify(prize))
            }
        }
    }

}
