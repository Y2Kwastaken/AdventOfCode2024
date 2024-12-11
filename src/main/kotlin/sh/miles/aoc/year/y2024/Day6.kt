package sh.miles.aoc.year.y2024

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.grid.Grid
import sh.miles.aoc.utils.grid.GridCoord
import sh.miles.aoc.utils.grid.GridDirection
import sh.miles.aoc.utils.grid.intGridOf
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day6 : Day {
    override fun run(file: Path): ResultUnion {
        val grid = intGridOf(file.readLines(), { RoomElements.convert(it) })
        val partOne = partOne(grid.copy())
        val partTwo = partTwo(grid.copy(), partOne)

        return ResultUnion(partOne.size, partTwo)
    }

    private fun partTwo(grid: Grid<Int>, positionsVisited: Set<GridCoord>): Int {
        var count = 0
        for (coord in positionsVisited) {
            if (partTwo0(grid.copy(), coord)) count++
        }
        return count
    }

    private fun partTwo0(grid: Grid<Int>, obstruct: GridCoord): Boolean {
        val visited = mutableSetOf<GuardState>()
        var position: GridCoord = grid.findFirst(RoomElements.NAVIGATOR.value)
        var direction = GridDirection.NORTH

        if (obstruct != position) {
            grid[obstruct] = RoomElements.PLACED_BLOCKAGE.value
        } else {
            return false
        }

        while (true) {
            visited.add(GuardState(position, direction))

            var peek = position.withDirection(direction)
            if (visited.contains(GuardState(peek, direction))) {
                return true
            }

            if (!grid.contains(peek)) {
                return false
            }

            if (grid[peek] == RoomElements.BLOCKAGE.value || grid[peek] == RoomElements.PLACED_BLOCKAGE.value) {
                direction = nextDirection(direction)
                continue
            }

            position = position.withDirection(direction)
        }
    }

    private fun partOne(grid: Grid<Int>): Set<GridCoord> {
        val visited = mutableSetOf<GridCoord>()
        var position = grid.findFirstOrThrow(RoomElements.NAVIGATOR.value)
        var direction = GridDirection.NORTH

        do {
            val next = position.withDirection(direction)
            if (!grid.contains(next)) {
                visited.add(position)
                grid[position] = RoomElements.PATH.value
                break
            }

            visited.add(position)
            if (grid[next] == RoomElements.BLOCKAGE.value) {
                direction = nextDirection(direction)
            }

            grid[position] = RoomElements.PATH.value
            position = position.withDirection(direction)
        } while (grid.contains(position))


        return visited
    }

    private fun nextDirection(direction: GridDirection): GridDirection {
        return when (direction) {
            GridDirection.NORTH -> GridDirection.EAST
            GridDirection.EAST -> GridDirection.SOUTH
            GridDirection.SOUTH -> GridDirection.WEST
            GridDirection.WEST -> GridDirection.NORTH
            else -> throw IllegalArgumentException("Can not find next direction for ${direction.name}")
        }
    }

    private data class GuardState(val location: GridCoord, val direction: GridDirection)

    private enum class RoomElements(val char: Char, val value: Int) {
        EMPTY_SPACE('.', 0),
        BLOCKAGE('#', 1),
        PLACED_BLOCKAGE('P', 4),
        NAVIGATOR('^', 2),
        PATH('X', 3);

        companion object {
            fun convert(num: Int): Char {
                for (value in entries) {
                    if (value.value == num) {
                        return value.char
                    }
                }

                throw IllegalArgumentException("Invalid Value $num")
            }

            fun convert(char: Char): Int {
                for (value in entries) {
                    if (value.char == char) {
                        return value.value
                    }
                }

                throw IllegalArgumentException("Invalid Char Value $char")
            }

            fun toMap(): Map<Int, Char> {
                return entries.associateByTo(mutableMapOf(), { it.value }, { it.char })
            }
        }
    }
}
