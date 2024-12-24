package adventofcode.y2024

import adventofcode.utils.plus
import adventofcode.utils.readInput

fun main() {
    val input = readInput("2024/day22/input.txt").map { it.toLong() }
    val allSecretNumbers = input.map { calculateSecretNumber(mutableListOf(it), 2000) }
    val part1 = allSecretNumbers.sumOf { it.last() }
    println("Part1: $part1")

    val prizes = allSecretNumbers.map { it.map { it.toString()[it.toString().length - 1].digitToInt() } }
    val sequencesWithPrice = prizes.map { it.windowed(5).map { it.windowed(2).map { it.last() - it.first() } to it.last() } }
    val mapOfSequencesWithPrizes = mutableMapOf<List<Int>, Int>()
    sequencesWithPrice.forEach {
        val doneSequences = mutableSetOf<List<Int>>()
        it.forEach {
            if (it.first !in doneSequences) {
                mapOfSequencesWithPrizes[it.first] = mapOfSequencesWithPrizes.getOrElse(it.first) { 0 } + it.second
                doneSequences.add(it.first)
            }
        }
    }
    println(mapOfSequencesWithPrizes.keys.size)
    val part2 = mapOfSequencesWithPrizes.maxBy { it.value }.value
    println("Part2: $part2")
}

private fun calculateSecretNumber(currentNumbers: MutableList<Long>, depth: Int): MutableList<Long> =
    if (depth == 0) {
        currentNumbers
    } else {
        calculateSecretNumber(currentNumbers.plus(currentNumbers.last().multiply(64).divide(32).multiply(2048)), depth - 1)
    }

private fun Long.divide(times: Long): Long =
    (this / times).mix(this).prune()

private fun Long.multiply(times: Long): Long =
    (this * times).mix(this).prune()

private fun Long.mix(currentNumber: Long): Long =
    this xor currentNumber

private fun Long.prune(): Long =
    this % 16777216