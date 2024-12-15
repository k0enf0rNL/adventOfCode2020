package adventofcode.y2024

import adventofcode.utils.Direction
import adventofcode.utils.Point
import adventofcode.utils.PointWithChar
import adventofcode.utils.getNextPoint
import adventofcode.utils.readInput
import adventofcode.utils.split

fun main() {
    val input = readInput("2024/day15/part2/input.txt").split { it.isBlank() }
    val warehouse = input.first().mapIndexed { rowIndex, row -> row.mapIndexed { colIndex, value -> PointWithChar(Point(rowIndex.toLong(), colIndex.toLong()), value) } }
    val movements = input.last().flatMap { it.toCharArray().toList() }.map { convertToDirection(it) }
    val startPoint = warehouse.flatMap { it.filter { it.value == '@' } }.first().point
    val setOfBoxes = warehouse.flatMap { it.filter { it.value == '[' } }.toSet().map { it.point to it.point.getNextPoint(Direction.RIGHT) }
    val setOfWalls = warehouse.flatMap { it.filter { it.value == '#' } }.toSet().map { it.point }

    val part2 = getFinalBoxPositionsAfterMovement(startPoint, setOfBoxes.toSet(), setOfWalls.toSet(), movements).sumOf { it.rowIndex * 100 + it.columnIndex }
    println("Part2: $part2")
}

private fun convertToDirection(it: Char) =
    when (it) {
        '^' -> Direction.UP
        '>' -> Direction.RIGHT
        'v' -> Direction.DOWN
        '<' -> Direction.LEFT
        else -> throw IllegalArgumentException("Unknown direction: $it")
    }

private fun getFinalBoxPositionsAfterMovement(startPoint: Point, boxes: Set<Pair<Point, Point>>, walls: Set<Point>, movements: List<Direction>): Set<Point> {
    var currentPositionOfRobot = startPoint
    val currentBoxes = boxes.toMutableSet()
    for (movement in movements) {
        when (movement) {
            Direction.UP -> {
                val relevantBoxes = currentBoxes.filter { it.first.rowIndex < currentPositionOfRobot.rowIndex }.toSet()
                val newRobotAndBoxes = moveRobotAndBoxes(currentPositionOfRobot.getNextPoint(movement), movement, relevantBoxes, walls)
                if (relevantBoxes != newRobotAndBoxes.second) {
                    currentBoxes.removeAll(relevantBoxes)
                    currentBoxes.addAll(newRobotAndBoxes.second)
                }
                if (newRobotAndBoxes.first) {
                    currentPositionOfRobot = currentPositionOfRobot.getNextPoint(movement)
                }
            }

            Direction.DOWN -> {
                val relevantBoxes = currentBoxes.filter { it.first.rowIndex > currentPositionOfRobot.rowIndex }.toSet()
                val newRobotAndBoxes = moveRobotAndBoxes(currentPositionOfRobot.getNextPoint(movement), movement, relevantBoxes, walls)
                if (relevantBoxes != newRobotAndBoxes.second) {
                    currentBoxes.removeAll(relevantBoxes)
                    currentBoxes.addAll(newRobotAndBoxes.second)
                }
                if (newRobotAndBoxes.first) {
                    currentPositionOfRobot = currentPositionOfRobot.getNextPoint(movement)
                }
            }

            Direction.LEFT -> {
                val relevantBoxes = currentBoxes.filter { it.first.columnIndex < currentPositionOfRobot.columnIndex }.toSet()
                val newRobotAndBoxes = moveRobotAndBoxes(currentPositionOfRobot.getNextPoint(movement), movement, relevantBoxes, walls)
                if (relevantBoxes != newRobotAndBoxes.second) {
                    currentBoxes.removeAll(relevantBoxes)
                    currentBoxes.addAll(newRobotAndBoxes.second)
                }
                if (newRobotAndBoxes.first) {
                    currentPositionOfRobot = currentPositionOfRobot.getNextPoint(movement)
                }
            }

            Direction.RIGHT -> {
                val relevantBoxes = currentBoxes.filter { it.second.columnIndex > currentPositionOfRobot.columnIndex }.toSet()
                val newRobotAndBoxes = moveRobotAndBoxes(currentPositionOfRobot.getNextPoint(movement), movement, relevantBoxes, walls)
                if (relevantBoxes != newRobotAndBoxes.second) {
                    currentBoxes.removeAll(relevantBoxes)
                    currentBoxes.addAll(newRobotAndBoxes.second)
                }
                if (newRobotAndBoxes.first) {
                    currentPositionOfRobot = currentPositionOfRobot.getNextPoint(movement)
                }
            }
        }
//        prettyPrint(currentPositionOfRobot, currentBoxes, walls, movement)
    }
    return currentBoxes.map { it.first }.toSet()
}

private fun prettyPrint(currentPositionOfRobot: Point, currentBoxes: MutableSet<Pair<Point, Point>>, walls: Set<Point>, movement: Direction) {
    println("Move: $movement")
    for (i in 0L .. walls.maxOf { it.rowIndex }) {
        for (j in 0L .. walls.maxOf { it.columnIndex }) {
            if (Point(i, j) == currentPositionOfRobot) {
                print('@')
            } else if (Point(i, j) in currentBoxes.map { it.first }) {
                print('[')
            } else if (Point(i, j) in currentBoxes.map { it.second }) {
                print(']')
            } else if (Point(i, j) in walls) {
                print('#')
            } else {
                print('.')
            }
        }
        print("\n")
    }
    println()
}


private fun moveRobotAndBoxes(newPositionOfRobot: Point, movement: Direction, boxesThatMightBeInTheWay: Set<Pair<Point, Point>>, walls: Set<Point>): Pair<Boolean, Set<Pair<Point, Point>>> =
    if (newPositionOfRobot !in boxesThatMightBeInTheWay.flatMap { it.toList() } && newPositionOfRobot !in walls) {
        true to boxesThatMightBeInTheWay
    } else if (newPositionOfRobot in walls) {
        false to boxesThatMightBeInTheWay
    } else {
        var overlappingBoxes: MutableSet<Pair<Point, Point>> = mutableSetOf(boxesThatMightBeInTheWay.first { it.first == newPositionOfRobot || it.second == newPositionOfRobot })
        val newBoxPositions = boxesThatMightBeInTheWay.toMutableList()
        while (overlappingBoxes.isNotEmpty()) {
            val overlappingBox = overlappingBoxes.first()
            val newBoxPosition = overlappingBox.move(movement)
            val newOverlappingBox = boxesThatMightBeInTheWay.filter { it != overlappingBox }.filter { it.first == newBoxPosition.first || it.first == newBoxPosition.second || it.second == newBoxPosition.first || it.second == newBoxPosition.second }
            newBoxPositions.remove(overlappingBox)
            overlappingBoxes.remove(overlappingBox)
            newBoxPositions.add(newBoxPosition)
            overlappingBoxes.addAll(newOverlappingBox)
        }
        if (newBoxPositions.size != boxesThatMightBeInTheWay.size) {
            throw IllegalStateException("Not the same amount of boxes")
        }
        if (newBoxPositions.all { it.first !in walls && it.second !in walls }) {
            true to newBoxPositions.toSet()
        } else {
            false to boxesThatMightBeInTheWay
        }
    }

private fun Pair<Point, Point>.move(movement: Direction): Pair<Point, Point> =
    first.getNextPoint(movement) to second.getNextPoint(movement)
