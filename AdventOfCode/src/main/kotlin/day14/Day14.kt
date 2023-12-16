package day14

import utils.InputReader.readFileAsList
import utils.print

fun main() {
    val input = readFileAsList("Example.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val rockMap = input.map { it.toMutableList() }.toMutableList()
    println("Rock Map:")
    rockMap.print()
    println()
    rollNorth(rockMap)
    println("Roll North:")
    rockMap.print()
    println("Load:")
    return rockMap.mapIndexed { r, rockRow ->
        rockRow.sumOf {
            if (it == 'O') rockMap.size - r else 0
        }
    }.also { println(it) }.sum()
}

fun part2(input: List<String>): Int {
    val rockMap = input.map { it.toMutableList() }.toMutableList()
    println("Rock Map:")
    rockMap.print()
    println()
    val startTime = System.currentTimeMillis()
    println("Start time: $startTime")
    rollCycles(rockMap, 1_000_000_000)
    println("Load:")
    return rockMap.mapIndexed { r, rockRow ->
        rockRow.sumOf {
            if (it == 'O') rockMap.size - r else 0
        }
    }.also { println(it) }.sum().also {
        val stopTime = System.currentTimeMillis()
        println("Stop time: $stopTime")
        println("Duration: ${stopTime - startTime} ms")
    }
}

private fun rollCycles(input: MutableList<MutableList<Char>>, numCycles: Int) {
    val previousCycles = mutableSetOf<List<List<Char>>>()
    for (i in 0..<numCycles) {
        rollNorth(input)
        rollWest(input)
        rollSouth(input)
        rollEast(input)

        val cycle = input.map { it.toList() }.toList()
        if (cycle in previousCycles) {
            println("Encountered Loop on cycle: ${i + 1}, previous: ${previousCycles.size}")
            cycle.print()
            val cycleOffset = previousCycles.size - (numCycles % i)
            println("Cycling: $cycleOffset")
            for (j in 0..<cycleOffset) {
                rollNorth(input)
                rollWest(input)
                rollSouth(input)
                rollEast(input)
            }
            break
        }
        previousCycles.add(cycle)
    }
}

private fun rollSouth(input: MutableList<MutableList<Char>>) {
    for (i in input.size - 1 downTo 0) {
        for (j in i..<input.size - 1) {
            for (c in input[j].indices) {
                if (input[j][c] == 'O' && input[j + 1][c] == '.') {
                    var roll = j + 1
                    while (roll < input.size && input[roll][c] == '.') {
                        roll++
                    }
                    input[j][c] = '.'
                    input[roll - 1][c] = 'O'
                }
            }
        }
    }
}

private fun rollNorth(input: MutableList<MutableList<Char>>) {
    for (i in 1..<input.size) {
        for (j in i downTo 1) {
            for (c in input[j].indices) {
                if (input[j][c] == 'O' && input[j - 1][c] == '.') {
                    var roll = j - 1
                    while (roll >= 0 && input[roll][c] == '.') {
                        roll--
                    }
                    input[j][c] = '.'
                    input[roll + 1][c] = 'O'
                }
            }
        }
    }
}

private fun rollEast(input: MutableList<MutableList<Char>>) {
    for (i in input[0].size - 1 downTo 0) {
        for (j in i..<input[0].size - 1) {
            for (c in input.indices) {
                if (input[c][j] == 'O' && input[c][j + 1] == '.') {
                    var roll = j + 1
                    while (roll < input[0].size && input[c][roll] == '.') {
                        roll++
                    }
                    input[c][j] = '.'
                    input[c][roll - 1] = 'O'
                }
            }
        }
    }
}

private fun rollWest(input: MutableList<MutableList<Char>>) {
    for (i in 1..<input[0].size) {
        for (j in i downTo 1) {
            for (c in input.indices) {
                if (input[c][j] == 'O' && input[c][j - 1] == '.') {
                    var roll = j - 1
                    while (roll >= 0 && input[c][roll] == '.') {
                        roll--
                    }
                    input[c][j] = '.'
                    input[c][roll + 1] = 'O'
                }
            }
        }
    }
}
