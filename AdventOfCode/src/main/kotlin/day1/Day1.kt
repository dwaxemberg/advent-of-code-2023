package day1

import utils.InputReader.readFileAsList

fun main() {
    val input = readFileAsList("Day1.txt").filter { it.isNotBlank() }
    val part1 = part1(input)
    val part2 = part2(input)
    println("Part1 result: $part1 Part2 result: $part2")
}

private fun part1(input: List<String>): Int {
    val calibrationSum = input.sumOf {
        println("Processing: $it")
        val digits = getDigits(it)
        println("Got value: $digits")
        digits
    }
    return calibrationSum
}

private fun part2(input: List<String>): Int {
    val calibrationSum = input.sumOf {
        println("Processing: $it")
        val digits = getDigitsWithWords(it)
        println("Got value: $digits")
        digits
    }
    return calibrationSum
}

/**
 * Gets the first and last digit in a string.
 */
private fun getDigits(text: String): Int {
    val firstDigit = text[text.indexOfFirst { it.isDigit() }]
    val secondDigit = text[text.indexOfLast { it.isDigit() }]
    println("Extracted: $firstDigit and $secondDigit")
    return "$firstDigit$secondDigit".toInt()
}

/**
 * Gets the first and last digit in a string including spelled out digits.
 */
private fun getDigitsWithWords(text: String): Int {
    val firstDigitIndex = text.indexOfFirst { it.isDigit() }
    val lastDigitIndex = text.indexOfLast { it.isDigit() }
    val mapOfFirstDigits = digits.associateWith { text.indexOf(it) }.filterValues { it != -1 }
    val mapOfLastDigits = digits.associateWith { text.lastIndexOf(it) }.filterValues { it != -1 }
    val firstWordIndex = mapOfFirstDigits.entries.minByOrNull { it.value }
    val lastWordIndex = mapOfLastDigits.entries.maxByOrNull { it.value }
    val firstDigit =
        if (firstWordIndex != null && firstWordIndex.value < firstDigitIndex) digits.indexOf(firstWordIndex.key) else text[firstDigitIndex]
    val secondDigit =
        if (lastWordIndex != null && lastWordIndex.value > lastDigitIndex) digits.indexOf(lastWordIndex.key) else text[lastDigitIndex]
    println("Extracted: $firstDigit and $secondDigit")
    return "$firstDigit$secondDigit".toInt()
}

val digits = arrayOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
