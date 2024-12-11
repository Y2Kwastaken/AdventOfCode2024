package sh.miles.aoc.year.y2024

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.graph.NaiveGraphNode
import sh.miles.aoc.utils.grid.Grid
import sh.miles.aoc.utils.grid.GridCoord
import sh.miles.aoc.utils.grid.GridDirection
import sh.miles.aoc.utils.grid.intGridOf
import sh.miles.aoc.utils.timeFunction
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day10 : Day {
    private data class WrappedPosition(val coord: GridCoord, val value: Int)

    override fun run(file: Path): ResultUnion {
        val grid = intGridOf(file.readLines())
        val partOne = timeFunction { part(grid, mutableSetOf()) }
        val partTwo = timeFunction { part(grid, mutableListOf()) }

        return ResultUnion(partOne, partTwo)
    }

    private fun part(grid: Grid<Int>, collection: MutableCollection<NaiveGraphNode<WrappedPosition>>): Int {
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

    private fun buildNode(grid: Grid<Int>, source: GridCoord): NaiveGraphNode<WrappedPosition> {
        val root = NaiveGraphNode(WrappedPosition(source, 0))
        searchAround(grid, source, 0, root)
        return root
    }

    private fun searchAround(
        grid: Grid<Int>,
        source: GridCoord,
        currentValue: Int,
        node: NaiveGraphNode<WrappedPosition>
    ) {
        val collected = grid.collectAround(source, GridDirection.CARDINAL_DIRECTIONS).filter { grid[it] > currentValue }
        for (gridCoord in collected) {
            val newNode = NaiveGraphNode(WrappedPosition(gridCoord, grid[gridCoord]))
            node.connectWith(newNode)
            searchAround(grid, gridCoord, grid[gridCoord], newNode)
        }
    }
}
