package adventofcode.y2024

import adventofcode.utils.readInput
import kotlin.math.abs

sealed class Result(val first: Int, val second: Int) {
    class Ascending(first: Int, second: Int) : Result(first, second)
    class Descending(first: Int, second: Int) : Result(first, second)
    class Unsafe(first: Int, second: Int) : Result(first, second)
}

fun main() {
    val input: List<String> = readInput("2024/day2.txt")
    val numbers = input.map { it.split(" ").map(String::toInt) }
    val resultList = checkSafeOrUnsafeList(numbers)
    val part1 = resultList.countSafe()
    println("Part1: $part1")

    val onlyUnsafeList = resultList.filter { !it.second.all { it is Result.Ascending } && !it.second.all { it is Result.Descending } }
    val safeUnsafeList = onlyUnsafeList.map { onlyUnsafe ->
        onlyUnsafe.first.indices.map { numberIndexToRemove -> onlyUnsafe.first.filterIndexed { index, _ -> index != numberIndexToRemove } }
    }.map { checkSafeOrUnsafeList(it).countSafe() > 0 }

    val part2 = part1 + safeUnsafeList.count { it }
    println("Part2: $part2")
}

private fun List<Pair<List<Int>, List<Result>>>.countSafe() =
    count { it.second.all { it is Result.Ascending } || it.second.all { it is Result.Descending } }

private fun checkSafeOrUnsafeList(numbers: List<List<Int>>) = numbers.map {
    it to
        it.windowed(2)
            .map {
                if (it.first() > it.last() && abs(it.first() - it.last()) <= 3) {
                    Result.Descending(it.first(), it.last())
                } else if (it.first() < it.last() && abs(it.first() - it.last()) <= 3) {
                    Result.Ascending(it.first(), it.last())
                } else {
                    Result.Unsafe(it.first(), it.last())
                }
            }
}