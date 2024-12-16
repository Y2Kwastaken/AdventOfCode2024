package sh.miles.aoc.utils.grid

import sh.miles.aoc.utils.math.Vector

enum class GridDirection(val yOffset: Int, val xOffset: Int, val vector: Vector) {
    NORTH(-1, 0, Vector(0, -1)),
    SOUTH(1, 0, Vector(0, 1)),
    EAST(0, 1, Vector(1, 0)),
    WEST(0, -1, Vector(-1, 0)),
    NORTH_EAST(-1, 1, Vector(1, -1)),
    SOUTH_EAST(1, 1, Vector(1, 1)),
    NORTH_WEST(-1, -1, Vector(-1, -1)),
    SOUTH_WEST(1, -1, Vector(-1, 1));

    companion object {
        val CARDINAL_DIRECTIONS = listOf(NORTH, SOUTH, EAST, WEST)
    }
}
