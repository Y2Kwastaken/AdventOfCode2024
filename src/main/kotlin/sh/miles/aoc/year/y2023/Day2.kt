package sh.miles.aoc.year.y2023

import sh.miles.aoc.utils.ResultUnion
import sh.miles.aoc.utils.timeFunction
import sh.miles.aoc.year.Day
import java.nio.file.Path
import kotlin.io.path.readLines
import kotlin.math.max

object Day2 : Day {
    private data class Round(val red: Int, val green: Int, val blue: Int) {
        companion object {
            fun fromNumbers(numbers: Map<String, Int>): Round {
                return Round(
                    numbers.getOrDefault("red", 0),
                    numbers.getOrDefault("green", 0),
                    numbers.getOrDefault("blue", 0)
                )
            }
        }

        fun withRed(red: Int): Round {
            return Round(red, this.green, this.blue)
        }

        fun withGreen(green: Int): Round {
            return Round(this.red, green, this.blue)
        }

        fun withBlue(blue: Int): Round {
            return Round(this.red, this.green, blue)
        }
    }

    private data class Game(val id: Int, val rounds: List<Round>)
    private data class GameRestrictions(val red: Int, val green: Int, val blue: Int) {
        fun test(game: Game): Boolean {
            return game.rounds.filter { this.red >= it.red && this.blue >= it.blue && this.green >= it.green }.size == game.rounds.size
        }
    }

    private val RULES = GameRestrictions(12, 13, 14)

    private var id: Int = 0

    override fun run(file: Path): ResultUnion {
        val games = file.readLines().map { line ->
            Game(++id, line.split(":")[1].split(";").map { round ->
                Round.fromNumbers(round.split(",").associate {
                    val split = it.split(" ")
                    Pair(split[2], split[1].toInt())
                })
            })
        }

        return ResultUnion(timeFunction { partOne(games) }, timeFunction { partTwo(games) })
    }

    private fun partOne(games: List<Game>): Int {
        return games.filter { RULES.test(it) }.sumOf { it.id }
    }

    private fun partTwo(games: List<Game>): Int {
        var power = 0
        for (game in games) {
            var bestRound = Round(0, 0, 0)
            for (round in game.rounds) {
                if (round.red > bestRound.red) {
                    bestRound = bestRound.withRed(round.red)
                }

                if (round.green > bestRound.green) {
                    bestRound = bestRound.withGreen(round.green)
                }

                if (round.blue > bestRound.blue) {
                    bestRound = bestRound.withBlue(round.blue)
                }
            }
            println(bestRound)
            power += (max(bestRound.red, 1) * max(bestRound.green, 1) * max(bestRound.blue, 1))
        }

        return power
    }
}
