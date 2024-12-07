package sh.miles.aoc.utils.grid

data class GridCoord(val x: Int, val y: Int) {

    companion object {
        val OUT_OF_BOUNDS = GridCoord(-1, -1)
    }

    fun withDirection(direction: GridDirection): GridCoord {
        return withDirection(direction, 1, false)
    }

    fun withDirection(direction: GridDirection, times: Int): GridCoord {
        return withDirection(direction, times, false)
    }

    fun withDirection(direction: GridDirection, times: Int, opposite: Boolean): GridCoord {
        val negative = if (opposite) -1 else 1
        return this.withChanges(
            { x -> x + (direction.xOffset * times * negative) },
            { y -> y + (direction.yOffset * times * negative) })
    }

    fun withChanges(x: Int, y: Int): GridCoord {
        return GridCoord(this.x + x, this.y + y)
    }

    fun withChanges(xChange: (Int) -> Int, yChange: (Int) -> Int): GridCoord {
        return GridCoord(xChange(this.x), yChange(this.y))
    }
}
