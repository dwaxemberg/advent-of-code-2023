package day8

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import utils.InputReader.readFileAsList
import utils.lcm
import utils.mapAsync

fun main() {
    val input = readFileAsList("Day8.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val instructionList = input[0]
    val map = parseGraph(input.drop(1))
    var currentNode = "AAA"
    var steps = 0
    while (currentNode != "ZZZ") {
        currentNode =
            if (instructionList[steps % instructionList.length] == 'L') map[currentNode]!!.first else map[currentNode]!!.second
        steps++
    }
    return steps
}

fun part2(input: List<String>): Long {
    val instructionList = input[0]
    val map = parseGraph(input.drop(1))
    val currentNodes = map.keys.filter { it.endsWith("A") }.toMutableList()
    println("Starting Nodes: $currentNodes")

    val steps = runBlocking {
        withContext(Dispatchers.Default) {
            currentNodes.mapAsync {
                println("Starting Node: $it")
                var steps = 0L
                var node = it
                while (!node.endsWith("Z")) {
                    node =
                        if (instructionList[(steps % instructionList.length).toInt()] == 'L') map[node]!!.first else map[node]!!.second
                    steps++
                }
                steps
            }
        }
    }
    println("Steps: $steps")
    return steps.lcm()
}

private fun parseGraph(input: List<String>): Map<String, Pair<String, String>> {
    return input.associate {
        val (nodeLabel, possibleNodes) = it.split(" = ")
        val left = possibleNodes.drop(1).substringBefore(',')
        val right = possibleNodes.substringAfter(", ").dropLast(1)
        nodeLabel to (left to right)
    }
}
