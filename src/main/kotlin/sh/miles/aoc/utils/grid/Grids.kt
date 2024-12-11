package sh.miles.aoc.utils.grid

val CHAR_ARRAY_BUILDER = { w: Int, h: Int -> Array(h) { Array(w) { ' ' } } }
val INT_ARRAY_BUILDER = { w: Int, h: Int -> Array(h) { Array(w) { -1 } } }

fun charGridOf(input: List<String>): Grid<Char> {
    return gridOf(input, { it }, CHAR_ARRAY_BUILDER)
}

fun charGridOf(input: List<String>, valueConverter: (Char) -> Char): Grid<Char> {
    return gridOf(input, valueConverter, CHAR_ARRAY_BUILDER)
}

fun intGridOf(input: List<String>): Grid<Int> {
    return gridOf(input, { it.digitToInt() }, INT_ARRAY_BUILDER)
}

fun intGridOf(input: List<String>, valueConverter: (Char) -> Int): Grid<Int> {
    return gridOf(input, valueConverter, INT_ARRAY_BUILDER)
}

fun <E> gridOf(
    input: List<String>,
    valueConverter: (Char) -> E,
    arrayBuilder: (Int, Int) -> Array<Array<E>>
): Grid<E> {
    val height = input.size
    val width = input[0].length

    val array = arrayBuilder(width, height)
    for ((y, line) in input.withIndex()) {
        for ((x, value) in line.withIndex()) {
            array[y][x] = valueConverter(value)
        }
    }

    return Grid(array, width, height, arrayBuilder)
}
