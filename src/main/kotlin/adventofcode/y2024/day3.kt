package adventofcode.y2024

import adventofcode.utils.readInput

fun main() {
    val input: List<String> = readInput("2024/day3.txt")
    val multiplyNumbers = input.joinToString("").getMuls()
    val part1 = multiplyNumbers.sumOf { numberPair -> numberPair.second.first * numberPair.second.second }
    println("Part1: $part1")

    val multiplyNumbersPart2 = input.joinToString("").replace(Regex("don't\\(\\).*?(?=do\\(\\)|\$)"), "").getMuls()
    val part2 = multiplyNumbersPart2.sumOf { numberPair -> numberPair.second.first * numberPair.second.second }
    println("Part2: $part2")
}

private fun String.getMuls() = Regex("mul\\((\\d+,\\d+)\\)")
    .findAll(this)
    .map {
        it.range.first to it.groupValues.last().split(",").let { it.first().toLong() to it.last().toLong() }
    }