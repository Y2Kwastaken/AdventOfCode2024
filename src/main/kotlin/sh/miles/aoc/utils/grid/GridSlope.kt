package sh.miles.aoc.utils.grid

data class GridSlope(val rise: Int, val run: Int, val positive: Boolean) {

    constructor(rise: Int, run: Int) : this(
        rise, run, (rise * run) > 0
    )

    fun negative(): GridSlope {
        return GridSlope(-1 * rise, -1 * run)
    }

    fun invert(): GridSlope {
        return GridSlope(rise, rise)
    }

}
