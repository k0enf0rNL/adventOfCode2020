package adventofcode.y2021

import adventofcode.utils.readInput

data class BingoCard(
    val numbers: List<List<Number>>
) {
    fun hasBingo(): Boolean =
        numbers.any { it.all { it.check } } || (0..4).map { index -> numbers.map { it[index] }.all { it.check } }.any { it }

    fun calculateScore() =
        numbers.sumOf { it.filter { !it.check }.sumOf { it.value } }
}

data class Number(
    val value: Int,
    var check: Boolean = false
)

fun main() {
    val input: MutableList<String> = readInput("2021/input.txt").toMutableList()

    val allNumbers = (0..100).map { Number(it) }

    fun Int.toNumber() =
        allNumbers.first { it.value == this@toNumber }

    fun List<String>.toNumbers(): List<List<Number>> =
        map {
            it.split("""\s+""".toRegex())
                .filter { it.isNotBlank() }
                .map { it.toInt().toNumber() }
        }

    fun List<String>.toBingoCard() =
        BingoCard(
            this.toNumbers()
        )

    fun List<List<String>>.toBingoCards() =
        map { it.toBingoCard() }

    val bingoNumbers = input[0].split(",").map { it.toInt().toNumber() }
    input.removeFirst()
    input.removeFirst()

    val bingoCards = input.filter { it.isNotBlank() }.chunked(5).toBingoCards()

// Part 1
    for (bingoNumber in bingoNumbers) {
        bingoNumber.check = true
        val winningCard = bingoCards.firstOrNull { it.hasBingo() }
        if (winningCard != null) {
            println("Part 1: ${winningCard.calculateScore() * bingoNumber.value}")
            break
        }
    }

// Part 2
    var losingCards = bingoCards
    for (bingoNumber in bingoNumbers) {
        bingoNumber.check = true
        val tempLosingCards = bingoCards.filter { !it.hasBingo() }
        if (losingCards.size == 1 && tempLosingCards.isEmpty()) {
            println("Part 2: ${losingCards[0].calculateScore() * bingoNumber.value}")
            break
        }
        losingCards = tempLosingCards
    }
}
