package adventofcode.y2024

import adventofcode.utils.readInput
import adventofcode.utils.split

fun main() {
    val input = readInput("2024/day19/input.txt").split { it.isBlank() }
    val towels = input.first().first().split(", ").sortedByDescending { it.length }
    val combinations = input.last()
    val part1 = combinations.count {
        combinationIsPossible(towels, it)
    }
    println("Part1: $part1")

    val part2 = combinations.sumOf {
        countAllCombinations(towels, it)
    }
    println("Part2: $part2")
}

fun combinationIsPossible(towels: List<String>, combination: String): Boolean =
    if (combination.isBlank()) {
        true
    } else {
        towels.any {
            if (combination.startsWith(it)) {
                combinationIsPossible(towels, combination.removePrefix(it))
            } else {
                false
            }
        }
    }

private val cache = hashMapOf<String, Long>()
fun countAllCombinations(unlimitedStripes: List<String>, combination: String): Long =
    if (combination.isBlank()) {
        1
    } else {
        cache.getOrPut(combination) {
            unlimitedStripes.sumOf {
                if (combination.startsWith(it)) {
                    countAllCombinations(unlimitedStripes, combination.removePrefix(it))
                } else {
                    0
                }
            }
        }
    }









