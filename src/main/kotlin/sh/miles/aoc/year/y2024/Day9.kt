package sh.miles.aoc.year.y2024

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.timeFunction
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines

object Day9 : Day {
    // 6241633730082
    // 1046248994
    override fun run(file: Path): ResultUnion {
        val line = file.readLines()[0].map { it.digitToInt() }
        val resultPartOne = timeFunction { partOne(line) }
        val resultPartTwo = timeFunction { partTwo(line) }

        return ResultUnion(resultPartOne, resultPartTwo)
    }

    private fun partOne(line: List<Int>): Long {
        val setup = mutableListOf<Int>()

        var id = 0
        for ((index, digit) in line.withIndex()) {
            if (index % 2 == 0) {
                for (num in 0 until digit) {
                    setup.add(id)
                }
                ++id
            } else {
                for (num in 0 until digit) {
                    setup.add(-1)
                }
            }
        }

        var cursor = 0
        var back = setup.size - 1

        while (cursor != setup.size) {
            if (setup[cursor] != -1) {
                ++cursor
                continue
            }

            setup[cursor] = setup[back]
            setup.removeLast()
            --back
        }

        var count = 0L
        for ((index, i) in setup.withIndex()) {
            count += (index * i)
        }

        return count
    }

    private data class AmphipodFile(val id: Int, val space: Int)

    private data class Space(val pos: Int, val size: Int)
    private data class File(val pos: Int, val size: Int, val id: Int)

    private fun partTwo(line: List<Int>): Long {
        val fileQueue = ArrayDeque<File>()
        val spaceQueue = ArrayDeque<Space>()
        val result = mutableListOf<Int?>()

        var pos = 0
        var fileId = 0
        line.withIndex().forEach { (index, char) ->
            if (index % 2 == 0) {
                fileQueue.add(File(pos, char, fileId))
                repeat((pos..<pos + char).count()) {
                    result.add(fileId)
                }
                pos += char
                fileId += 1
            } else {
                spaceQueue.add(Space(pos, char))
                result.addAll(List(char) { null })
                pos += char
            }
        }

        fileQueue.reversed().forEach { (pos, size, fileId) ->
            spaceQueue.withIndex().firstOrNull { (_, space) ->
                space.pos < pos && size <= space.size
            }?.let { (si, s) ->
                (0..<size).forEach { index ->
                    result[pos + index] = null
                    result[s.pos + index] = fileId
                }
                spaceQueue[si] = Space(s.pos + size, s.size - size)
            }
        }

        return result.withIndex().sumOf { (index, value) -> index.toLong() * (value ?: 0) }
    }

//    private fun partTwoF(line: List<Int>): Long {
//        val rawFiles = mutableListOf<SystemSpace>()
//        var id = 0
//
//        for ((index, digit) in line.withIndex()) {
//            if (index % 2 == 0) {
//                rawFiles.add(FileSpace(id, digit))
//                id++
//            } else {
//                if (digit != 0) {
//                    rawFiles.add(SystemSpace(digit))
//                }
//            }
//        }
//
//        val fileSystem = FileSystem(rawFiles, mutableSetOf())
//        var lastFileIndex: Int
//        while (fileSystem.findLastUnlockedFile().also { lastFileIndex = it } != -1) {
//            val file = fileSystem[lastFileIndex]
//            val openIndex = fileSystem.findFirstOpen(file.space)
//            if (openIndex == -1) {
//                fileSystem.lock(lastFileIndex)
//                continue
//            }
//
//            if (openIndex < lastFileIndex) {
//                fileSystem.moveFile(lastFileIndex, openIndex)
//            }
//            fileSystem.lock(lastFileIndex)
//        }
//
//        return fileSystem.system.asSequence().map { file ->
//            val list = mutableListOf<Int>()
//            if (file is FileSpace) {
//                repeat(file.space) { list.add(file.fileId) }
//            } else {
//                repeat(file.space) { list.add(-1) }
//            }
//
//            list
//        }.flatten().withIndex().filter { it.value != -1 }.sumOf { (index, num) -> num.toLong() * index.toLong() }
//    }
    // to high 6265513483391
    // is 6265268809555

    private class FileSystem(val system: MutableList<SystemSpace>, private val locked: MutableSet<Int>) {

        operator fun get(index: Int): SystemSpace {
            return system[index]
        }

        fun size(): Int {
            return system.size
        }

