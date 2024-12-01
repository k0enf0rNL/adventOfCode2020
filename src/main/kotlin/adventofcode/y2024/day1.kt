package adventofcode.y2024

import adventofcode.utils.readInput
import kotlin.math.abs

fun main() {
    val input: List<String> = readInput("2024/day1.txt")

    val numbers1 = input.map { it.split("   ").first().trim().toInt() }
    val numbers2 = input.map { it.split("   ").last().trim().toInt() }

    val sortedNumbers1 = numbers1.sorted()
    val sortedNumbers2 = numbers2.sorted()

    val part1 = sortedNumbers1
        .foldIndexed(0) { index: Int, acc: Int, current: Int -> acc + abs((current - sortedNumbers2[index])) }
    println("Part1: $part1")

    val part2 = sortedNumbers1
        .map { n1 -> n1 to sortedNumbers2.count { n2 -> n1 == n2 } }
        .map { n1WithCount: Pair<Int, Int> ->
            if (n1WithCount.second != 0) {
                n1WithCount.first.toLong() * n1WithCount.second
            } else 0L
        }.sum()
    println("Part2: $part2")
}