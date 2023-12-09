package day9

import utils.InputReader.readFileAsList

fun main() {
    val input = readFileAsList("Day9.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.sumOf {
        findNext(it.split(' ').map { it.toInt() })
    }
}

fun part2(input: List<String>): Int {
    return input.sumOf {
        findNext(it.split(' ').map { it.toInt() }, backwards = true)
    }
}

private fun findNext(sequence: List<Int>, backwards: Boolean = false): Int {
    val differences = ArrayList<Int>(sequence.size - 1)
    for (i in 1..<sequence.size) {
        differences.add(sequence[i] - sequence[i - 1])
    }
    return if (differences.distinct().size == 1) {
        if (backwards) {
            sequence.first() - differences[0]
        } else {
            sequence.last() + differences[0]
        }
    } else {
        if (backwards) {
            sequence.first() - findNext(differences, true)
        } else {
            sequence.last() + findNext(differences)
        }
    }
}
