package sh.miles.aoc.day

import sh.miles.aoc.utils.ResultUnion
import java.nio.file.Path
import kotlin.io.path.readLines

object DayThree : Day {
    val PART_ONE_REGEX = "(mul\\([0-9]{1,3},[0-9]{1,3}\\))".toRegex()

    override fun run(file: Path): ResultUnion {
        val partOne = file.readLines().sumOf { parse(it) }

        return ResultUnion(partOne)
    }

    private fun parse(line: String): Int {
        val matches = PART_ONE_REGEX.findAll(line)
        var count = 0;
        for (match in matches) {
            for (groupValue in match.groupValues) {
                val stripped = groupValue.drop(4).replace(")", "").split(",")
                count += (stripped[0].toInt() * stripped[1].toInt())
            }
        }

        return count / 2
    }

}
