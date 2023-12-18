package adventofcode.y2023

import adventofcode.utils.Point
import adventofcode.utils.calculateAreaOfConvexHullUsingShoelace
import adventofcode.utils.calculateAreaOfPolygonByPicksTheorem
import adventofcode.utils.readInput

fun calculateVolume(commands: List<String>): Long {
    val points = mutableListOf<Point>()
    var position = Point(0, 0)
    var volume = 0

    for (command in commands) {
        val direction = command[0]
        val distance = Regex("(\\d+)").findAll(command).first().value.toInt()

        when (direction) {
            'U' -> position = position.copy(rowIndex = position.rowIndex + distance)
            'D' -> position = position.copy(rowIndex = position.rowIndex - distance)
            'L' -> position = position.copy(columnIndex = position.columnIndex - distance)
            'R' -> position = position.copy(columnIndex = position.columnIndex + distance)
        }

        if (!points.contains(position)) {
            points.add(position)
            volume += distance
        }
    }

    val area = calculateAreaOfConvexHullUsingShoelace(points)
    return calculateAreaOfPolygonByPicksTheorem(area, volume.toLong()) + volume
}

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val commands = readInput("2023/day18.txt")
    println("The lagoon can contain ${calculateVolume(commands)} cubic meters of lava.")

    val part2Commands = commands.map {
        val hex = Regex("(#[a-z0-9]+)").find(it)!!.value.drop(1)
        val steps = hex.take(5).hexToInt(HexFormat.Default)
        val direction = when (hex.takeLast(1).toInt()) {
            0 -> 'R'
            1 -> 'D'
            2 -> 'L'
            3 -> 'U'
            else -> throw IllegalArgumentException()
        }
        "$direction $steps"
    }
    println("The lagoon can contain ${calculateVolume(part2Commands)} cubic meters of lava.")
}
