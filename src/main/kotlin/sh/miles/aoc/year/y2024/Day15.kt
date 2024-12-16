package sh.miles.aoc.year.y2024

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.grid.Grid
import sh.miles.aoc.utils.grid.GridCoord
import sh.miles.aoc.utils.grid.GridDirection
import sh.miles.aoc.utils.grid.charGridOf
import sh.miles.aoc.utils.operate
import sh.miles.aoc.utils.split
import sh.miles.aoc.utils.timeFunction
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day15 : Day {
    private const val ROBOT = '@'
    private const val IMMOVABLE = '#'
    private const val BOX = 'O'
    private const val BOX_LEFT = '['
    private const val BOX_RIGHT = ']'
    private const val EMPTY_SPACE = '.'

    override fun run(file: Path): ResultUnion {
        val lines = file.readLines().split("")
        val grid = charGridOf(lines[0])
        val instructions = lines[1].map { it.map(::charToDirection) }.flatten()

        val grid2 = charGridOf(lines[0].map { line ->
            line.map {
                if (it == 'O') {
                    "[]"
                } else if (it == '.') {
                    ".."
                } else if (it == '#') {
                    "##"
                } else {
                    "$it."
                }
            }.joinToString("")
        })

        return ResultUnion(
            timeFunction { partOne(grid.copy(), instructions) },
            timeFunction { partTwo(grid2.copy(), instructions) }
        )
    }

    private fun partTwo(grid: Grid<Char>, instructions: List<GridDirection>): Int {
        var robot = grid.findFirst(ROBOT)

        for (instruction in instructions) {
            val peek = robot.withDirection(instruction)
            assert(grid.contains(peek))
            if (grid[peek] == IMMOVABLE) {
                continue
            }

            if (grid[peek] == EMPTY_SPACE) {
                grid[robot] = EMPTY_SPACE
                grid[peek] = ROBOT
                robot = peek
                continue
            }

            when (instruction) {
                GridDirection.WEST, GridDirection.EAST -> {
                    attemptPush(grid, robot, peek, instruction)
                    robot = grid.findFirst(ROBOT)
                }

                GridDirection.NORTH, GridDirection.SOUTH -> {
                    val collector = mutableListOf<Pair<GridCoord, GridCoord>>()
                    val result = collectBoxesNorthSouth(grid, robot, instruction, collector)
                    if (!result) continue
                    collector.flatMap { listOf(it.first, it.second) }
                        .map { Triple(it.withDirection(instruction), it, grid[it]) }.operate {
                        if (!grid.contains(it.first)) throw IllegalArgumentException("Out of bounds while shifting coordinate ${it.second}[${it.third}] to ${it.first} with a shift vector of")
                        grid[it.second] = EMPTY_SPACE
                    }.forEach { (coord, _, value) ->
                        grid[coord] = value
                    }
                    grid[robot] = EMPTY_SPACE
                    robot = robot.withDirection(instruction)
                    grid[robot] = ROBOT
                }

                else -> throw IllegalArgumentException("unknown instruction $instruction")
            }
        }

        return grid.findAll(BOX_LEFT).sumOf { (it.y * 100) + it.x }
    }

    private fun partOne(grid: Grid<Char>, instructions: List<GridDirection>): Int {
        var robot = grid.findFirst(ROBOT)

        for (instruction in instructions) {
            val peek = robot.withDirection(instruction)
            assert(grid.contains(peek))
            if (grid[peek] == IMMOVABLE) {
                continue
            }

            if (grid[peek] == EMPTY_SPACE) {
                grid[robot] = EMPTY_SPACE
                grid[peek] = ROBOT
                robot = peek
                continue
            }

            if (grid[peek] == BOX) {
                attemptPush(grid, robot, peek, instruction)
                robot = grid.findFirst(ROBOT)
            }
        }

        return grid.findAll(BOX).sumOf { (it.y * 100) + it.x }
    }

    private fun attemptPush(grid: Grid<Char>, robot: GridCoord, vision: GridCoord, direction: GridDirection) {
        val test = timesToEmptySpace(grid, vision, direction)
        if (test == -1) return

        grid.shift(robot, vision.withDirection(direction, test), direction.vector, '.')
    }

    private fun timesToEmptySpace(grid: Grid<Char>, start: GridCoord, direction: GridDirection): Int {
        var stepCount = 0
        var step = start
        assert(grid.contains(step))

        while (grid[step] != IMMOVABLE) {
            if (grid[step] == EMPTY_SPACE) {
                return stepCount
            }

            stepCount++
            step = step.withDirection(direction)
        }

        return -1
    }

    private fun charToDirection(char: Char): GridDirection {
        return when (char) {
            '<' -> GridDirection.WEST
            '>' -> GridDirection.EAST
            '^' -> GridDirection.NORTH
            'v' -> GridDirection.SOUTH
            else -> {
                throw IllegalArgumentException("Unknown Char [$char]")
            }
        }
    }

    private fun collectBoxesNorthSouth(
        grid: Grid<Char>,
        source: GridCoord,
        direction: GridDirection,
        boxes: MutableList<Pair<GridCoord, GridCoord>>
    ): Boolean {
        if (direction != GridDirection.NORTH && direction != GridDirection.SOUTH) return false

        val peek = source.withDirection(direction)
        if (grid[peek] == IMMOVABLE) {
            return false
        }

        if (grid[peek] == EMPTY_SPACE) {
            return true
        }

        if (grid[peek] == BOX_LEFT) {
            boxes.add(Pair(peek, peek.withDirection(GridDirection.EAST)))
            val result = collectBoxesNorthSouth(grid, peek, direction, boxes)
            val result1 = collectBoxesNorthSouth(grid, peek.withDirection(GridDirection.EAST), direction, boxes)

            if (!result1 || !result) {
                return false
            }
        }

        if (grid[peek] == BOX_RIGHT) {
            boxes.add(Pair(peek, peek.withDirection(GridDirection.WEST)))
            val result = collectBoxesNorthSouth(grid, peek, direction, boxes)
            val result1 = collectBoxesNorthSouth(grid, peek.withDirection(GridDirection.WEST), direction, boxes)

            if (!result1 || !result) {
                return false
            }
        }


        return true
    }
}
