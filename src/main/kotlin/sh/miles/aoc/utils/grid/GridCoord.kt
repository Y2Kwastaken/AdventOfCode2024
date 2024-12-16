package sh.miles.aoc.utils.grid

import sh.miles.aoc.utils.math.Vector
import kotlin.math.max
import kotlin.math.min

data class GridCoord(val x: Int, val y: Int) {

    companion object {
        val OUT_OF_BOUNDS = GridCoord(-1, -1)

        fun max(first: GridCoord, second: GridCoord): GridCoord {
            return GridCoord(max(first.x, second.x), max(first.y, second.y))
        }

        fun min(first: GridCoord, second: GridCoord): GridCoord {
            return GridCoord(min(first.x, second.x), min(first.y, second.y))
        }
    }

    fun asVector(): Vector {
        return Vector(this.x, this.y)
    }

    fun withVelocity(velocity: Vector): GridCoord {
        return GridCoord(this.x + velocity.x, this.y + velocity.y)
    }

    fun withVelocityBounded(velocity: Vector, width: Int, height: Int, wrap: Boolean): GridCoord {
        var x = this.x + velocity.x
        var y = this.y + velocity.y

        while (x >= width && wrap) {
            x -= width
        }

        while (x < 0 && wrap) {
            x += width
        }

        while (y >= height && wrap) {
            y -= height
        }

        while (y < 0 && wrap) {
            y += height
        }

        if (x > width || x < 0 || y > height || y < 0) {
            return GridCoord(this.x, this.y)
        }

        return GridCoord(x, y)
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

    fun withSlope(slope: GridSlope): GridCoord {
        return GridCoord(this.x + slope.run, this.y + slope.rise)
    }

    fun slopeBetween(other: GridCoord): GridSlope {
        return GridSlope(other.y - this.y, other.x - this.x)
    }

}
