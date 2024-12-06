package sh.miles.aoc.day

import sh.miles.aoc.utils.ResultUnion
import java.nio.file.Path
import kotlin.io.path.readLines

object DayFive : Day {
    override fun run(file: Path): ResultUnion {
        val lines = file.readLines()
        val unparsedRules = lines.filter { it.contains("|") }
        val unparsedInput = lines.filter { !it.contains("|") }.drop(1)

        val rules = parseRules(unparsedRules)
        val input = unparsedInput.map { orderInput(it, rules) }

        for (ints in input) {
//            println(ints)
        }

        return ResultUnion(
            input.filter { (first, second) -> first == second }.sumOf { (first, _) -> first[first.size / 2] },
            input.filter { (first, second) -> first != second }.sumOf { (first, _) -> first[first.size / 2] }
        )
    }

    private fun parseRules(rules: List<String>): Map<Int, PrintEntry> {
        val map = mutableMapOf<Int, PrintEntry>()

        for (rule in rules) {
            val split = rule.split("|")
            val dependency = split[0].toInt()
            val page = split[1].toInt()

            map.computeIfAbsent(page) { _ -> PrintEntry(page, setOf()) }
            map.computeIfPresent(page) { _, value -> value.withRequirement(dependency) }
        }

        println(map)
        return map
    }

    private fun orderInput(order: String, rules: Map<Int, PrintEntry>): Pair<List<Int>, List<Int>> {
        val loadOrder = mutableListOf<Int>()
        val loaded = mutableSetOf<Int>()
        val numbers = order.split(",").map { it.toInt() }.toMutableList()
        for (number in numbers) {
            if (loaded.contains(number)) {
                continue
            }

            load(number, rules[number], numbers, loaded, loadOrder, rules)
        }

        return Pair(loadOrder, numbers)
    }

    private fun load(
        number: Int,
        entry: PrintEntry?,
        numbers: List<Int>,
        loaded: MutableSet<Int>,
        loadOrder: MutableList<Int>,
        rules: Map<Int, PrintEntry>
    ) {
        if (entry == null) {
            loadOrder.add(number)
            loaded.add(number)
            return
        }

        if (!entry.hasRequirements()) {
            loadOrder.add(number)
            loaded.add(number)
            return
        }

        for (require in entry.requires) {
            if (numbers.contains(require) && !loaded.contains(require)) {
                load(require, rules[require], numbers, loaded, loadOrder, rules)
            }
        }

        loaded.add(number)
        loadOrder.add(number)
    }

    private data class PrintEntry(val page: Int, val requires: Set<Int>) {

        fun hasRequirements(): Boolean {
            return requires.isNotEmpty()
        }

        fun withRequirement(requirement: Int): PrintEntry {
            val mutable = requires.toMutableSet()
            mutable.add(requirement)
            return PrintEntry(this.page, mutable.toSet())
        }

        override fun toString(): String {
            val builder = StringBuilder()
            builder.append("{").append(page).append(" | ")
            for (require in requires) {
                builder.append(require).append(",")
            }

            return builder.insert(builder.length - 1, "}").dropLast(1).toString()
        }
    }
}
