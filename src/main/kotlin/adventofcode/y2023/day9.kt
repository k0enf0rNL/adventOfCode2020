package adventofcode.y2023

import adventofcode.utils.readInput

fun main() {

    val input: List<String> = readInput("2023/day9.txt")

    val baseSequences: List<List<Int>> = input.map { it.split(" ").map { it.toInt() } }

    fun List<Int>.getDifferences(): List<Int> =
        mapIndexedNotNull { index, number ->
            if (index != size - 1) {
                get(index + 1) - number
            } else {
                null
            }
        }

    fun List<Int>.getListOfAllDifferences(): List<List<Int>> {
        val result: MutableList<List<Int>> = mutableListOf(this)
        while (!result.last().all { it == 0 }) {
            result.add(result.last().getDifferences())
        }
        return result
    }

    fun List<Int>.getNewLaterValueOfList(): Int {
        var difference = 0
        getListOfAllDifferences().reversed().forEach {
            difference += it.last()
        }
        return difference
    }

    println(baseSequences.asSequence().map { it.getNewLaterValueOfList() }.sumOf { it })

    println(baseSequences.asSequence().map { it.reversed().getNewLaterValueOfList() }.sumOf { it })
}

