package adventofcode.y2021

import adventofcode.utils.readInput
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

data class Point(
    val x: Int,
    val y: Int
)

data class PointCount(
    val point: Point,
    var count: Int = 0
)

fun main() {
    fun List<Int>.toPoint() =
        Point(
            this@toPoint[0] * 1,
            this@toPoint[1] * 1
        )

    fun List<Point>.toLineOfPoints(): List<Point> {
        val start = this@toLineOfPoints[0]
        val end = this@toLineOfPoints[1]
        val xIncline = (start.x - end.x).sign
        val yIncline = (start.y - end.y).sign
        val height = max((start.x - end.x).absoluteValue, (start.y - end.y).absoluteValue)
        return (0..height).map { i -> Point(start.x + i * -xIncline, start.y + i * -yIncline) }
    }

    fun List<List<Point>>.countOverlappingPoints(diagonal: Boolean): List<PointCount> {
        val pointCountList: MutableList<PointCount> = ArrayList()
        this@countOverlappingPoints.forEach {
            if ((diagonal && it.distinctBy { it.x }.size != 1 && it.distinctBy { it.y }.size != 1) || (it.distinctBy { it.x }.size == 1 || it.distinctBy { it.y }.size == 1)) {
                it.forEach { point ->
                    pointCountList.firstOrNull { it.point == point }?.apply { count += 1 } ?: pointCountList.add(PointCount(point, 1))
                }
            }
        }
        return pointCountList
    }

    val input: List<String> = readInput("2021/day5.txt")

// Part 1
    input.map {
        it.split(" -> ").map { it.split(",").map { it.toInt() }.toPoint() }.toLineOfPoints()
    }.countOverlappingPoints(diagonal = false).count { it.count > 1 }.also { println("Part 1: $it") }

// Part 2
    input.map {
        it.split(" -> ").map { it.split(",").map { it.toInt() }.toPoint() }.toLineOfPoints()
    }.countOverlappingPoints(diagonal = true).count { it.count > 1 }.also { println("Part 2: $it") }
}
