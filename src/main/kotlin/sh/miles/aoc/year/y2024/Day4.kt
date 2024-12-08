package sh.miles.aoc.year.y2024

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day4 : Day {
    override fun run(file: Path): ResultUnion {
        val plane = Plane2d(
            file.readLines(), mapOf(
                '.' to -1,
                'X' to 0,
                'M' to 1,
                'A' to 2,
                'S' to 3
            )
        )

        // Part 1
        var count = 0
        for (x in 0 until plane.maxX) {
            for (y in 0 until plane.maxY) {
                count += plane.searchAllDirections(x, y, "XMAS")
            }
        }
        // end Part 1

        // Part 2
        var countPartTwo = 0
        for (x in 1 until plane.maxX) {
            for (y in 0 until plane.maxY) {
                if (plane.isWordSlanted(x, y, "MAS")) {
                    countPartTwo += 1
                }
            }
        }
        // end Part 2

        return ResultUnion(count, countPartTwo)
    }
}

private class Plane2d(lines: List<String>, private val dictionary: Map<Char, Int>) {
    private val state: Array<IntArray> = Array(lines.size) { IntArray(lines[0].length) };
    val maxX = state[0].size
    val maxY = state.size
    private val reverseDictionary: MutableMap<Int, Char> = mutableMapOf()

    init {
        dictionary.forEach { (key, value) ->
            reverseDictionary[value] = key
        }

        for ((gindex, line) in lines.withIndex()) {
            for ((index, character) in line.withIndex()) {
                state[gindex][index] = dictionary[character]!!;
            }
        }
    }

    fun set(x: Int, y: Int, character: Char) {
        state[y][x] = dictionary[character]!!
    }

    fun isWordSlanted(centerX: Int, centerY: Int, word: String): Boolean {
        val first = joinAsCenter(centerX, centerY, Direction.NORTH_EAST, word.length)
        val second = joinAsCenter(centerX, centerY, Direction.NORTH_WEST, word.length)

        return (first == word || first.reversed() == word) && (second == word || second.reversed() == word)
    }

    fun joinAsCenter(centerX: Int, centerY: Int, direction: Direction, length: Int): String {
        val aLength = length - 1
        if (aLength % 2 != 0) {
            throw IllegalArgumentException("Can not join at center of even length")
        }

        val joiner = StringBuilder().append(reverseDictionary[state[centerY][centerX]])
        var transformed: Pair<Int, Int> = Pair(centerX, centerY)
        for (i in (0 until (aLength / 2)).reversed()) {
            transformed = direction.invert(transformed)
            if (!isValidCoordinates(transformed)) {
                return ""
            }

            joiner.insert(0, reverseDictionary[state[transformed.second][transformed.first]])
        }

        transformed = Pair(centerX, centerY)
        for (i in (0 until (aLength / 2))) {
            transformed = direction.transform(transformed)
            if (!isValidCoordinates(transformed)) {
                break
            }

            joiner.append(reverseDictionary[state[transformed.second][transformed.first]])
        }

        return joiner.toString()
    }

    fun searchAllDirections(x: Int, y: Int, expected: String): Int {
        var count = 0
        for (value in Direction.entries) {
            if (search(x, y, value, expected)) count += 1
        }

        return count
    }

    fun search(x: Int, y: Int, direction: Direction, expected: String): Boolean {
        val length = expected.length
        return join(x, y, direction, length) == expected
    }

    fun join(x: Int, y: Int, direction: Direction, length: Int): String {
        val joiner = StringBuilder().append(reverseDictionary[state[y][x]])
        var transformed: Pair<Int, Int> = Pair(x, y)
        for (i in 0 until length - 1) {
            transformed = direction.transform(transformed)
            if (!isValidCoordinates(transformed)) {
                break
            }

            joiner.append(reverseDictionary[state[transformed.second][transformed.first]])
        }

        return joiner.toString()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        for (ints in state) {
            for (int in ints) {
                builder.append(reverseDictionary[int]!!)
            }
            builder.append("\n")
        }

        return builder.toString()
    }

    private fun isValidCoordinates(coordinates: Pair<Int, Int>): Boolean {
        return coordinates.first in 0 until maxX && coordinates.second in 0 until maxY
    }
}

private enum class Direction(private val offsetYToNext: Int, private val offsetXToNext: Int) {
    NORTH(-1, 0),
    SOUTH(1, 0),
    EAST(0, 1),
    WEST(0, -1),
    NORTH_EAST(-1, 1),
    SOUTH_EAST(1, 1),
    NORTH_WEST(-1, -1),
    SOUTH_WEST(1, -1);

    fun transform(xY: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(xY.first + offsetXToNext, xY.second + offsetYToNext)
    }

    fun invert(xY: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(xY.first - offsetXToNext, xY.second - offsetYToNext)
    }
}
