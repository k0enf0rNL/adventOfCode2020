package adventofcode.y2024

import adventofcode.utils.Point
import adventofcode.utils.PointWithInt
import adventofcode.utils.findAllPathsByPredicate
import adventofcode.utils.getNeighbours
import adventofcode.utils.readInput

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
    val paths = startingPoints.map {
        findAllPathsByPredicate(
            it,
            { it.value == 9 },
            { pointWithInt -> input.getNeighbours(pointWithInt.point).filter { pointWithInt.value + 1 == it.value } }
        )
    }
    val part1 = paths.map { it.mapNotNull { it.end }.toSet() }.sumOf { it.size }
    println("Part 1: $part1")

    val part2 = paths.map { it.mapNotNull { it.end } }.sumOf { it.size }
    println("Part 2: $part2")
}