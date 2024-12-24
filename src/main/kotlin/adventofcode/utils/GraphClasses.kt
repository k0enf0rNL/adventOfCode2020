package adventofcode.utils

import kotlin.math.abs

data class Point(val rowIndex: Long, val columnIndex: Long)
data class PointInDirection(val point: Point, val direction: Direction)
data class PointInDirectionWithLine(val point: Point, val direction: Direction, val line: Int)
data class PointWithInt(val point: Point, val value: Int)
data class PointWithChar(val point: Point, val value: Char)

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

enum class DiagonalDirection {
    UP_RIGHT,
    UP_LEFT,
    DOWN_RIGHT,
    DOWN_LEFT
}

fun Point.getNextPoint(direction: Direction): Point =
    when (direction) {
        Direction.UP -> Point(rowIndex - 1, columnIndex)
        Direction.DOWN -> Point(rowIndex + 1, columnIndex)
        Direction.LEFT -> Point(rowIndex, columnIndex - 1)
        Direction.RIGHT -> Point(rowIndex, columnIndex + 1)
    }

fun Point.getNextPointDiagonally(direction: DiagonalDirection): Point =
    when (direction) {
        DiagonalDirection.UP_RIGHT -> Point(rowIndex - 1, columnIndex + 1)
        DiagonalDirection.DOWN_RIGHT -> Point(rowIndex + 1, columnIndex + 1)
        DiagonalDirection.DOWN_LEFT -> Point(rowIndex + 1, columnIndex - 1)
        DiagonalDirection.UP_LEFT -> Point(rowIndex - 1, columnIndex - 1)
    }

fun Point.getNeighbours(): List<Point> = Direction.entries.toTypedArray().map {
    getNextPoint(it)
}

fun Direction.getOtherDirectionsWithoutGoingBack(): List<Direction> =
    when (this) {
        Direction.UP,
        Direction.DOWN -> listOf(Direction.LEFT, Direction.RIGHT)

        Direction.LEFT,
        Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
    }

fun Direction.rotateRight(): Direction =
    when (this) {
        Direction.UP -> Direction.RIGHT
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
        Direction.RIGHT -> Direction.DOWN
    }

fun <T> List<List<T>>.getPointOrNull(point: Point): T? =
    getOrNull(point.rowIndex.toInt())?.getOrNull(point.columnIndex.toInt())

fun <T> List<List<T>>.getNeighbours(point: Point): List<T> = Direction.entries.toTypedArray().mapNotNull {
    getPointOrNull(point.getNextPoint(it))
}

fun <T> List<List<T>>.getNeighboursDiagonally(point: Point): List<Pair<T?, DiagonalDirection>> = DiagonalDirection.entries.toTypedArray().map {
    getPointOrNull(point.getNextPointDiagonally(it)) to it
}

fun <T> List<List<T>>.getNeighboursWithDirection(point: Point): List<Pair<T?, Direction>> = Direction.entries.toTypedArray().map {
    getPointOrNull(point.getNextPoint(it)) to it
}

fun <T> List<List<T>>.get90degreesNeighboursWithDirection(point: Point, direction: Direction): List<Pair<T?, Direction>> = direction.getOtherDirectionsWithoutGoingBack().plus(direction).toTypedArray().map {
    getPointOrNull(point.getNextPoint(it)) to it
}

fun List<List<PointWithChar>>.countCorners(pointWithChar: PointWithChar): Long {
    val diagonals = getNeighboursDiagonally(pointWithChar.point)
    val neighbours = getNeighboursWithDirection(pointWithChar.point)
    return diagonals.sumOf { diagonal ->
        when (diagonal.second) {
            DiagonalDirection.UP_RIGHT ->
                if (diagonal.first?.value != pointWithChar.value && neighbours.first { it.second == Direction.UP }.first?.value == pointWithChar.value && neighbours.first { it.second == Direction.RIGHT }.first?.value == pointWithChar.value) {
                    1L
                } else if (neighbours.first { it.second == Direction.UP }.first?.value != pointWithChar.value && neighbours.first { it.second == Direction.RIGHT }.first?.value != pointWithChar.value) {
                    1L
                } else {
                    0L
                }

            DiagonalDirection.UP_LEFT ->
                if (diagonal.first?.value != pointWithChar.value && neighbours.first { it.second == Direction.UP }.first?.value == pointWithChar.value && neighbours.first { it.second == Direction.LEFT }.first?.value == pointWithChar.value) {
                    1L
                } else if (neighbours.first { it.second == Direction.UP }.first?.value != pointWithChar.value && neighbours.first { it.second == Direction.LEFT }.first?.value != pointWithChar.value) {
                    1L
                } else {
                    0L
                }

            DiagonalDirection.DOWN_RIGHT ->
                if (diagonal.first?.value != pointWithChar.value && neighbours.first { it.second == Direction.DOWN }.first?.value == pointWithChar.value && neighbours.first { it.second == Direction.RIGHT }.first?.value == pointWithChar.value) {
                    1L
                } else if (neighbours.first { it.second == Direction.DOWN }.first?.value != pointWithChar.value && neighbours.first { it.second == Direction.RIGHT }.first?.value != pointWithChar.value) {
                    1L
                } else {
                    0L
                }

            DiagonalDirection.DOWN_LEFT ->
                if (diagonal.first?.value != pointWithChar.value && neighbours.first { it.second == Direction.DOWN }.first?.value == pointWithChar.value && neighbours.first { it.second == Direction.LEFT }.first?.value == pointWithChar.value) {
                    1L
                } else if (neighbours.first { it.second == Direction.DOWN }.first?.value != pointWithChar.value && neighbours.first { it.second == Direction.LEFT }.first?.value != pointWithChar.value) {
                    1L
                } else {
                    0L
                }
        }
    }
}

fun Point.getManhattenDistance(point: Point): Pair<Long, Long> =
    (point.rowIndex - rowIndex) to (point.columnIndex - columnIndex)
