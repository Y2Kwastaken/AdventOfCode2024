package sh.miles.aoc.year.y2024

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.graph.NaiveGraphNode
import sh.miles.aoc.utils.grid.GridCoord
import sh.miles.aoc.utils.grid.GridDirection
import sh.miles.aoc.utils.grid.IntGrid
import sh.miles.aoc.utils.timeFunction
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day10 : Day {
    private val PART_ONE_DIRECTIONS =
        listOf(GridDirection.NORTH, GridDirection.SOUTH, GridDirection.EAST, GridDirection.WEST)

    private data class WrappedPosition(val coord: GridCoord, val value: Int)

    override fun run(file: Path): ResultUnion {
        val grid = readGrind(file)
        val partOne = timeFunction { part(grid, mutableSetOf()) }
        val partTwo = timeFunction { part(grid, mutableListOf()) }

        return ResultUnion(partOne, partTwo)
    }

    private fun part(grid: IntGrid, collection: MutableCollection<NaiveGraphNode<WrappedPosition>>): Int {
        val starts = grid.findAll(0)

        var count = 0
        for (start in starts) {
            val node = buildNode(grid, start)
            countTrailHeads(node, 0, 9, collection)
            count += collection.size
            collection.clear()
        }

        return count
    }

    private fun countTrailHeads(
        root: NaiveGraphNode<WrappedPosition>,
        minBound: Int,
        maxRequired: Int,
        heads: MutableCollection<NaiveGraphNode<WrappedPosition>>
    ) {
        for (node in root) {
            if (node.value.value != root.value.value + 1) continue

            if (node.value.value >= maxRequired) {
                heads.add(node)
            } else {
                countTrailHeads(node, minBound, maxRequired, heads)
            }
        }
    }

    private fun buildNode(grid: IntGrid, source: GridCoord): NaiveGraphNode<WrappedPosition> {
        val root = NaiveGraphNode(WrappedPosition(source, 0))
        searchAround(grid, source, 0, root)
        return root
    }

    private fun searchAround(
        grid: IntGrid,
        source: GridCoord,
        currentValue: Int,
        node: NaiveGraphNode<WrappedPosition>
    ) {
        val collected = grid.collectAround(source, PART_ONE_DIRECTIONS).filter { grid[it] > currentValue }
        for (gridCoord in collected) {
            val newNode = NaiveGraphNode(WrappedPosition(gridCoord, grid[gridCoord]))
            node.connectWith(newNode)
            searchAround(grid, gridCoord, grid[gridCoord], newNode)
        }
    }

    private fun readGrind(file: Path): IntGrid {
        val lines = file.readLines()
        val height = lines.size
        val width = lines[0].length
        val grid = Array(height) { IntArray(width) }

        for ((y, line) in lines.withIndex()) {
            for ((x, value) in line.withIndex()) {
                if (value == '.') {
                    grid[y][x] = -1
                } else {
                    grid[y][x] = value.digitToInt()
                }
            }
        }

        return IntGrid(grid, width, height)

    }
}
