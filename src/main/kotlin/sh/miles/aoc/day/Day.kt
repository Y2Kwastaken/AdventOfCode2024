package sh.miles.aoc.day

import sh.miles.aoc.utils.ResultUnion
import java.nio.file.Path

interface Day {
    fun run(file: Path): ResultUnion;
}
