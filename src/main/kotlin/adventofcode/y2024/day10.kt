package adventofcode.y2024

import adventofcode.utils.Point
import adventofcode.utils.PointWithInt
import adventofcode.utils.findAllPathsByPredicate
import adventofcode.utils.getNeighbours
import adventofcode.utils.println
import adventofcode.utils.readInput
import kotlin.time.measureTime

fun main() {
    val input = readInput("2024/day10/input.txt").mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, c ->
            PointWithInt(
                Point(
                    rowIndex.toLong(),
                    columnIndex.toLong()
                ),
                c.digitToInt()
            )
        }
    }
    val startingPoints = input.flatMap { it.filter { it.value == 0 } }

    measureTime {
        val paths = startingPoints.map {
            findAllPathsByPredicate(
                start = it,
                endFunction = { it.value == 9 },
                neighbours = { pointWithInt -> input.getNeighbours(pointWithInt.point).filter { pointWithInt.value + 1 == it.value } }
            )
        }
        val part1 = paths.map { it.mapNotNull { it.end }.toSet() }.sumOf { it.size }
        println("Part 1: $part1")

        val part2 = paths.map { it.mapNotNull { it.end } }.sumOf { it.size }
        println("Part 2: $part2")
    }.println()
}