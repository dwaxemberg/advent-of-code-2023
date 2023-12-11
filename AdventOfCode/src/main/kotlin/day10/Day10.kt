package day10

import day10.Direction.EAST
import day10.Direction.NORTH
import day10.Direction.SOUTH
import day10.Direction.WEST
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import utils.InputReader.readFileAsList
import utils.mapAsync
import java.util.*

fun main() {
    val input = readFileAsList("Example.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val pipes = parseInput(input)
    val startCoord = findStart(pipes)

    val loops = findLoops(pipes, startCoord)
    println("number of loops: ${loops.size} loops: $loops")
    return (loops.maxOf { it.size } + 1) / 2
}

fun part2(input: List<String>): Int {
    val pipes = parseInput(input)
    val startCoord = findStart(pipes)

    val maxLoop = findLoops(pipes, startCoord).maxBy { it.size }.toMutableList()
    maxLoop.add(0, Triple(startCoord.row, startCoord.column, 'S'))
    println("maxLoop: $maxLoop")
    val minRow = maxLoop.minOf { it.first }
    val maxRow = maxLoop.maxOf { it.first }
    val minColumn = maxLoop.minOf { it.second }
    val maxColumn = maxLoop.maxOf { it.second }
    var inside = 0
    for (row in minRow..maxRow) {
        for (column in minColumn..maxColumn) {
            val symbol = pipes[row][column]
            if (Triple(row, column, symbol) !in maxLoop) {
                println("Not in loop: ${Triple(row, column, symbol)}")
                var numWalls = 0
                for (r in row - 1 downTo 0) {
                    println("Checking: $r,$column")
                    if (Triple(r, column, pipes[r][column]) in maxLoop) {
                        println("In loop: ${Triple(r, column, pipes[r][column])}")
                        numWalls++
                    }
                }
                println("Num walls above: $numWalls")
                if (numWalls != 0 && numWalls % 2 != 0) {
                    numWalls = 0
                    for (r in row + 1..<pipes.size) {
                        println("Checking: $r,$column")
                        if (Triple(r, column, pipes[r][column]) in maxLoop) {
                            println("In loop: ${Triple(r, column, pipes[r][column])}")
                            numWalls++
                        }
                    }
                    println("Num walls below: $numWalls")
                    if (numWalls != 0 && numWalls % 2 == pipes.size % 2) {
                        println("inside loop!")
                        inside++
                    }
                }

            }
        }
    }

    return inside
}

private fun findLoops(pipes: List<List<Char>>, startCoord: Coord): List<List<Triple<Int, Int, Char>>> {
    val directions = findPotentialStartDirections(pipes, startCoord)
    return runBlocking {
        withContext(Dispatchers.Default) {
            directions.mapAsync {
                getLoop(pipes, startCoord.copy(), it)
            }.filterNotNull()
        }
    }
}

private fun parseInput(input: List<String>): List<List<Char>> {
    return input.map { it.toList() }
}

private fun findStart(pipes: List<List<Char>>): Coord {
    pipes.forEachIndexed { row, rowChars ->
        rowChars.forEachIndexed { column, symbol ->
            if (symbol == 'S') return Coord(row, column)
        }
    }
    throw IllegalArgumentException("Pipes don't contain a start node! \n$pipes")
}

private fun findPotentialStartDirections(pipes: List<List<Char>>, start: Coord): List<Direction> {
    val directions = mutableListOf<Direction>()
    val (row, column) = start
    // NORTH
    if (row > 0 && pipes[row - 1][column] in listOf('|', '7', 'F')) directions.add(NORTH)
    // SOUTH
    if (row < pipes.size - 1 && pipes[row + 1][column] in listOf('|', 'J', 'L')) directions.add(SOUTH)
    // EAST
    if (column < pipes[row].size - 1 && pipes[row][column + 1] in listOf('-', 'J', '7')) directions.add(EAST)
    // WEST
    if (column > 0 && pipes[row][column - 1] in listOf('-', 'F', 'L')) directions.add(WEST)
    return directions
}

private fun getLoop(pipes: List<List<Char>>, coord: Coord, direction: Direction): List<Triple<Int, Int, Char>>? {
    val potentialLoop = LinkedList<Triple<Int, Int, Char>>()
    var previous = coord.copy()
    when (direction) {
        NORTH -> coord.row -= 1
        SOUTH -> coord.row += 1
        EAST -> coord.column += 1
        WEST -> coord.column -= 1
    }
    while (coord within pipes && pipes[coord.row][coord.column] != 'S') {
        val symbol = pipes[coord.row][coord.column]
        potentialLoop.add(Triple(coord.row, coord.column, symbol))
        if (symbol == '.') return null
        previous = coord.next(previous, symbol)
    }
    return if (pipes[coord.row][coord.column] == 'S') potentialLoop else null
}

private data class Coord(var row: Int, var column: Int) {

    infix fun within(pipes: List<List<*>>): Boolean {
        return (row >= 0 && row < pipes.size && column >= 0 && column < pipes[0].size)
    }

    fun next(previous: Coord, symbol: Char): Coord {
        val newPrevious = copy()
        when (symbol) {
            /**
             * | is a vertical pipe connecting north and south.
             * - is a horizontal pipe connecting east and west.
             * L is a 90-degree bend connecting north and east.
             * J is a 90-degree bend connecting north and west.
             * 7 is a 90-degree bend connecting south and west.
             * F is a 90-degree bend connecting south and east.
             * . is ground; there is no pipe in this tile.
             * S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
             */
            '|' -> if (previous.row > row) row-- else row++
            '-' -> if (previous.column > column) column-- else column++
            'L' -> if (previous.row < row) column++ else row--
            'J' -> if (previous.row < row) column-- else row--
            '7' -> if (previous.row > row) column-- else row++
            'F' -> if (previous.row > row) column++ else row++
            else -> throw IllegalArgumentException("Don't know what to do with: $symbol")
        }
        return newPrevious
    }
}

private enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST
}
