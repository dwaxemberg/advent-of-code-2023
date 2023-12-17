package day16

import day16.Direction.EAST
import day16.Direction.NORTH
import day16.Direction.SOUTH
import day16.Direction.WEST
import utils.InputReader.readFileAsList
import utils.print

fun main() {
    val input = readFileAsList("Day16.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val mirrors = input.map { it.toList() }.toList()
    return energize(mirrors, Laser(0, 0, EAST))
}

fun part2(input: List<String>): Int {
    val mirrors = input.map { it.toList() }.toList()
    val startLasers = mutableListOf<Laser>()
    startLasers.addAll(mirrors.indices.map { Laser(it, 0, EAST) })
    startLasers.addAll(mirrors.indices.map { Laser(it, mirrors[it].size - 1, WEST) })
    startLasers.addAll(mirrors[0].indices.map { Laser(0, it, SOUTH) })
    startLasers.addAll(mirrors[0].indices.map { Laser(mirrors.size - 1, it, NORTH) })
    println("Starts: $startLasers")
    return startLasers.maxOf { energize(mirrors, it) }
}

private fun energize(mirrors: List<List<Char>>, start: Laser): Int {
    val lasers = mutableListOf(start)
    val visited = mutableSetOf<Laser>()

    fun checkLaser(laser: Laser): Boolean {
        return laser.row >= 0
                && laser.row < mirrors.size
                && laser.column >= 0
                && laser.column < mirrors[0].size
                && laser !in visited
    }

    while (lasers.isNotEmpty()) {
        val laser = lasers.first()
        visited.add(laser.copy())
        val newLaser = propagateLaser(mirrors, laser)
        newLaser?.let { if (checkLaser(it)) lasers.add(it) }
        if (!checkLaser(laser)) lasers.remove(laser)
    }
    mirrors.print()
    println("-----")
    val mutableMirrors = mirrors.map { it.toMutableList() }.toMutableList()
    visited.forEach { laser ->
        mutableMirrors[laser.row][laser.column] = '#'
    }
    mutableMirrors.print()
    return visited.distinctBy { (row, column, _) -> row to column }.size
}

private fun propagateLaser(mirrors: List<List<Char>>, laser: Laser): Laser? {
    fun moveForward(laser: Laser) {
        when (laser.direction) {
            NORTH -> laser.row--
            SOUTH -> laser.row++
            EAST -> laser.column++
            WEST -> laser.column--
        }
    }
    return when (mirrors[laser.row][laser.column]) {
        '.' -> {
            moveForward(laser)
            null
        }

        '/' -> {
            laser.direction = when (laser.direction) {
                NORTH -> EAST
                SOUTH -> WEST
                EAST -> NORTH
                WEST -> SOUTH
            }
            moveForward(laser)
            null
        }

        '\\' -> {
            laser.direction = when (laser.direction) {
                NORTH -> WEST
                SOUTH -> EAST
                EAST -> SOUTH
                WEST -> NORTH
            }
            moveForward(laser)
            null
        }

        '-' -> {
            if (laser.direction in listOf(EAST, WEST)) {
                moveForward(laser)
                null
            } else {
                val newLaser = laser.copy(direction = EAST)
                laser.direction = WEST
                moveForward(laser)
                moveForward(newLaser)
                newLaser
            }
        }

        '|' -> {
            if (laser.direction in listOf(NORTH, SOUTH)) {
                moveForward(laser)
                null
            } else {
                val newLaser = laser.copy(direction = SOUTH)
                laser.direction = NORTH
                moveForward(laser)
                moveForward(newLaser)
                newLaser
            }
        }

        else -> throw IllegalStateException("Unknown mirror: ${mirrors[laser.row][laser.column]}")
    }
}

data class Laser(var row: Int, var column: Int, var direction: Direction)

enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST
}
