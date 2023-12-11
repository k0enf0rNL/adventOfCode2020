package adventofcode.y2023

import adventofcode.utils.readInput
import kotlin.math.pow

fun main() {

    data class Card(val winningNumbers: List<Int>, val numbersScratched: List<Int>) {
        fun calculatePoints(): Int =
            when (val amountOfWinningNumbers = amountOfWinningNumbers()) {
                in (0..1) -> {
                    amountOfWinningNumbers
                }

                else -> {
                    2.0.pow(amountOfWinningNumbers - 1).toInt()
                }
            }

        fun amountOfWinningNumbers() = numbersScratched.count { winningNumbers.contains(it) }
    }

    fun getNumbersFromString(line: String): List<Int> =
        Regex("(\\d+)").findAll(line).map { it.value.toInt() }.toList()

    val input: List<String> = readInput("2023/day4.txt")

    val cards: List<Card> = input.map {
        val splitted = it.split(":").last().split("|")
        Card(
            getNumbersFromString(splitted.first()),
            getNumbersFromString(splitted.last())
        )
    }

    println(cards.sumOf { it.calculatePoints() })


    val amountOfCardsPerCard: MutableList<Int> = MutableList(cards.size) { 1 }

    println(cards.sumOf {
        (0..<it.amountOfWinningNumbers()).forEach { amountOfCardsPerCard[it + 1] = amountOfCardsPerCard[it + 1] + amountOfCardsPerCard.first() }
        amountOfCardsPerCard.removeFirst()
    })
}



