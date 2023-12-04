package day4

import utils.InputReader.readFileAsList
import kotlin.math.pow

fun main() {
    val input = readFileAsList("Day4.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return parseCards(input).sumOf { (_, myNumbers, winningNumbers) ->
        val numMatching = myNumbers.filter { it in winningNumbers }.size
        if (numMatching > 0) 2.0.pow(numMatching - 1.0).toInt() else 0
    }
}

fun part2(input: List<String>): Int {
    val listOfCards = parseCards(input).map { it to 1 }.toMutableList()
    listOfCards.forEachIndexed { index, cardPair ->
        val (_, myNumbers, winningNumbers) = cardPair.first
        val cardsToCopy = myNumbers.filter { it in winningNumbers }.size
        val boundedCopies = minOf(listOfCards.size - 1, index + cardsToCopy)
        for (toCopy in index + 1..boundedCopies) {
            listOfCards[toCopy] = listOfCards[toCopy].first to listOfCards[toCopy].second + cardPair.second
        }
    }
    return listOfCards.sumOf { it.second }
}

private fun parseCards(input: List<String>): List<Card> {
    return input.map { card ->
        val (cardInfo, cardValues) = card.split(":")
        val cardNumber = cardInfo.substringAfter(" ").trim().toInt()
        val (myNumbers, winningNumbers) = cardValues.split("|")
        val myNumbersList = myNumbers.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
        val winningNumbersList = winningNumbers.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
        Card(cardNumber, myNumbersList, winningNumbersList)
    }
}

data class Card(val id: Int, val myNumbers: List<Int>, val winningNumbers: List<Int>)
