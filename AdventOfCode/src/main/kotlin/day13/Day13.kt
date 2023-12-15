package day13

import utils.InputReader.readFileAsListWithBlanks

fun main() {
    val input = readFileAsListWithBlanks("Day13.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val sketches = parseSketches(input)

    return sketches.sumOf {
        findMirrors(it).also { println("Mirrors: $it") }
    }
}

fun part2(input: List<String>): Int {
    val sketches = parseSketches(input)

    return sketches.sumOf {
        findMirrors(it, withSmudges = true).also { println("Mirrors: $it") }
    }
}

fun parseSketches(input: List<String>): List<List<List<Char>>> {
    val sketches = splitOnBlanks(input).map { sketch -> sketch.map { it.toList() } }
    println("Number of sketches: ${sketches.size}")
    sketches.forEachIndexed { i, l ->
        println("Sketch ${i + 1}")
        l.forEach {
            it.forEach(::print)
            println()
        }
        println()
    }
    return sketches
}

fun splitOnBlanks(input: List<String>): List<List<String>> {
    val split = mutableListOf<List<String>>()
    input.fold(mutableListOf<String>()) { acc, s ->
        if (s.isNotBlank()) {
            acc.add(s)
            acc
        } else {
            split.add(acc)
            mutableListOf()
        }
    }
    return split.dropLastWhile { it.isEmpty() }
}

private fun findMirrors(lava: List<List<Char>>, withSmudges: Boolean = false): Int {
    potentialMirrors(lava, horizontal = true, withSmudges).also { println("potentials: $it")}.firstOrNull { isMirror(lava, it, horizontal = true, withSmudges) }?.let {
        // Add 100 multiplied by the number of rows above each horizontal line of reflection.
        return 100 * (it + 1)
    }
    potentialMirrors(lava, horizontal = false, withSmudges).firstOrNull { isMirror(lava, it, horizontal = false, withSmudges) }?.let {
        // Add up the number of columns to the left of each vertical line of reflection.
        return it + 1
    }
    lava.forEach {
        it.forEach(::print)
        println()
    }
    throw IllegalArgumentException("Could not find a mirror in $lava")
}

private fun potentialMirrors(lava: List<List<Char>>, horizontal: Boolean, withSmudges: Boolean): List<Int> {
    val potentials = mutableListOf<Int>()
    val outerBound = if (horizontal) lava.size else lava[0].size
    val innerBound = if (horizontal) lava[0].size else lava.size
    for (i in 1..<outerBound) {
        var errors = 0
        for (j in 0..<innerBound) {
            if ((horizontal && lava[i - 1][j] != lava[i][j])
                || (!horizontal && lava[j][i - 1] != lava[j][i])
            ) {
                errors++
                if (!withSmudges || (errors > 1)) {
                    break
                }
            }
        }
        if (withSmudges && errors <= 1 || errors == 0) potentials.add(i - 1)
    }
    return potentials
}

private fun isMirror(lava: List<List<Char>>, mirrorLine: Int, horizontal: Boolean, withSmudges: Boolean): Boolean {
    var first = mirrorLine
    var second = mirrorLine + 1
    val outerBound = if (horizontal) lava.size else lava[0].size
    val innerBound = if (horizontal) lava[0].size else lava.size

    var repairedSmudge = false
    while (first >= 0 && second < outerBound) {
        for (i in 0..<innerBound) {
            if ((horizontal && lava[first][i] != lava[second][i])
                || (!horizontal && lava[i][first] != lava[i][second])
            ) {
                if (withSmudges && !repairedSmudge) {
                    repairedSmudge = true
                } else {
                    return false
                }
            }
        }
        first--
        second++
    }
    return !withSmudges || repairedSmudge
}
