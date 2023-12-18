package adventofcode.utils

data class Point(val columnIndex: Long, val rowIndex: Long)

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

fun Point.getNextPoint(direction: Direction): Point =
    when(direction) {
        Direction.UP -> Point(columnIndex, rowIndex - 1)
        Direction.DOWN -> Point(columnIndex, rowIndex + 1)
        Direction.LEFT -> Point(columnIndex - 1, rowIndex)
        Direction.RIGHT -> Point(columnIndex + 1, rowIndex)
    }

fun Direction.getOtherDirectionsWithoutGoingBack(): List<Direction> =
    when(this) {
        Direction.UP,
        Direction.DOWN -> listOf(Direction.LEFT, Direction.RIGHT)
        Direction.LEFT,
        Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
    }
