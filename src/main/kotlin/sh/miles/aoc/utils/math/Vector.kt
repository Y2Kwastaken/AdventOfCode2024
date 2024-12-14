package sh.miles.aoc.utils.math

data class Vector(val x: Int, val y: Int) {

    operator fun plus(vector: Vector): Vector {
        return Vector(this.x + vector.x, this.y + vector.y)
    }

    operator fun minus(vector: Vector): Vector {
        return Vector(this.x - vector.x, this.y - vector.y)
    }

    operator fun times(vector: Vector): Vector {
        return Vector(this.x * vector.x, this.y * vector.y)
    }

    operator fun div(vector: Vector): Vector {
        if (vector.x == 0 || vector.y == 0) throw IllegalArgumentException("Vector parameters must be non zero instead got $vector")
        return Vector(this.x / vector.x, this.y / vector.y)
    }

    fun plus(x: Int, y: Int): Vector {
        return Vector(this.x + x, this.y + y)
    }

    fun minus(x: Int, y: Int): Vector {
        return Vector(this.x - x, this.y - y)
    }

    fun times(x: Int, y: Int): Vector {
        return Vector(this.x * x, this.y * y)
    }

    fun div(x: Int, y: Int): Vector {
        if (x == 0 || y == 0) throw IllegalArgumentException("Vector parameters must be non zero instead got Vector($x,$y)")
        return Vector(this.x / x, this.y / y)
    }
}
