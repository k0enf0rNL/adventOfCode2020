package adventofcode.y2024

import adventofcode.utils.readInput
import adventofcode.utils.split
import adventofcode.utils.transpose

fun main() {
    val input = readInput("2024/day25/input.txt").split { it.isBlank() }.map { (if (it.first().all { it == '#' }) "lock" else "key") to it.map { it.toCharArray().toList() }.take(it.size-1).takeLast(it.size-2).transpose() }
    val keys = input.filter { it.first == "key" }.map { it.second }.map { it.map { it.filter { it == '#' }.size } }.sortedBy { it.joinToString("").toLong() }
    val locks = input.filter { it.first == "lock" }.map { it.second }.map { it.map { it.filter { it == '#' }.size } }.sortedByDescending { it.joinToString("").toLong() }
    val part1 = locks.sumOf { lock -> keys.count { key -> lock.mapIndexed { index, i -> i + key[index] }.all { it <= 5 } } }
    println("Part 1: $part1")
}