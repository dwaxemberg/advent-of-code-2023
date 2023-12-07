package day7

import utils.InputReader.readFileAsList

typealias Card = Int
typealias Bid = Int

fun main() {
    val input = readFileAsList("Day7.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val camelHands = parseInput(input)
    return camelHands.sortedBy { it.first }.also { println("Sorted: \t$it") }
        .mapIndexed { i, (_, bid) -> (i + 1) * bid }.sum()
}

fun part2(input: List<String>): Int {
    val camelHands = parseInput(input).map { (hand, bid) ->
        Hand(hand.cards.map { if (it == 11) 0 else it }) to bid // Replace all J with 0 (wild)
    }
    return camelHands.sortedBy { it.first }.also { println("Sorted: \t$it") }
        .mapIndexed { i, (_, bid) -> (i + 1) * bid }.sum()
}

private fun parseInput(input: List<String>): List<Pair<Hand, Bid>> {
    return input.map {
        val (cardString, bid) = it.split(" ")
        val cardNums = cardString.map { card ->
            if (card.isDigit()) card.digitToInt()
            else
                when (card) {
                    'T' -> 10
                    'J' -> 11
                    'Q' -> 12
                    'K' -> 13
                    'A' -> 14
                    else -> throw IllegalArgumentException("Unknown card: $card")
                }
        }
        Hand(cardNums) to bid.toInt()
    }
}

data class Hand(val cards: List<Int>) : Comparable<Hand> {
    val rank by lazy {
        val cardCounts = mutableMapOf<Card, Int>()
        cards.sortedDescending().forEach {
            if (it == 0) {
                val key = if (cardCounts.isEmpty()) 0 else cardCounts.maxBy { it.value }.key // Find the max and increment it.
                cardCounts[key] = cardCounts.getOrDefault(key, 0) + 1
            } else {
                cardCounts[it] = cardCounts.getOrDefault(it, 0) + 1
            }
        }
        when (cardCounts.size) {
            5 -> 1 // All different
            4 -> 2 // One pair
            3 -> 1 + cardCounts.maxOf { it.value } // 2 pair or 3 of a kind yields 3 or 4 rank
            2 -> 2 + cardCounts.maxOf { it.value } // Full house or 4 of a kind yields 5 or 6
            1 -> 7 // 5 of a kind
            else -> throw IllegalStateException("Too many cards: $cards, $cardCounts")
        }
    }

    override fun compareTo(other: Hand): Int {
        if (rank > other.rank) {
            return 1
        } else if (rank < other.rank) {
            return -1
        } else {
            for (i in cards.indices) {
                if (cards[i] > other.cards[i]) return 1
                else if (cards[i] < other.cards[i]) return -1
            }
        }
        println("Same hands: $cards, ${other.cards}")
        return 0
    }
}
