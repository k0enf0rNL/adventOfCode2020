package adventofcode.y2024

import adventofcode.utils.readInput
import java.math.BigInteger

fun main() {
    val input = readInput("2024/day7/input.txt")
    val calculations = input.map { it.split(": ").first().toBigInteger() to it.split(": ").last().split(" ").map { it.toBigInteger() } }
    val part1 = calculations.filter { calculate(it.second, false).contains(it.first) }.map { it.first }.sumOf { it }
    println("Part1: $part1")
    val part2 = calculations.filter { calculate(it.second, true).contains(it.first) }.map { it.first }.sumOf { it }
    println("Part2: $part2")
}

fun calculate(value: List<BigInteger>, combine: Boolean): List<BigInteger> =
    value.fold(listOf()) { acc, l ->
        if (acc.isEmpty()) {
            listOf(l)
        } else {
            acc.flatMap { nextValues(it, l, combine) }
        }
    }

private fun nextValues(it: BigInteger, l: BigInteger, combine: Boolean) =
    if (combine) {
        listOf(it * l, it + l, "$it$l".toBigInteger())
    } else {
        listOf(it * l, it + l)
    }
