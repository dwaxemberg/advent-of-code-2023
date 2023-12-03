package day3

import utils.InputReader.readFileAsList

fun main() {
    val input = readFileAsList("day3.txt")
    val part1 = part1(input)
    val part2 = part2(input)
    println("Part 1: $part1 Part 2: $part2")
}

private fun part1(input: List<String>): Int {
    var rollingSum = 0
    input.forEachIndexed { i, row ->
        row.forEachIndexed { j, column ->
            // Check if is symbol.
            if (!column.isDigit() && column != '.') {
                println("Found symbol at $j")
                if (i > 0) println("\t${input[i - 1]}")
                println("\t$row")
                if (i < input.size - 1) println("\t${input[i + 1]}")
                println("Sum of numbers at symbol: ${sumAdjacentNumbers(input, i, j)}")
                rollingSum += sumAdjacentNumbers(input, i, j)
            }
        }
    }
    return rollingSum
}

private fun part2(input: List<String>): Int {
    var rollingSum = 0
    input.forEachIndexed { i, row ->
        row.forEachIndexed { j, column ->
            // Check if is gear.
            if (column == '*') {
                println("Found symbol at $j")
                if (i > 0) println("\t${input[i - 1]}")
                println("\t$row")
                if (i < input.size - 1) println("\t${input[i + 1]}")
                println("Sum of numbers at symbol: ${multiplyAdjacentTwoNumbers(input, i, j)}")
                rollingSum += multiplyAdjacentTwoNumbers(input, i, j)
            }
        }
    }
    return rollingSum
}

private fun sumAdjacentNumbers(input: List<String>, row: Int, column: Int): Int {
    return adjacentNumbers(input, row, column).sum()
}

private fun multiplyAdjacentTwoNumbers(input: List<String>, row: Int, column: Int): Int {
    val adjacentNums = adjacentNumbers(input, row, column)
    return if (adjacentNums.size == 2) adjacentNums[0] * adjacentNums[1] else 0
}

private fun adjacentNumbers(
    input: List<String>,
    row: Int,
    column: Int
): List<Int> {
    fun expandDigit(line: Int, index: Int): Int {
        var startOfDigit = index
        var endOfDigit = index
        while (startOfDigit > 0 && input[line][startOfDigit - 1].isDigit()) startOfDigit--
        while (endOfDigit < input[line].length && input[line][endOfDigit].isDigit()) endOfDigit++
        return input[line].substring(startOfDigit, endOfDigit).toInt()
    }

    fun middleOut(line: Int, index: Int): List<Int> {
        val nums = mutableListOf<Int>()
        // Middle
        if (input[line][index].isDigit()) {
            nums += expandDigit(line, index)
        } else {
            if (index > 0 && input[line][index - 1].isDigit()) {
                // Check left
                nums += expandDigit(line, index - 1)
            }
            if (input[line].length > index + 1 && input[line][index + 1].isDigit()) {
                // Check right
                nums += expandDigit(line, index + 1)
            }
        }
        return nums
    }

    val adjacentNums = mutableListOf<Int>()
    // Check right.
    if (input[row].length > column + 1 && input[row][column + 1].isDigit()) {
        adjacentNums += expandDigit(row, column + 1)
    }
    // Check left.
    if (column > 0 && input[row][column - 1].isDigit()) {
        adjacentNums += expandDigit(row, column - 1)
    }
    // Check below.
    if (input.size > row + 1) {
        adjacentNums += middleOut(row + 1, column)
    }
    // Check above.
    if (row > 0) {
        adjacentNums += middleOut(row - 1, column)
    }
    return adjacentNums
}
