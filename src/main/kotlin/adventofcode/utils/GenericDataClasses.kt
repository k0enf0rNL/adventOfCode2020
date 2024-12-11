package adventofcode.utils

data class Point(val rowIndex: Long, val columnIndex: Long)
data class PointInDirection(val point: Point, val direction: Direction)
data class PointInDirectionWithLine(val point: Point, val direction: Direction, val line: Int)
data class PointWithInt(val point: Point, val value: Int)

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

fun Point.getNextPoint(direction: Direction): Point =
    when(direction) {
        Direction.UP -> Point(rowIndex - 1, columnIndex)
        Direction.DOWN -> Point(rowIndex + 1, columnIndex)
        Direction.LEFT -> Point(rowIndex, columnIndex - 1)
        Direction.RIGHT -> Point(rowIndex, columnIndex + 1)
    }

fun Direction.getOtherDirectionsWithoutGoingBack(): List<Direction> =
    when(this) {
        Direction.UP,
        Direction.DOWN -> listOf(Direction.LEFT, Direction.RIGHT)
        Direction.LEFT,
        Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
    }

fun Direction.rotateRight(): Direction =
    when(this) {
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
