package sh.miles.aoc.utils.grid

import kotlin.jvm.Throws

class Grid<E>(
    private val grid: Array<Array<E>>,
    val width: Int,
    val height: Int,
    private val arrayBuilder: (Int, Int) -> Array<Array<E>>
) {

    @Throws(IllegalStateException::class)
    operator fun get(coord: GridCoord): E {
        if (!contains(coord)) {
            throw IllegalStateException("Coordinate $coord is not within grid of width $width and height $height")
        }

        return grid[coord.y][coord.x]
    }

    @Throws(IllegalStateException::class)
    operator fun set(coord: GridCoord, value: E) {
        if (!contains(coord)) {
            throw IllegalStateException("Coordinate $coord is not within grid of width $width and height $height")
        }

        grid[coord.y][coord.x] = value
    }

    fun findAll(value: E): List<GridCoord> {
        val collector = mutableListOf<GridCoord>()
        for ((y, line) in grid.withIndex()) {
            for ((x, xValue) in line.withIndex()) {
                if (xValue == value) {
                    collector.add(GridCoord(x, y))
                }
            }
        }

        return collector
    }

    fun findFirst(value: E): GridCoord {
        for ((y, line) in grid.withIndex()) {
            for ((x, xValue) in line.withIndex()) {
                if (xValue == value) {
                    return GridCoord(x, y)
                }
            }
        }

        return GridCoord.OUT_OF_BOUNDS
    }

    fun findFirstOrThrow(value: E): GridCoord {
        val found = findFirst(value)
        if (found != GridCoord.OUT_OF_BOUNDS) {
            return found
        }

        throw IllegalArgumentException("Value $value could not be found within the grid of width $width and height $height")
    }

    fun collectAround(coord: GridCoord, directions: List<GridDirection>): List<GridCoord> {
        if (!contains(coord)) return listOf()
        val collector = mutableListOf<GridCoord>()

        for (direction in directions) {
            val modified = coord.withDirection(direction)
            if (!contains(modified)) continue
            collector.add(modified)
        }

        return collector
    }

    fun asString(converter: Map<E, String>): String {
        val builder = StringBuilder()

        for (row in grid) {
            for (value in row) {
                builder.append(converter.getOrElse(value) { value })
            }
            builder.append("\n")
        }

        return builder.dropLast(1).toString()
    }

    override fun toString(): String {
        return asString(mapOf())
    }

    fun copy(): Grid<E> {
        val copy = this.arrayBuilder(this.width, this.height)

        for ((y, line) in grid.withIndex()) {
            for ((x, e) in line.withIndex()) {
                copy[y][x] = e
            }
        }

        return Grid(copy, this.width, this.height, this.arrayBuilder)
    }

    fun contains(coord: GridCoord): Boolean {
        return coord.x in 0 until width && coord.y in 0 until height
    }
}
