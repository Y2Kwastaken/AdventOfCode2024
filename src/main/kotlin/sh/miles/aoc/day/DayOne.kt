package sh.miles.aoc.day

import sh.miles.aoc.utils.ResultUnion
import java.nio.file.Path
import kotlin.io.path.readLines
import kotlin.math.max
import kotlin.math.min

object DayOne : Day {
    override fun run(file: Path): ResultUnion {
        val countMap = mutableMapOf<Int, Int>()
        val listOne = mutableListOf<Int>()
        val listTwo = mutableListOf<Int>()
        for (readLine in file.readLines()) {
            val split = readLine.split("   ")
            val first = split[0].toInt()
            listOne.add(first);
            listTwo.add(split[1].toInt())
            countMap[first] = 0;
        }

        listOne.sort();
        listTwo.sort();

        var totalDifference = 0;
        for (i in 0 until 1000) {
            val max = max(listOne[i], listTwo[i])
            val min = min(listOne[i], listTwo[i])

            totalDifference += (max - min);
        }

        for (i in listTwo) {
            countMap.computeIfPresent(i) { _, value -> value + 1 }
        }

        var totalSimilarity = 0;
        for (entry in countMap.entries) {
            val key = entry.key;
            val value = entry.value;

            totalSimilarity += (key * value)
        }

        return ResultUnion(totalDifference, totalSimilarity);
    }
}
