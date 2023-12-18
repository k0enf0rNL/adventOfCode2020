package adventofcode.y2023

import adventofcode.utils.Direction
import adventofcode.utils.readInput
import kotlin.math.max

data class Tile(val char: Char, var energized: Boolean = false, val seenBeams: MutableList<Beam> = mutableListOf())

data class Beam(var rowIndex: Int, var columnIndex: Int, var direction: Direction)

fun followBeam(beam: Beam, grid: HashMap<Pair<Int, Int>, Tile>, beams: MutableList<Beam>) {
    val tile = grid[beam.rowIndex to beam.columnIndex]!!
    if (tile.seenBeams.any { it == beam }) {
        beams.remove(beam)
        return
    }
    tile.seenBeams.add(beam.copy())
    tile.energized = true
    when (tile.char) {
        '|' -> if (beam.direction == Direction.LEFT || beam.direction == Direction.RIGHT) {
            beams.add(Beam(beam.rowIndex + 1, beam.columnIndex, Direction.DOWN))
            beam.direction = Direction.UP
        }

        '\\' -> beam.direction = when (beam.direction) {
            Direction.UP -> Direction.LEFT
            Direction.DOWN -> Direction.RIGHT
            Direction.LEFT -> Direction.UP
            Direction.RIGHT -> Direction.DOWN
        }

        '/' -> beam.direction = when (beam.direction) {
            Direction.UP -> Direction.RIGHT
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.DOWN
            Direction.RIGHT -> Direction.UP
        }

        '-' -> if (beam.direction == Direction.UP || beam.direction == Direction.DOWN) {
            beams.add(Beam(beam.rowIndex, beam.columnIndex + 1, Direction.RIGHT))
            beam.direction = Direction.LEFT
        }

        else -> {}
    }
    when (beam.direction) {
        Direction.UP -> beam.rowIndex -= 1
        Direction.DOWN -> beam.rowIndex += 1
        Direction.LEFT -> beam.columnIndex -= 1
        Direction.RIGHT -> beam.columnIndex += 1
    }
}

fun part1(input: List<String>, startingRowIndex: Int, startingColumnIndex: Int, direction: Direction): Pair<HashMap<Pair<Int, Int>, Tile>, MutableList<Beam>> {
    val grid: HashMap<Pair<Int, Int>, Tile> =
        HashMap(input.flatMapIndexed { rowIndex, line -> line.toCharArray().toList().mapIndexed { columnIndex, char -> (rowIndex to columnIndex) to Tile(char) } }.toMap())
    val beams: MutableList<Beam> = mutableListOf(Beam(startingRowIndex, startingColumnIndex, direction))
    val maxRowIndex = grid.keys.maxOf { it.first }
    val maxColumnIndex = grid.keys.maxOf { it.second }
    while (beams.any { it.rowIndex in 0..maxRowIndex && it.columnIndex in 0..maxColumnIndex }) {
        val beam = beams.first { it.rowIndex in 0..maxRowIndex && it.columnIndex in 0..maxColumnIndex }
        followBeam(beam, grid, beams)
    }
    return grid to beams
}

fun main() {
    val input = readInput("2023/day16.txt")
    val part1Result = part1(input, 0, 0, Direction.RIGHT)
    val part1Count = part1Result.first.count { it.value.energized }

    println(part1Count)

    var maxEnergized = part1Count
    val pairsAlreadyCalculated: MutableList<Pair<Pair<Int, Int>, Direction>> = mutableListOf()
    val startingPairs: MutableList<Pair<Pair<Int, Int>, Direction>> = mutableListOf()
    startingPairs.addAll((0..<110).flatMap { listOf((0 to it) to Direction.DOWN, (it to 0) to Direction.RIGHT, (109 to it) to Direction.UP, (it to 109) to Direction.LEFT) })
    startingPairs.remove((0 to 0) to Direction.RIGHT)
    (0..<startingPairs.size).forEach {
        if (!pairsAlreadyCalculated.contains(startingPairs[it])) {
            val result = part1(input, startingPairs[it].first.first, startingPairs[it].first.second, startingPairs[it].second)
            result.second.filter { it.rowIndex < 0 }.forEach { pairsAlreadyCalculated.add((it.rowIndex + 1 to it.columnIndex) to Direction.DOWN) }
            result.second.filter { it.rowIndex > 109 }.forEach { pairsAlreadyCalculated.add((it.rowIndex - 1 to it.columnIndex) to Direction.UP) }
            result.second.filter { it.columnIndex < 0 }.forEach { pairsAlreadyCalculated.add((it.rowIndex to it.columnIndex + 1) to Direction.RIGHT) }
            result.second.filter { it.columnIndex > 109 }.forEach { pairsAlreadyCalculated.add((it.rowIndex to it.columnIndex - 1) to Direction.LEFT) }
            maxEnergized = max(maxEnergized, result.first.count { it.value.energized })
        }
    }
    println(maxEnergized)
}
