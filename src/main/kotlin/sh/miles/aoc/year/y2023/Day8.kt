package sh.miles.aoc.year.y2023

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day8 : Day {
    private fun left(map: Map<String, Transportation>, transport: Transportation): Transportation {
        return map[transport.left]!!
    }

    private fun right(map: Map<String, Transportation>, transport: Transportation): Transportation {
        return map[transport.right]!!
    }

    override fun run(file: Path): ResultUnion {
        val lines = file.readLines()
        val instructions = parseInstructions(lines)
        val mapping = parseTransportations(lines)

//        var starting = mapping["AAA"]!!
//        var partOneCount = 0
//        while (starting != mapping["ZZZ"]) {
//            for (instruction in instructions) {
//                starting = instruction(mapping, starting)
//                partOneCount++
//            }
//        }

        var allStarters = mapping.values.filter { it.key.endsWith("A") }
        var partTwoCount = 0
        for (allStarter in allStarters) {
            partTwoCount += partTwo(allStarter, instructions, mapping)
        }

        return ResultUnion(0, partTwoCount)
    }

    private fun partTwo(
        startingTransport: Transportation,
        instructions: List<(Map<String, Transportation>, Transportation) -> Transportation>,
        mapping: Map<String, Transportation>
    ): Int {
        var current = startingTransport
        var count = 0
        while (!current.key.endsWith("Z")) {
            for (instruction in instructions) {
                current = instruction(mapping, current)
                count++
                if (current.key.endsWith("Z")) {
                    break
                }
            }
        }

        return count
    }

    private fun parseTransportations(lines: List<String>): Map<String, Transportation> {
        val map = mutableMapOf<String, Transportation>()
        var startParsing = false
        for (line in lines) {
            if (line.isEmpty()) {
                startParsing = true
                continue
            }

            if (!startParsing) {
                continue
            }

            val stripped = line.filter { it != ' ' }.split("=")
            val key = stripped[0]
            val transport = stripped[1].replace("(", "").replace(")", "").split(",")

            map[key] = Transportation(key, transport[0], transport[1])
        }

        return map
    }

    private fun parseInstructions(lines: List<String>): List<(Map<String, Transportation>, Transportation) -> Transportation> {
        val instructions = mutableListOf<(Map<String, Transportation>, Transportation) -> Transportation>()
        for ((_, line) in lines.withIndex()) {
            if (line.isEmpty()) {
                break
            }

            for (instruction in line) {
                if (instruction == 'L') {
                    instructions.add(::left)
                } else if (instruction == 'R') {
                    instructions.add(::right)
                }
            }
        }

        return instructions
    }

    private data class Transportation(val key: String, val left: String, val right: String)
}
