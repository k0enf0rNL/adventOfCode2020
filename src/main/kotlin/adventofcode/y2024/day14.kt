package adventofcode.y2024

import adventofcode.utils.Point
import adventofcode.utils.readInput

data class PointWithVelocity(val point: Point, val rowVelocity: Long, val columnVelocity: Long) {
    fun getNextPoint(steps: Long, maxRowIndex: Long, maxColumnIndex: Long): Point {
        val newRowIndex = (point.rowIndex + (rowVelocity * steps)) % maxRowIndex
        val newColumnIndex = (point.columnIndex + (columnVelocity * steps)) % maxColumnIndex
        return point.copy(
            rowIndex = if (newRowIndex < 0L) maxRowIndex + newRowIndex else newRowIndex,
            columnIndex = if (newColumnIndex < 0L) maxColumnIndex + newColumnIndex else newColumnIndex,
        )
    }
}

fun main() {
    val input = readInput("2024/day14/input.txt").map {
        it.split(" ").let {
            val point = it.first().removePrefix("p=").split(",").let { Point(it.last().toLong(), it.first().toLong()) }
            val velocity = it.last().removePrefix("v=").split(",").let { it.last().toLong() to it.first().toLong() }
            PointWithVelocity(point, velocity.first, velocity.second)
        }
    }
    val maxRow = 103L // 7
    val maxCol = 101L // 11
    val maxRowIndex = maxRow - 1
    val maxColIndex = maxCol - 1

    val pointsAfter100Steps = input.map { it.getNextPoint(100, maxRow, maxCol) }
    val part1 = pointsAfter100Steps
        .filter { it.rowIndex != maxRowIndex / 2 && it.columnIndex != maxColIndex / 2 }
        .groupBy { (it.rowIndex < maxRowIndex / 2) to (it.columnIndex < maxColIndex / 2) }
        .map { it.value.size }
        .ifEmpty { null }
        ?.reduce { a, b -> a * b }
    println("Part1: $part1")

    for (i in 1L..10000L) {
        val findChristmasTree = input.map { it.getNextPoint(i, maxRow, maxCol) }
        val groupByRow = findChristmasTree.groupBy { it.rowIndex }
        val groupByColumn = findChristmasTree.groupBy { it.columnIndex }
        if (groupByRow.filter { it.value.size > 20 }.size >= 3 && groupByColumn.filter { it.value.size >= 20 }.size >= 8) {
            println("Steps: $i")
            findChristmasTree.prettyPrint(maxRowIndex, maxColIndex)
            println("\n")
        }
    }
}

private fun List<Point>.prettyPrint(maxRowIndex: Long, maxColIndex: Long) {
    for (i in 0L until maxRowIndex) {
        for (j in 0L until maxColIndex) {
            print(count { it.rowIndex == i && it.columnIndex == j }.let {
                if (it == 0) {
                    "."
                } else it.toString()
            })
        }
        print("\n")
    }
}
