package sh.miles.aoc.day

import sh.miles.aoc.utils.ResultUnion
import java.nio.file.Path
import kotlin.io.path.readLines

object DaySeven : Day {
    override fun run(file: Path): ResultUnion {
        val tests: List<TestSet> = file.readLines().map {
            val split = it.split(":")
            val set = split[1].split(" ").drop(1)

            TestSet(split[0].toLong(), set.map { it.toLong() })
        }

        return ResultUnion(
            tests.filter { it.test(listOf(Operator.ADD, Operator.MULTIPLY)) }.sumOf { it.expected },
            tests.filter { it.test(listOf(Operator.ADD, Operator.MULTIPLY, Operator.CONCAT)) }.sumOf { it.expected }
        )
    }

    private data class TestSet(val expected: Long, val input: List<Long>) {

        fun test(operators: List<Operator>): Boolean {
            val operatorPositions = input.size - 1
            val combinations = Operator.combinations(operatorPositions, operators)
            var last: Long = -1
            var operandIndex = 0
            for (combination in combinations) {
                for (i in input) {
                    if (last == -1L) {
                        last = i
                        continue
                    }

                    last = combination[operandIndex].compute(last, i)
                    operandIndex++
                }

                if (last == expected) {
                    return true
                }

                operandIndex = 0
                last = -1
            }

            return false
        }

    }


    private enum class Operator(val compute: (Long, Long) -> Long) {
        ADD({ first, second -> first + second }),
        MULTIPLY({ first, second -> first * second }),
        CONCAT({ first, second -> (first.toString() + second.toString()).toLong() });

        companion object {
            fun combinations(size: Int, operators: List<Operator>): MutableList<List<Operator>> {
                val list = mutableListOf<List<Operator>>()
                generateCombinations(size, operators, listOf(), list)
                return list
            }

            private fun generateCombinations(
                size: Int,
                operators: List<Operator>,
                currentCombination: List<Operator>,
                collector: MutableList<List<Operator>>
            ) {
                if (currentCombination.size == size) {
                    collector.add(currentCombination.toList())
                    return
                }

                for (operator in operators) {
                    generateCombinations(size, operators, currentCombination + operator, collector)
                }
            }
        }
    }
}
