package adventofcode.y2024

import adventofcode.utils.println
import adventofcode.utils.readInput
import kotlin.time.measureTime

fun main() {
    val input = readInput("2024/day11/input.txt").first().split(" ").map { it.toLong() }
    measureTime {
        val part1 = input.sumOf { count(it, 25) }
        println("Part 1: $part1")
    }.println()

    measureTime {
        val part2 = input.sumOf { count(it, 75) }
        println("Part 2: $part2")
    }.println()
}

private val cache = hashMapOf<Pair<Long, Int>, Long>()
private fun count(stone: Long, blinks: Int): Long {
    if (blinks == 0) {
        return 1
    }
    return cache.getOrPut(stone to blinks) {
        if (stone == 0L) {
            count(1L, blinks - 1)
        } else if (stone.toString().length % 2 == 0) {
            count(stone.toString().take(stone.toString().length / 2).toLong(), blinks - 1) +
                count(stone.toString().takeLast(stone.toString().length / 2).toLong(), blinks - 1)
        } else {
            count(stone * 2024, blinks - 1)
        }
    }
}