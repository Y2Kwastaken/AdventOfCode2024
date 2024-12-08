package sh.miles.aoc.utils.grid

import kotlin.jvm.Throws

class CharGrid(private val grid: Array<CharArray>, val width: Int, val height: Int) {

    @Throws(IllegalStateException::class)
    operator fun get(coord: GridCoord): Char {
        if (!contains(coord)) {
            throw IllegalStateException("Coordinate $coord is not within grid of width $width and height $height")
        }

        return grid[coord.y][coord.x]
    }

    @Throws(IllegalStateException::class)
    operator fun set(coord: GridCoord, value: Char) {
        if (!contains(coord)) {
            throw IllegalStateException("Coordinate $coord is not within grid of width $width and height $height")
        }

        grid[coord.y][coord.x] = value
    }

    fun findAll(value: Char): List<GridCoord> {
        val collector = mutableListOf<GridCoord>()
        for((ydex, y) in grid.withIndex()) {
            for((xdex, xValue) in y.withIndex()) {
                if (xValue == value) {
                    collector.add(GridCoord(xdex, ydex))
                }
            }
        }

        return collector
    }

    fun findFirst(value: Char): GridCoord {
        for ((ydex, y) in grid.withIndex()) {
            for ((xdex, xValue) in y.withIndex()) {
                if (xValue == value) {
                    return GridCoord(xdex, ydex)
                }
            }
        }

        return GridCoord.OUT_OF_BOUNDS
    }

    fun findFirstOrThrow(value: Char): GridCoord {
        val found = findFirst(value)
        if (found != GridCoord.OUT_OF_BOUNDS) {
            return found
        }

        throw IllegalArgumentException("Value $value could not be found within the grid of width $width and height $height")
    }

    fun contains(coord: GridCoord): Boolean {
        return coord.x in 0 until width && coord.y in 0 until height
    }

    fun asString(converter: Map<Char, Char>): String {
        val builder = StringBuilder()

        for (row in grid) {
            for (value in row) {
                builder.append(converter.getOrElse(value) { value })
            }
            builder.append("\n")
        }

        return builder.dropLast(1).toString()
    }

    fun copy(): CharGrid {
        val copy = Array(this.height) { CharArray(this.width) }
        for ((y, ints) in this.grid.withIndex()) {
            for ((x, value) in ints.withIndex()) {
                copy[y][x] = value
            }
        }
        return CharGrid(copy, this.width, this.height)
    }

    override fun toString(): String {
        return asString(mapOf())
    }

}
