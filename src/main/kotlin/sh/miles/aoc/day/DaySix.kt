package sh.miles.aoc.day

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.grid.Grid
import sh.miles.aoc.utils.grid.GridCoord
import sh.miles.aoc.utils.grid.GridDirection
import java.nio.file.Path
import kotlin.io.path.readLines

object DaySix : Day {
    override fun run(file: Path): ResultUnion {
        val grid = readGrid(file)
        val partOne = partOne(grid.copy())
        val partTwo = partTwo(grid.copy(), partOne)

        return ResultUnion(partOne.size, partTwo)
    }

    private fun partTwo(grid: Grid, positionsVisited: Set<GridCoord>): Int {
        var count = 0
        for (y in 0 until grid.height) {
            for (x in 0 until grid.width) {
                if (partTwo0(grid.copy(), GridCoord(x, y))) count++
            }
        }
        return count
    }

    private fun partTwo0(grid: Grid, obstruct: GridCoord): Boolean {
        var status = true
        val visited = mutableSetOf<GuardState>()
        var position = grid.findFirstOrThrow(RoomElements.NAVIGATOR.value)
        var direction = GridDirection.NORTH

        if (obstruct != position) {
            grid[obstruct] = RoomElements.BLOCKAGE.value
        }

        do {
            val next = position.withDirection(direction)
            if (!grid.contains(next)) {
                visited.add(GuardState(position, direction))
                grid[position] = RoomElements.PATH.value
                status = false
                break
            }

            visited.add(GuardState(position, direction))
            if (grid[next] == RoomElements.BLOCKAGE.value) {
                direction = nextDirection(direction)
            }

            grid[position] = RoomElements.PATH.value
            position = position.withDirection(direction)
        } while (grid.contains(position) && !visited.contains(GuardState(position, direction)))

        return status
    }

    private fun partOne(grid: Grid): Set<GridCoord> {
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

    fun nextDirection(direction: GridDirection): GridDirection {
        return when (direction) {
            GridDirection.NORTH -> GridDirection.EAST
            GridDirection.EAST -> GridDirection.SOUTH
            GridDirection.SOUTH -> GridDirection.WEST
            GridDirection.WEST -> GridDirection.NORTH
            else -> throw IllegalArgumentException("Can not find next direction for ${direction.name}")
        }
    }

    private fun readGrid(file: Path): Grid {
        val lines = file.readLines()
        val height = lines.size
        val width = lines[0].length
        val grid = Array(height) { IntArray(width) }

        for ((y, line) in lines.withIndex()) {
            for ((x, value) in line.withIndex()) {
                grid[y][x] = RoomElements.convert(value)
            }
        }

        return Grid(grid, width, height)
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