        fun moveFile(from: Int, to: Int): Boolean {
            if (isLocked(from) || isLocked(to)) return false
            val fromFile = system[from]
            if (fromFile !is FileSpace) return false
            val toSpace = system[to]
            if (toSpace is FileSpace) return false
            val spaceDifference = toSpace.space - fromFile.space

            if (spaceDifference == 0) {
                system[from] = toSpace
                system[to] = fromFile
                return true
            } else if (spaceDifference > 0) {
                system[from] = SystemSpace(fromFile.space)
                system[to] = fromFile
                system.add(to + 1, SystemSpace(spaceDifference))
                return true
            } else {
                return false
            }
        }

        fun findFirstOpen(minSize: Int): Int {
            for ((index, space) in system.withIndex()) {
                if (space !is FileSpace && space.space >= minSize) {
                    return index
                }
            }

            return -1
        }

        fun findLastFile(): Int {
            for (index in (0 until system.size).reversed()) {
                val space = system[index]
                if (space !is FileSpace) continue
                return index
            }

            throw IllegalArgumentException("No files found")
        }

        fun findLastUnlockedFile(): Int {
            for (index in (0 until system.size).reversed()) {
                val space = system[index]
                if (space !is FileSpace || isLocked(index)) continue
                return index
            }

            return -1
        }

        fun lock(index: Int) {
            locked.add(index)
        }

        fun isLocked(index: Int): Boolean {
            return locked.contains(index)
        }

        private fun joinWhitespaceAround(index: Int) {
            var count = system[index].space

            var minIndex = -1
            var cursor = index - 1
            while (cursor > 0 && system[cursor] !is FileSpace) {
                count += system[cursor].space
                minIndex = cursor--
            }

            cursor = index + 1
            var maxIndex = -1
            while (cursor < system.size && system[cursor] !is FileSpace) {
                count += system[cursor].space
                maxIndex = cursor++
            }

            if (maxIndex > system.size) {
                throw IllegalArgumentException("Wtf")
            }

            if (minIndex != -1 && maxIndex != -1) {
                system[minIndex] = SystemSpace(count)
                for (i in minIndex + 1 until maxIndex) {
                    system.removeAt(minIndex + 1)
                }
            } else if (minIndex != -1) {
                system[minIndex] = SystemSpace(count)
                for (i in minIndex + 1 until index) {
                    system.removeAt(minIndex + 1)
                }
            } else if (maxIndex != -1) {
                system[maxIndex] = SystemSpace(count)
                for (i in index until maxIndex - 1) {
                    system.removeAt(index)
                }
            }
        }

        private fun joinWhiteSpaces() {
            val toJoin: MutableList<SystemSpace> = mutableListOf()

            val iterator = system.listIterator()
            var next: SystemSpace? = null
            while (iterator.hasNext()) {
                next = iterator.next()
                if (next is FileSpace) {
                    if (toJoin.isNotEmpty()) {
                        iterator.previous()
                        iterator.add(SystemSpace(toJoin.sumOf { it.space }))
                    }
                    toJoin.clear()
                    continue
                }

                toJoin.add(next)
                iterator.remove()
            }

            if (next !is FileSpace) {
                system.add(next!!)
            }
        }

        override fun toString(): String {
            return this.system.joinToString("")
        }

        fun toSegmentedString(): String {
            val builder = StringBuilder()
            for (systemSpace in system) {
                builder.append(systemSpace.toSegmentedString())
            }
            return builder.toString()
        }
    }

    private open class SystemSpace(val space: Int) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is SystemSpace) return false
            if (space != other.space) return false
            return true
        }

        override fun hashCode(): Int {
            return space
        }

        override fun toString(): String {
            return ".".repeat(space)
        }

        open fun toSegmentedString(): String {
            return "[" + ".".repeat(space) + "]"
        }
    }

    private class FileSpace(val fileId: Int, space: Int) : SystemSpace(space) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is FileSpace) return false
            if (!super.equals(other)) return false

            if (fileId != other.fileId) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + fileId
            return result
        }

        override fun toString(): String {
            val builder = StringBuilder()
            if (fileId > 9) {
                builder.repeat("[$fileId]", space)
            } else {
                builder.repeat("${fileId.digitToChar()}", space)
            }
            return builder.toString()
        }

        override fun toSegmentedString(): String {
            val builder = StringBuilder().append("[")
            if (fileId > 9) {
                builder.repeat("[$fileId]", space)
            } else {
                builder.repeat("${fileId.digitToChar()}", space)
            }
            return builder.append("]").toString()
        }
    }
}
