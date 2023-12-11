package adventofcode.y2021

import adventofcode.utils.readInput
import adventofcode.utils.transpose

fun main() {
    val input: List<String> = readInput("2021/day3.txt")

    fun List<Int>.getCommon(default: Int, comparator: (Map<Int, Int>) -> Map.Entry<Int, Int>) =
        groupingBy { it }
            .eachCount()
            .let {
                if (it[0] == it[1]) {
                    it.entries.first { it.key == default }
                } else {
                    comparator(it)
                }
            }.key

    fun List<Int>.fromBitsToInt(): Int =
        joinToString("") { it.toString() }.let { Integer.parseInt(it, 2) }


// Part 1
    val part1List = input.map { it.map { it.toString().toInt() } }
        .transpose()
        .map {
            it.groupingBy { it }.eachCount().maxByOrNull { it.value }!!.key
        }

    val part1XorList = part1List.map { it.xor(1) }

    val part1gamma = part1List.fromBitsToInt()

    val part1epsilon = part1XorList.fromBitsToInt()

    println("Part 1: ${part1gamma * part1epsilon}")


// Part 2
    val part2OxygenList = input.map { it.map { it.toString().toInt() } }.let { originalList ->
        var filteredList: List<List<Int>> = originalList
        for (i in 0 until originalList.maxOf { it.size }) {
            val mostCommonInPosition = filteredList.map { it[i] }.getCommon(1) { it.maxByOrNull { it.value }!! }
            filteredList = filteredList.filter { it[i] == mostCommonInPosition }
        }
        filteredList.first()
    }

    val part2ScrubberList = input.map { it.map { it.toString().toInt() } }.let { originalList ->
        var filteredList: List<List<Int>> = originalList
        for (i in 0 until originalList.maxOf { it.size }) {
            val leastCommonInPosition = filteredList.map { it[i] }.getCommon(0) { it.minByOrNull { it.value }!! }
            filteredList = filteredList.filter { it[i] == leastCommonInPosition }
        }
        filteredList.first()
    }

    val part2Oxygen = part2OxygenList.fromBitsToInt()
    val part2Scrubber = part2ScrubberList.fromBitsToInt()

    println("Part 2: ${part2Oxygen * part2Scrubber}")
}
