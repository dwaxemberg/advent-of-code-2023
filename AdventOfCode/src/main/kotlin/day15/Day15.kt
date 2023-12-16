package day15

import utils.InputReader.readFileAsString

fun main() {
    val input = readFileAsString("Day15.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: String): Int {
    return input.split(',').sumOf { hash(it) }
}

fun part2(input: String): Int {
    val boxes = mutableMapOf<Int, MutableList<Lens>>()
    val stepPattern = "(\\w+)([=\\-])(\\d*)".toRegex()
    input.split(',').forEach { initStep ->
        val matches = stepPattern.find(initStep)
        val (_, label, operation, focal) = matches?.groupValues
            ?: throw IllegalArgumentException("Didn't find a match: $initStep")

        val boxNum = hash(label)
        val box = boxes.getOrDefault(boxNum, mutableListOf())
        when (operation) {
            "=" -> {
                box.find { (lbl, _) -> lbl == label }?.let {
                    it.focalLength = focal.toInt()
                } ?: box.add(Lens(label, focal.toInt()))
            }

            "-" -> {
                box.removeAll { (lbl, _) -> lbl == label }
            }
        }
        boxes[boxNum] = box
    }
    println(boxes)
    var sum = 0
    boxes.forEach { (boxNum, box) ->
        if (box.isNotEmpty()) {
            sum += box.mapIndexed { i, (_, focalLength) ->
                (1 + boxNum) * (i + 1) * focalLength
            }.sum()
        }
    }
    return sum
}

private fun hash(string: String): Int {
    var current = 0
    string.forEach {
        current += it.code
        current *= 17
        current %= 256
    }
    return current
}

data class Lens(val label: String, var focalLength: Int)
