package sh.miles.aoc.year.y2024

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.grid.Grid
import sh.miles.aoc.utils.grid.GridCoord
import sh.miles.aoc.utils.grid.GridDirection
import sh.miles.aoc.utils.grid.charGridOf
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day12 : Day {
    override fun run(file: Path): ResultUnion {
        val grid = charGridOf(file.readLines())
        val grid2 = grid.copy()

        return ResultUnion(
//            grid.findUniqueEntries().map { entry ->
//                var count = 0
//                while (grid.findCountDispose(entry).also { if (it != -1) count += it } != -1) {
//                }
//                count
//            }.sumOf { it },
            grid2.findUniqueEntries().map { entry ->
                var count = 0
                grid2.findCountDisposeBulked(entry).also { if (it != -1) count += it }
                count
            }.sumOf { it }
        )
    }

    private fun Grid<Char>.findUniqueEntries(): Set<Char> {
        val unique = mutableSetOf<Char>()
        for (y in 0 until this.height) {
            for (x in 0 until this.width) {
                unique.add(this[GridCoord(x, y)])
            }
        }

        return unique
    }

    private fun Grid<Char>.findCountDispose(value: Char): Int {
        val first = findFirst(value)
        if (first == GridCoord.OUT_OF_BOUNDS) return -1
        val collector = mutableSetOf<GridCoord>()
        collectAll(first, GridDirection.CARDINAL_DIRECTIONS, { first, second -> first == second }, collector)

        var perimeter = 0
        for (gridCoord in collector) {
            perimeter += countForeignFaces(gridCoord, true)
        }

        for (gridCoord in collector) {
            this[gridCoord] = '.'
        }

        return perimeter * collector.size
    }

    private fun Grid<Char>.findCountDisposeBulked(value: Char): Int {
        if (value != 'C') return 0
        val first = findFirst(value)
        if (first == GridCoord.OUT_OF_BOUNDS) return -1
        val collector = mutableSetOf<GridCoord>()
        collectAll(first, GridDirection.CARDINAL_DIRECTIONS, { first, second -> first == second }, collector)
        val ySorted =
            collector.sortedBy { it.y }.filterIndexed { index, _ -> index == 0 || index == collector.size - 1 }
        val xSorted =
            collector.sortedBy { it.x }.filterIndexed { index, _ -> index == 0 || index == collector.size - 1 }

        val low = GridCoord(xSorted.first().x, ySorted.first().y)
        val high = GridCoord(xSorted.last().x, ySorted.last().y)
        val copy = this.copy(low, high)
        println(copy)
        println(copy.sides(copy.findFirst('C'),  mutableSetOf(), mutableSetOf()))
        var sides = 0


        return 0
    }

    private fun Grid<Char>.sides(
        start: GridCoord,
        visited: MutableSet<GridCoord>,
        peeked: MutableSet<Pair<GridDirection, GridCoord>>
    ): Int {
        if (visited.contains(start)) {
            return 0
        }

        visited.add(start)

        var sides = 0
        for (direction in GridDirection.CARDINAL_DIRECTIONS) {
            val peek = start.withDirection(direction)
            val directional = Pair(direction, peek)
            if (!contains(peek)) {
                println(peek)
                peeked.add(directional)
                sides++
            } else {
                if (this[peek] != this[start]) {
                    println(peek)
                    peeked.add(directional)
                    sides++
                } else sides += sides(peek, visited, peeked)
            }
        }

        println(peeked)
        return sides
    }
}
