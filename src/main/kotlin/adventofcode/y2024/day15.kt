package adventofcode.y2024

import adventofcode.utils.Direction
import adventofcode.utils.Point
import adventofcode.utils.PointWithChar
import adventofcode.utils.getNextPoint
import adventofcode.utils.readInput
import adventofcode.utils.split

fun main() {
    val input = readInput("2024/day15/input.txt").split { it.isBlank() }
    val warehouse = input.first().mapIndexed { rowIndex, row -> row.mapIndexed { colIndex, value -> PointWithChar(Point(rowIndex.toLong(), colIndex.toLong()), value) } }
    val movements = input.last().flatMap { it.toCharArray().toList() }.map { convertToDirection(it) }
    val startPoint = warehouse.flatMap { it.filter { it.value == '@' } }.first().point
    val setOfBoxes = warehouse.flatMap { it.filter { it.value == 'O' } }.toSet().map { it.point }
    val setOfWalls = warehouse.flatMap { it.filter { it.value == '#' } }.toSet().map { it.point }

    val part1 = getFinalBoxPositionsAfterMovement(startPoint, setOfBoxes.toSet(), setOfWalls.toSet(), movements).sumOf { it.rowIndex * 100 + it.columnIndex }
    println("Part1: $part1")
}

private fun convertToDirection(it: Char) =
    when (it) {
        '^' -> Direction.UP
        '>' -> Direction.RIGHT
        'v' -> Direction.DOWN
        '<' -> Direction.LEFT
        else -> throw IllegalArgumentException("Unknown direction: $it")
    }

private fun getFinalBoxPositionsAfterMovement(startPoint: Point, boxes: Set<Point>, walls: Set<Point>, movements: List<Direction>): Set<Point> {
    var currentPositionOfRobot = startPoint
    val currentBoxes = boxes.toMutableSet()
    for (movement in movements) {
        when (movement) {
            Direction.UP -> {
                val relevantBoxes = currentBoxes.filter { it.rowIndex < currentPositionOfRobot.rowIndex && it.columnIndex == currentPositionOfRobot.columnIndex }.toSet()
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
                val relevantBoxes = currentBoxes.filter { it.rowIndex > currentPositionOfRobot.rowIndex && it.columnIndex == currentPositionOfRobot.columnIndex }.toSet()
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
                val relevantBoxes = currentBoxes.filter { it.rowIndex == currentPositionOfRobot.rowIndex && it.columnIndex < currentPositionOfRobot.columnIndex }.toSet()
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
                val relevantBoxes = currentBoxes.filter { it.rowIndex == currentPositionOfRobot.rowIndex && it.columnIndex > currentPositionOfRobot.columnIndex }.toSet()
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
    return currentBoxes.toSet()
}

private fun prettyPrint(currentPositionOfRobot: Point, currentBoxes: MutableSet<Point>, walls: Set<Point>, movement: Direction) {
    println("Move: $movement")
    for (i in 0L .. walls.maxOf { it.rowIndex }) {
        for (j in 0L .. walls.maxOf { it.columnIndex }) {
            if (Point(i, j) == currentPositionOfRobot) {
                print('@')
            } else if (Point(i, j) in currentBoxes) {
                print('O')
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


private fun moveRobotAndBoxes(newPositionOfRobot: Point, movement: Direction, boxesThatMightBeInTheWay: Set<Point>, walls: Set<Point>): Pair<Boolean, Set<Point>> =
    if (newPositionOfRobot !in boxesThatMightBeInTheWay && newPositionOfRobot !in walls) {
        true to boxesThatMightBeInTheWay
    } else if (newPositionOfRobot in walls) {
        false to boxesThatMightBeInTheWay
    } else {
        var newBoxPoint = newPositionOfRobot
        while (newBoxPoint in boxesThatMightBeInTheWay) {
            newBoxPoint = newBoxPoint.getNextPoint(movement)
        }
        if (newBoxPoint !in walls) {
            true to boxesThatMightBeInTheWay.toMutableSet().apply { remove(newPositionOfRobot); add(newBoxPoint) }
        } else {
            false to boxesThatMightBeInTheWay
        }
    }
