package adventofcode.y2024

import adventofcode.utils.Direction
import adventofcode.utils.Point
import adventofcode.utils.PointInDirection
import adventofcode.utils.PointWithChar
import adventofcode.utils.findAllShortestPathsByPredicate
import adventofcode.utils.findShortestPathByPredicate
import adventofcode.utils.get90degreesNeighboursWithDirection
import adventofcode.utils.readInput

fun main() {
    val input = readInput("2024/day16/input.txt").mapIndexed { rowIndex, row -> row.mapIndexed { columnIndex, value -> PointWithChar(Point(rowIndex.toLong(), columnIndex.toLong()), value) } }
    val startPoint = PointInDirection(input.flatMap { it.filter { it.value == 'S' } }.first().point, Direction.RIGHT)
    val endPoint = input.flatMap { it.filter { it.value == 'E' } }.first().point
    val part1 = findShortestPathByPredicate(
        start = startPoint,
        endFunction = { it.point == endPoint },
        neighbours = { input.get90degreesNeighboursWithDirection(it.point, it.direction).filter { it.first != null && it.first!!.value != '#' }.map { PointInDirection(it.first!!.point, it.second) } },
        cost = { currentPoint, nextPoint -> if (currentPoint.direction != nextPoint.direction) { 1001 } else { 1 } }
    )
    println("Part1: ${part1.getScore()}")

    val allPaths = findAllShortestPathsByPredicate(
        start = startPoint,
        endFunction = { it.point == endPoint },
        neighbours = { input.get90degreesNeighboursWithDirection(it.point, it.direction).filter { it.first != null && it.first!!.value != '#' }.map { PointInDirection(it.first!!.point, it.second) } },
        cost = { currentPoint, nextPoint -> if (currentPoint.direction != nextPoint.direction) { 1001 } else { 1 } }
    )
    val tiles = allPaths.getPaths().flatMap { it.map { it.point } }.toSet()
    println("Part2: ${tiles.size}")
}