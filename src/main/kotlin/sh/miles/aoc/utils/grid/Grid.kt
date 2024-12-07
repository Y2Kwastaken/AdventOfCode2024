package sh.miles.aoc.utils.grid

import kotlin.jvm.Throws

class Grid(private val grid: Array<IntArray>, val width: Int, val height: Int) {

    @Throws(IllegalStateException::class)
    operator fun get(coord: GridCoord): Int {
        if (!contains(coord)) {
            throw IllegalStateException("Coordinate $coord is not within grid of width $width and height $height")
        }

        return grid[coord.y][coord.x]
    }

    @Throws(IllegalStateException::class)
    operator fun set(coord: GridCoord, value: Int) {
        if (!contains(coord)) {
            throw IllegalStateException("Coordinate $coord is not within grid of width $width and height $height")
        }

        grid[coord.y][coord.x] = value
    }

    fun findFirst(value: Int): GridCoord {
        for ((ydex, y) in grid.withIndex()) {
            for ((xdex, xValue) in y.withIndex()) {
                if (xValue == value) {
                    return GridCoord(xdex, ydex)
                }
            }
        }

        return GridCoord.OUT_OF_BOUNDS
    }

    fun findFirstOrThrow(value: Int): GridCoord {
        val found = findFirst(value)
        if (found != GridCoord.OUT_OF_BOUNDS) {
            return found
        }

        throw IllegalArgumentException("Value $value could not be found within the grid of width $width and height $height")
    }

    fun contains(coord: GridCoord): Boolean {
        return coord.x in 0 until width && coord.y in 0 until height
    }

    fun asString(converter: Map<Int, Char>): String {
        val builder = StringBuilder()

        for (row in grid) {
            for (value in row) {
                builder.append(converter.getOrElse(value) { value })
            }
            builder.append("\n")
        }

        return builder.dropLast(1).toString()
    }

    fun copy(): Grid {
        val copy = Array(this.height) { IntArray(this.width) }
        for ((y, ints) in this.grid.withIndex()) {
            for ((x, value) in ints.withIndex()) {
                copy[y][x] = value
            }
        }
        return Grid(copy, this.width, this.height)
    }

    override fun toString(): String {
        return asString(mapOf())
    }

}
