package adventofcode.y2024

import adventofcode.utils.Point
import adventofcode.utils.findShortestPathByPredicate
import adventofcode.utils.getNeighbours
import adventofcode.utils.readInput

fun main() {
    val obstacles = readInput("2024/day18/input.txt").map { it.split(",") }.map { Point(it.last().toLong(), it.first().toLong()) }.toSet()
    val part1Obstacles = obstacles.take(1024).toSet()
    val rowRange = 0..70
    val columnRange = 0..70
    val part1Path = findShortestPathByPredicate(
        start = Point(0, 0),
        endFunction = { it.rowIndex == rowRange.last.toLong() && it.columnIndex == columnRange.last.toLong() },
        neighbours = { it.getNeighbours().filter { it.rowIndex in rowRange && it.columnIndex in columnRange && !part1Obstacles.contains(it) } },
    )
    println("Part1: ${part1Path.getScore()}")

    for (i: Int in 1.. obstacles.size) {
        val part2Obstacles = obstacles.take(i).toSet()
        val part2Path = findShortestPathByPredicate(
            start = Point(0, 0),
            endFunction = { it.rowIndex == rowRange.last.toLong() && it.columnIndex == columnRange.last.toLong() },
            neighbours = { it.getNeighbours().filter { it.rowIndex in rowRange && it.columnIndex in columnRange && !part2Obstacles.contains(it) } },
        )
        if (part2Path.end == null) {
            println("Part 2: ${part2Obstacles.last().columnIndex},${part2Obstacles.last().rowIndex}")
            break
        }
    }
}