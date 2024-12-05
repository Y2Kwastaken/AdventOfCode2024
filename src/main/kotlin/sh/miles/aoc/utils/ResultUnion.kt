package sh.miles.aoc.utils

class ResultUnion(private vararg val results: Any) {

    override fun toString(): String {
        val builder = StringBuilder();
        for ((index, value) in results.withIndex()) {
            builder.append("\nPart ").append(index + 1).append(": ").append(value);
        }
        return builder.toString();
    }
}
