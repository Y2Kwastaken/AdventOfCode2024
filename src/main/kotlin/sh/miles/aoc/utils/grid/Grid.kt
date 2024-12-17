package sh.miles.aoc.utils.grid

import sh.miles.aoc.utils.math.Vector
import sh.miles.aoc.utils.operate

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

    fun findAll(values: List<E>): List<GridCoord> {
        val collector = mutableListOf<GridCoord>()
        for (value in values) {
            collector.addAll(findAll(value))
        }

        return collector
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

    fun collectAll(
        coord: GridCoord,
        directions: List<GridDirection>,
        doCollect: (E, E) -> Boolean,
        collector: MutableCollection<GridCoord>,
    ) {
        collector.add(coord)
        val coordValue = grid[coord.y][coord.x]

        for (direction in directions) {
            val modified = coord.withDirection(direction)
            if (contains(modified) && doCollect(coordValue, grid[modified.y][modified.x]) && !collector.contains(
                    modified
                )
            ) {
                collectAll(modified, directions, doCollect, collector)
            }
        }
    }

    fun shift(first: GridCoord, second: GridCoord, shift: Vector, fillEmptyWith: E) {
        if (!contains(first) || !contains(second)) {
            throw IllegalArgumentException("$first and $second must be inside the grid of width $width and height $height")
        }

        val capture: MutableList<Pair<GridCoord, E>> = mutableListOf()

        var step = first
        while(step != second) {
            capture.add(Pair(GridCoord(step.x, step.y), grid[step.y][step.x]))
            step = step.withVelocity(shift)
        }

        capture.map { Triple(it.first.withVelocity(shift), it.first, it.second) }.operate { (coord, initial, value) ->
            if (!contains(coord)) throw IllegalArgumentException("Out of bounds while shifting coordinate $initial[$value] to $coord with a shift vector of $shift")
            grid[initial.y][initial.x] = fillEmptyWith
        }.forEach { (coord, _, value) ->
            grid[coord.y][coord.x] = value
        }
    }

    fun countForeignFaces(coord: GridCoord, includeOutOfBounds: Boolean): Int {
        if (!contains(coord)) return -1

        var count = 0
        for (direction in GridDirection.CARDINAL_DIRECTIONS) {
            val modified = coord.withDirection(direction)
            if (!contains(modified).also { if (!it && includeOutOfBounds) count++ }) {
                continue
            }

            if (grid[coord.y][coord.x] != grid[modified.y][modified.x]) {
                count++
            }
        }

        return count
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

    fun copy(low: GridCoord, high: GridCoord): Grid<E> {
        val width = (high.x + 1 - low.x)
        val height = (high.y + 1 - low.y)

        val copy = this.arrayBuilder(this.width, this.height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                copy[y][x] = grid[low.y + y][low.x + x]
            }
        }

        return Grid(copy, width, height, this.arrayBuilder)
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
