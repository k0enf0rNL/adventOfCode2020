package adventofcode.y2024

import adventofcode.utils.readInput
import kotlin.math.abs

enum class Result {
    ASCENDING,
    DESCENDING,
    UNSAFE
}

fun main() {
    val input: List<String> = readInput("2024/day2.txt")
    val numbers = input.map { it.split(" ").map(String::toInt) }
    val resultList = checkSafeOrUnsafeList(numbers)
    val part1 = resultList.countSafe()
    println("Part1: $part1")

    val onlyUnsafeList = resultList.filter { !it.second.all { it == Result.ASCENDING } && !it.second.all { it == Result.DESCENDING } }
    val safeUnsafeList = onlyUnsafeList.map { onlyUnsafe ->
        onlyUnsafe.first.indices.map { numberIndexToRemove -> onlyUnsafe.first.filterIndexed { index, _ -> index != numberIndexToRemove } }
    }.map { checkSafeOrUnsafeList(it).countSafe() > 0 }

    val part2 = part1 + safeUnsafeList.count { it }
    println("Part2: $part2")
}

private fun List<Pair<List<Int>, List<Result>>>.countSafe() =
    count { it.second.all { it == Result.ASCENDING } || it.second.all { it == Result.DESCENDING } }

private fun checkSafeOrUnsafeList(numbers: List<List<Int>>) = numbers.map {
    it to
        it.windowed(2)
            .map {
                if (it.first() > it.last() && abs(it.first() - it.last()) <= 3) {
                    Result.DESCENDING
                } else if (it.first() < it.last() && abs(it.first() - it.last()) <= 3) {
                    Result.ASCENDING
                } else {
                    Result.UNSAFE
                }
            }
}