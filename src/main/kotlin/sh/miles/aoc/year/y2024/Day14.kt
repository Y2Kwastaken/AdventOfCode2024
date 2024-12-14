package sh.miles.aoc.year.y2024

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.grid.Grid
import sh.miles.aoc.utils.grid.GridCoord
import sh.miles.aoc.utils.grid.INT_ARRAY_BUILDER
import sh.miles.aoc.utils.grid.gridOf
import sh.miles.aoc.utils.math.Vector
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day14 : Day {
    override fun run(file: Path): ResultUnion {
        val robots = file.readLines().map {
            it.split(" ").map {
                val position = it.split("=")[1].split(",").map { it.toInt() }
                GridCoord(position[0], position[1])
            }.zipWithNext().map { (first, second) -> Pair(first, Vector(second.x, second.y)) }
        }.flatten()

        val grid = gridOf(0, 101, 103, INT_ARRAY_BUILDER)
        robots.map { it.first.withVelocityBounded(it.second.times(100, 100), grid.width, grid.height, true) }.forEach {
            grid[it] = grid[it] + 1
        }

        return ResultUnion(quadrantCounter(grid))
    }

    private fun quadrantCounter(grid: Grid<Int>): Int {
        val centerWidth = (grid.width / 2)
        val centerHeight = (grid.height / 2)

        val quadrant = intArrayOf(0, 0, 0, 0)
        for (y in 0 until grid.height) {
            if (y == centerHeight) {
                continue
            }

            for (x in 0 until grid.width) {
                if (x > centerWidth && y < centerHeight) {
                    quadrant[0] = quadrant[0] + grid[GridCoord(x, y)]
                } else if (x < centerWidth && y < centerHeight) {
                    quadrant[1] = quadrant[1] + grid[GridCoord(x, y)]
                } else if (x < centerWidth && y > centerHeight) {
                    quadrant[2] = quadrant[2] + grid[GridCoord(x, y)]
                } else if (x > centerWidth && y > centerHeight) {
                    quadrant[3] = quadrant[3] + grid[GridCoord(x, y)]
                }
            }
        }

        return quadrant.reduce { acc, i -> acc * i }
    }
}
