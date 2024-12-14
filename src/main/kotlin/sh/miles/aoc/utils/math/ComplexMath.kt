package sh.miles.aoc.utils.math

object ComplexMath {

    fun applyGaussJordan(matrix: Array<DoubleArray>) {
        for ((pivotIndex, pivotRow) in matrix.withIndex()) {
            val pivot = pivotRow[pivotIndex]

            for (i in pivotIndex until pivotRow.size) {
                pivotRow[i] = pivotRow[i] / pivot
            }


            for (i in (pivotIndex + 1 until matrix.size)) {
                val belowRow = matrix[i]

                val secondaryPivot = belowRow[pivotIndex]
                for (j in belowRow.indices) {
                    belowRow[j] = belowRow[j] - (secondaryPivot * pivotRow[j])
                }
            }

            for (i in pivotIndex - 1 downTo 0) {
                val aboveRow = matrix[i]

                val secondaryPivot = aboveRow[pivotIndex]
                for (j in aboveRow.indices) {
                    aboveRow[j] = aboveRow[j] - (secondaryPivot * pivotRow[j])
                }
            }
        }
    }
}
