package day2

import utils.InputReader

private const val NUM_RED = 12
private const val NUM_GREEN = 13
private const val NUM_BLUE = 14

fun main() {
    val input = InputReader.readFileAsList("Day2.txt").filter { it.isNotBlank() }

    val part1 = input.map(::parseGame).filter {
        it.red <= NUM_RED && it.green <= NUM_GREEN && it.blue <= NUM_BLUE
    }.sumOf { it.id }

    val part2 = input.map(::parseGame).sumOf { it.red * it.green * it.blue }

    println("Part 1: $part1, Part 2: $part2")
}

private fun parseGame(game: String): Game {
    println("Processing: $game")
    val (gameHeader, gameValues) = game.split(":")
    val gameId = gameHeader.dropWhile { !it.isDigit() }.toInt()
    val rounds = gameValues.split(";")
    println("\tRounds: $rounds, size: ${rounds.size}")
    var maxRed = 0
    var maxGreen = 0
    var maxBlue = 0
    rounds.forEach {
        "(\\d+) red".toRegex().find(it)?.groups?.get(1)?.value?.toInt()?.let { maxRed = maxOf(maxRed, it) }
        "(\\d+) green".toRegex().find(it)?.groups?.get(1)?.value?.toInt()?.let { maxGreen = maxOf(maxGreen, it) }
        "(\\d+) blue".toRegex().find(it)?.groups?.get(1)?.value?.toInt()?.let { maxBlue = maxOf(maxBlue, it) }
    }
    println("\tRed: $maxRed Green: $maxGreen Blue: $maxBlue")
    return Game(gameId, maxRed, maxGreen, maxBlue)
}

data class Game(val id: Int, val red: Int, val green: Int, val blue: Int)
