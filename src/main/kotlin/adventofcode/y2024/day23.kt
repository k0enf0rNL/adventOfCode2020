package adventofcode.y2024

import adventofcode.utils.distinctPairs
import adventofcode.utils.readInput

fun main() {
    val input = readInput("2024/day23/input.txt")
    val pairs: List<Pair<String, String>> = input.map { it.split("-").let { it.first() to it.last() } }
    val connectedPairsPart1 = pairs.flatMap { pair ->
        val listOfConnectedToFirst: Set<String> = pairs.filter { it.first == pair.first || it.second == pair.first }.map { if (it.first == pair.first) it.second else it.first }.toSet()
        val listOfConnectedToSecond: Set<String> = pairs.filter { it.first == pair.second || it.second == pair.second }.map { if (it.first == pair.second) it.second else it.first }.toSet()
        listOfConnectedToFirst.intersect(listOfConnectedToSecond).map { listOf(it, pair.first, pair.second).sorted() }
    }.distinct()
    val part1 = connectedPairsPart1.filter { it.size == 3 }.filter { it.any { it.startsWith("t") } }.size
    println("Part1: $part1")

    val connectedPairsPart2 = pairs.map { pair ->
        val listOfConnectedToFirst: Set<String> = pairs.filter { it.first == pair.first || it.second == pair.first }.map { if (it.first == pair.first) it.second else it.first }.toSet()
        val listOfConnectedToSecond: Set<String> = pairs.filter { it.first == pair.second || it.second == pair.second }.map { if (it.first == pair.second) it.second else it.first }.toSet()
        listOfConnectedToFirst.intersect(listOfConnectedToSecond).plus(listOf(pair.first, pair.second)).sorted()
    }
    val part2 = connectedPairsPart2.groupBy { it }.filter { it.key.distinctPairs().toList().size == it.value.size }.maxBy { it.key.size }.key.joinToString(",")
    println("Part2: $part2")
}