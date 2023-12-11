package day11

import utils.InputReader.readFileAsList
import kotlin.math.abs

fun main() {
    val input = readFileAsList("Day11.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Long {
    val space = input.map { it.toList() }
    val expansionFactor = 2
    return calculateDistances(space, expansionFactor)
}

fun part2(input: List<String>): Long {
    val space = input.map { it.toList() }
    val expansionFactor = 1_000_000
    return calculateDistances(space, expansionFactor)
}

private fun calculateDistances(space: List<List<Char>>, expansionFactor: Int): Long {
    println("Space:")
    space.forEach { println(it) }
    println("-----")
    val galaxies = findAllGalaxies(space)
    println("Galaxies: $galaxies")
    val (emptyRows, emptyColumns) = findEmpties(space)
    var distanceSum = 0L
    for (galaxy1 in galaxies.indices) {
        for (galaxy2 in galaxy1 + 1..<galaxies.size) {
            val (row1, column1) = galaxies[galaxy1]
            val (row2, column2) = galaxies[galaxy2]
            val distance = abs(row1 - row2) + abs(column1 - column2)
            val emptyRowsCrossed = emptyRows.sumOf {
                if (it in (row1 + 1)..<row2 || it in (row2 + 1)..<row1) 1 else 0L
            }
            val emptyColumnsCrossed = emptyColumns.sumOf {
                if (it in (column1 + 1)..<column2 || it in (column2 + 1)..<column1) 1 else 0L
            }
            distanceSum += distance + ((expansionFactor - 1) * (emptyRowsCrossed + emptyColumnsCrossed))
        }
    }
    return distanceSum
}

private fun findAllGalaxies(space: List<List<Char>>): List<Pair<Int, Int>> {
    val galaxies = mutableListOf<Pair<Int, Int>>()
     space.forEachIndexed { row, rowChars ->
        rowChars.forEachIndexed { column, symbol ->
            if (symbol == '#') {
                galaxies.add(row to column)
            }
        }
    }
    return galaxies
}

private fun findEmpties(space: List<List<Char>>): Pair<List<Int>, List<Int>> {
    val emptyRows = mutableListOf<Int>()
    // Find all empty rows
    space.forEachIndexed { row, rowChars ->
        if (rowChars.all { it == '.' }) {
            emptyRows.add(row)
        }
    }
    val emptyColumns = mutableListOf<Int>()
    // Find all empty columns
    for (column in space[0].size - 1 downTo 0) {
        if (space.count { it[column] == '.' } == space.size) {
            emptyColumns.add(column)
        }
    }
    return emptyRows to emptyColumns
}
