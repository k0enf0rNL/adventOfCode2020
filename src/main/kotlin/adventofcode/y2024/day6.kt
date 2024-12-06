package adventofcode.y2024

import adventofcode.utils.Direction
import adventofcode.utils.Point
import adventofcode.utils.PointInDirection
import adventofcode.utils.findShortestPathByPredicate
import adventofcode.utils.getNextPoint
import adventofcode.utils.getPointOrNull
import adventofcode.utils.println
import adventofcode.utils.readInput
import adventofcode.utils.rotateRight
import kotlin.time.measureTime

fun main() {
    val input = readInput("2024/day6.txt").map { it.toCharArray().toMutableList() }.toMutableList()
    val startRow = input.indexOfFirst { it.any { it == '^' } }
    val startColumn = input[startRow].indexOf('^')
    val start = PointInDirection(Point(startRow.toLong(), startColumn.toLong()), Direction.UP)

    val path = findShortestPathByPredicate(
        start,
        { (point, _) -> point.rowIndex !in input.indices || point.columnIndex !in input.first().indices },
        { it.neighbours(input) }
    )
    val part1 = path.getPath().map { it.point }.filter { it.rowIndex in input.indices && it.columnIndex in input.first().indices }.toSet().size
    println("Part1: $part1")

    measureTime {
        val part2 = path.getPath().fold(setOf<Point>()) { acc, pointInDirection ->
            val pathByPlacingObstacle = findShortestPathByPredicate(
                start,
                { it.point.rowIndex !in input.indices || it.point.columnIndex !in input.first().indices },
                { it.neighboursPart2(input, pointInDirection.point) }
            )
            if (pathByPlacingObstacle.end == null && input.getPointOrNull(pointInDirection.point) !in listOf('#', null)) {
                acc + pointInDirection.point
            } else {
                acc
            }
        }.filter { it.rowIndex in input.indices && it.columnIndex in input.first().indices && it != Point(startRow.toLong(), startColumn.toLong()) }
        println("Part2: ${part2.size}")
    }.println()
}

private fun PointInDirection.neighbours(input: MutableList<MutableList<Char>>): List<PointInDirection> = buildList {
    if (input.getPointOrNull(getNextPoint()) != '#') {
        add(PointInDirection(getNextPoint(), direction))
    } else {
        add(PointInDirection(getNextPointRotateRight(), direction.rotateRight()))
    }
}

private fun PointInDirection.neighboursPart2(input: MutableList<MutableList<Char>>, newObstacle: Point): List<PointInDirection> = buildList {
    if (input.getPointOrNull(getNextPoint()) != '#' && getNextPoint() != newObstacle
    ) {
        add(PointInDirection(getNextPoint(), direction))
    } else if (input.getPointOrNull(getNextPointRotateRight()) != '#' && getNextPointRotateRight() != newObstacle
    ) {
        add(PointInDirection(getNextPointRotateRight(), direction.rotateRight()))
    } else if (input.getPointOrNull(getNextPointRotateRightTwice()) != '#' && getNextPointRotateRightTwice() != newObstacle
    ) {
        add(PointInDirection(getNextPointRotateRightTwice(), direction.rotateRight().rotateRight()))
    }
}

private fun PointInDirection.getNextPoint() =
    point.getNextPoint(direction)

private fun PointInDirection.getNextPointRotateRight() =
    point.getNextPoint(direction.rotateRight())

private fun PointInDirection.getNextPointRotateRightTwice() =
    point.getNextPoint(direction.rotateRight().rotateRight())