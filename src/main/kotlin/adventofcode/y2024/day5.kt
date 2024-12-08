package adventofcode.y2024

import adventofcode.utils.readInput
import adventofcode.utils.split

fun main() {
    val input = readInput("2024/day5/input.txt")
    val (rules, updates) = input.split { it.isBlank() }.let { it.first() to it.last() }
    val allowedRulesMap = rules.map { it.split("|").let { it.first().toInt() to it.last().toInt() } }.groupBy { it.first }.mapValues { it.value.map { it.second } }
    val updatesList = updates.map { it.split(",").map { it.toInt() } }

    val (correctUpdates, incorrectUpdates) = updatesList
        .map {
            val correctedUpdate = it.orderCorrectly(allowedRulesMap)
            (it == correctedUpdate) to correctedUpdate
        }
        .groupBy { it.first }.mapValues { it.value.map { it.second } }
        .let { it[true]!! to it[false]!! }

    val part1 = correctUpdates.sumOf { it[it.lastIndex/2] }
    println("Part1: $part1")
    val part2 = incorrectUpdates.sumOf { it[it.lastIndex/2] }
    println("Part2: $part2")
}

private fun List<Int>.orderCorrectly(allowedRulesMap: Map<Int, List<Int>>) =
    fold(mutableListOf<Int>()) { acc, value ->
        if (allowedRulesMap[value]?.any { it in acc } == true) {
            val shouldBeBefore = allowedRulesMap[value]!!.filter { it in acc }.minBy { acc.indexOf(it) }
            acc.apply { add(indexOf(shouldBeBefore), value) }
        } else {
            acc.apply { add(value) }
        }
    }
