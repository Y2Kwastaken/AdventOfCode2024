package sh.miles.aoc.day

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.grid.CharGrid
import sh.miles.aoc.utils.grid.GridCoord
import sh.miles.aoc.utils.grid.GridSlope
import java.nio.file.Path
import kotlin.io.path.readLines

object DayEight : Day {
    override fun run(file: Path): ResultUnion {
        val grid = readGrid(file)
        val antennas = findAntennas(grid)

        val partOneGrid = grid.copy()
        var partOneCount = 0
        for (antennaList in antennas.values) {
            partOneCount += placeAntiNodes(partOneGrid, antennaList, ::antiNodePlacer1)
        }

        val partTwoGrid = grid.copy()
        var partTwoCount = 0
        for (antennaList in antennas.values) {
            partTwoCount += placeAntiNodes2(partTwoGrid, antennaList, ::antiNodePlacer2)
        }

        println(partTwoGrid)
        return ResultUnion(partOneCount, partTwoCount)
    }

    private fun placeAntiNodes2(
        grid: CharGrid, antennas: List<Antenna>, antiNodePlacer: (CharGrid, Antenna, GridSlope) -> Set<GridCoord>
    ): Int {
        val combinations = mutableListOf<Set<Antenna>>()
        generateCombinations(2, antennas, sortedSetOf(), combinations)
        val counted = mutableSetOf<GridCoord>()
        for (combination in combinations.toSet()) {
            if (combination.size < 2) continue
            val first = combination.first()
            val second = combination.last()
            assert(first != second)

            val slope = second.coord.slopeBetween(first.coord)
            counted += antiNodePlacer(grid, first, slope)
            counted += antiNodePlacer(grid, second, first.coord.slopeBetween(second.coord))
        }

        println(counted)
        return counted.size
    }

    private fun placeAntiNodes(
        grid: CharGrid, antennas: List<Antenna>, antiNodePlacer: (CharGrid, Antenna, GridSlope) -> Int
    ): Int {
        val combinations = mutableListOf<Set<Antenna>>()
        generateCombinations(2, antennas, sortedSetOf(), combinations)
        var count = 0
        for (combination in combinations) {
            if (combination.size < 2) continue
            val first = combination.first()
            val second = combination.last()
            assert(first != second)

            val slope = second.coord.slopeBetween(first.coord)
            count += antiNodePlacer(grid, first, slope)
        }

        return count
    }

    private fun antiNodePlacer1(grid: CharGrid, first: Antenna, slope: GridSlope): Int {
        val offset = first.coord.withSlope(slope)
        if (grid.contains(offset)) {
            if (grid[offset] != '#') {
                grid[offset] = '#'
                return 1
            }
        }

        return 0
    }

    private fun antiNodePlacer2(grid: CharGrid, first: Antenna, slope: GridSlope): Set<GridCoord> {
        var offset = first.coord.withSlope(slope)
        var counted = mutableSetOf<GridCoord>()
        while (grid.contains(offset)) {
            if (grid[offset] == '.' || grid[offset] == first.value) {
                if (grid[offset] == '.') grid[offset] = '#'
                counted.add(offset)
            }
            offset = offset.withSlope(slope)
        }

        val opposite = slope.negative()
        offset = first.coord.withSlope(opposite)
        while (grid.contains(offset) && (grid[offset] == '.' || grid[offset] == first.value)) {
            if ((grid[offset] == '.' || grid[offset] == first.value)) {
                if (grid[offset] == '.') grid[offset] = '#'
                counted.add(offset)
            }
            offset = offset.withSlope(opposite)
        }

        return counted
    }

    private fun generateCombinations(
        size: Int, antennas: List<Antenna>, currentCombination: Set<Antenna>, collector: MutableList<Set<Antenna>>
    ) {
        if (currentCombination.size == size) {
            collector.add(currentCombination.toSet())
            return
        }

        for (antenna in antennas) {
            if (!currentCombination.contains(antenna)) {
                generateCombinations(size, antennas, currentCombination + antenna, collector)
            }
        }
    }

    private fun findAntennas(grid: CharGrid): Map<Char, List<Antenna>> {
        val map = mutableMapOf<Char, MutableList<Antenna>>()
        for (y in 0 until grid.height) {
            for (x in 0 until grid.width) {
                val coord = GridCoord(x, y)
                val value = grid[coord]
                if (value == '.') continue
                map.putIfAbsent(value, mutableListOf())
                map[value]!!.add(Antenna(coord, value))
            }
        }


        for (value in map.values) {
            value.sort()
            value.reverse()
        }

        return map
    }

    private fun readGrid(file: Path): CharGrid {
        val lines = file.readLines()
        val height = lines.size
        val width = lines[0].length
        val array = Array(height) { CharArray(width) }

        for ((y, line) in lines.withIndex()) {
            for ((x, c) in line.withIndex()) {
                array[y][x] = c
            }
        }

        return CharGrid(array, width, height)
    }
}

private data class Antenna(val coord: GridCoord, val value: Char) : Comparable<Antenna> {
    override fun compareTo(other: Antenna): Int {
        val otherCoord = other.coord
        return (otherCoord.y - coord.y) + (otherCoord.x - coord.x) + (other.value - value)
    }

}
