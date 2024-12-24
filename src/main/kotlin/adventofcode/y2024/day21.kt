package adventofcode.y2024

import adventofcode.utils.Direction
import adventofcode.utils.Point
import adventofcode.utils.PointWithChar
import adventofcode.utils.getManhattenDistance
import adventofcode.utils.getNextPoint
import adventofcode.utils.permutations
import adventofcode.utils.readInput

fun main() {
    val input = readInput("2024/day21/input.txt")
    val part1 = input.sumOf { getLengthOfKeyPresses(numericKeyPad, it, 2, 0) * it.removeSuffix("A").toLong() }
    println("Part 1: $part1")
    val part2 = input.sumOf { getLengthOfKeyPresses(numericKeyPad, it, 25, 0) * it.removeSuffix("A").toLong() }
    println("Part 2: $part2")
}

private val numericKeyPad = setOf(
    setOf(
        PointWithChar(Point(0, 0), '7'),
        PointWithChar(Point(0, 1), '8'),
        PointWithChar(Point(0, 2), '9')
    ), setOf(
        PointWithChar(Point(1, 0), '4'),
        PointWithChar(Point(1, 1), '5'),
        PointWithChar(Point(1, 2), '6')
    ), setOf(
        PointWithChar(Point(2, 0), '1'),
        PointWithChar(Point(2, 1), '2'),
        PointWithChar(Point(2, 2), '3')
    ), setOf(
        PointWithChar(Point(3, 1), '0'),
        PointWithChar(Point(3, 2), 'A')
    )
)
private val directionalKeyPad = setOf(
    setOf(
        PointWithChar(Point(0, 1), '^'),
        PointWithChar(Point(0, 2), 'A')
    ), setOf(
        PointWithChar(Point(1, 0), '<'),
        PointWithChar(Point(1, 1), 'v'),
        PointWithChar(Point(1, 2), '>')
    )
)

private val cache = mutableMapOf<Triple<String, Int, Int>, Long>()
private fun getLengthOfKeyPresses(keypad: Set<Set<PointWithChar>>, instructions: String, limit: Int, depth: Int): Long {
    val startPoint = keypad.flatten().first { it.value == 'A' }.point
    return cache.getOrPut(Triple(instructions, depth, limit)) {
        instructions.fold(startPoint to 0L) { (currentPoint, sum), instruction ->
            val pointInstruction = keypad.flatten().first { it.value == instruction }.point
            val paths = getInstructionsForKeyPad(keypad, currentPoint, pointInstruction)
            pointInstruction to (sum + if (depth == limit) paths.minOf { it.length }.toLong() else paths.minOfOrNull {
                getLengthOfKeyPresses(
                    directionalKeyPad,
                    it,
                    limit,
                    depth + 1
                )
            } ?: paths.minOf { it.length }.toLong())
        }.second
    }
}

private fun getInstructionsForKeyPad(keypad: Set<Set<PointWithChar>>, currentPoint: Point, pointInstruction: Point): List<String> {
    val manhattenDistanceToInstruction = currentPoint.getManhattenDistance(pointInstruction)
    return (
        (
            if (manhattenDistanceToInstruction.first.toInt() < 0)
                "^".repeat(-manhattenDistanceToInstruction.first.toInt())
            else
                "v".repeat(manhattenDistanceToInstruction.first.toInt())
            ) +
            if (manhattenDistanceToInstruction.second.toInt() < 0)
                "<".repeat(-manhattenDistanceToInstruction.second.toInt())
            else
                ">".repeat(manhattenDistanceToInstruction.second.toInt())
        )
        .permutations()
        .filter {
            it.runningFold(currentPoint) { acc: Point, c: Char -> acc.getNextPoint(getDirectionFromChar(c)) }
                .all { point -> keypad.any { it.any { it.point == point } } }
        }
        .map { it + "A" }
        .ifEmpty { listOf("A") }
}

private fun getDirectionFromChar(char: Char): Direction = when (char) {
    '^' -> Direction.UP
    'v' -> Direction.DOWN
    '<' -> Direction.LEFT
    '>' -> Direction.RIGHT
    else -> throw IllegalArgumentException("Unknown character: $char")
}