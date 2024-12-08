package adventofcode.y2024

import adventofcode.utils.plus
import adventofcode.utils.readInput
import kotlin.math.abs


fun main() {
    val input: List<String> = readInput("2024/day1/day1.txt")

    val (numbers1, numbers2) = input
        .map { it.split("   ") }
        .map { it.first().toInt() to it.last().toInt() }
        .fold(mutableListOf<Int>() to mutableListOf<Int>()) { (accn1, accn2), (n1, n2) ->
            accn1 + n1 to accn2 + n2
        }

    val sortedNumbers1 = numbers1.sorted()
    val sortedNumbers2 = numbers2.sorted()

    val part1 = sortedNumbers1
        .foldIndexed(0) { index: Int, acc: Int, current: Int -> acc + abs((current - sortedNumbers2[index])) }
    println("Part1: $part1")

    val part2 = sortedNumbers1
        .fold(0L) { acc: Long, n1: Int -> acc + n1 * sortedNumbers2.count { n2 -> n1 == n2 } }
    println("Part2: $part2")
}