package adventofcode.y2023

import adventofcode.utils.Direction
import adventofcode.utils.Point
import adventofcode.utils.findShortestPathByPredicate
import adventofcode.utils.getNextPoint
import adventofcode.utils.getOtherDirectionsWithoutGoingBack
import adventofcode.utils.readInput

data class PointInDirection(val point: Point, val direction: Direction, val line: Int) {
    fun neighbours(): List<PointInDirection> = buildList {
        if (line < 3) {
            add(PointInDirection(point.getNextPoint(direction), direction, line + 1))
        }
        direction.getOtherDirectionsWithoutGoingBack().forEach {
            add(PointInDirection(point.getNextPoint(it), it, 1))
        }
    }

    fun neighboursPart2(): List<PointInDirection> = buildList {
        if (line < 10) {
            add(PointInDirection(point.getNextPoint(direction), direction, line + 1))
        }
        //line == 0 caters for the starting state where we can go any direction
        if (line >= 4 || line == 0) {
            direction.getOtherDirectionsWithoutGoingBack().forEach {
                add(PointInDirection(point.getNextPoint(it), it, 1))
            }
        }
    }
}

fun main() {
    val input = readInput("2023/day17.txt")
    val mapOfDigits = input.map { it.map { it.digitToInt() } }
    val start = PointInDirection(Point(0,0), Direction.RIGHT, 0)
    val end = Point(mapOfDigits.first().lastIndex.toLong(), mapOfDigits.lastIndex.toLong())

    val path = findShortestPathByPredicate(
        start,
        { (point, _) -> point == end },
        { it.neighbours().filter { it.point.rowIndex in mapOfDigits.indices && it.point.columnIndex in mapOfDigits.first().indices } },
        { _, pointInDirection -> mapOfDigits[pointInDirection.point.rowIndex.toInt()][pointInDirection.point.columnIndex.toInt()] }
    )
    println(path.getScore())

    val pathPart2 = findShortestPathByPredicate(
        start,
        { (p, _, line) -> p == end && line >= 4 },
        { it.neighboursPart2().filter { it.point.rowIndex in mapOfDigits.indices && it.point.columnIndex in mapOfDigits.first().indices } },
        { _, pointInDirection -> mapOfDigits[pointInDirection.point.rowIndex.toInt()][pointInDirection.point.columnIndex.toInt()] }
    )
    println(pathPart2.getScore())
}
