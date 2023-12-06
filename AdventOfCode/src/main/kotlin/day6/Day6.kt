package day6

import utils.InputReader.readFileAsList
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

typealias RaceTime = Long
typealias RecordDistance = Long

fun main() {
    val input = readFileAsList("Day6.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Long {
    val times = input[0].substringAfter(": ").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    val records = input[1].substringAfter(": ").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    val races = times.mapIndexed { index, time -> time to records[index] }.toMap()
    println(races)
    return numberOfWaysToWin(races)
}

fun part2(input: List<String>): Long {
    val time = input[0].substringAfter(": ").replace(" ", "").toLong()
    val record = input[1].substringAfter(": ").replace(" ", "").toLong()
    val race = mapOf(time to record)
    println(race)
    return numberOfWaysToWin(race)
}

private fun numberOfWaysToWin(races: Map<RaceTime, RecordDistance>) = races.map { (time, distance) ->
    val t = time.toDouble()
    val d = distance.toDouble()
    // x = -((-time ± √(time^2 - 4*distance))/2)
    val x1 = -1 * ((-t + sqrt(t.pow(2) - 4 * d)) / 2)
    val x2 = -1 * ((-t - sqrt(t.pow(2) - 4 * d)) / 2)
    println("x1: $x1, x2: $x2")

    (ceil(x2 - 1) - floor(x1 + 1) + 1).toLong()
}.also { println(it) }.fold(1L) { acc, it -> acc * it }
