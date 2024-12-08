package adventofcode.y2024

import adventofcode.utils.Point
import adventofcode.utils.println
import adventofcode.utils.readInput
import kotlin.time.measureTime

fun main() {
    val input = readInput("2024/day8/input.txt")
    val rowRange = input.indices
    val columnRange = input.first().indices
    val mapOfAntennas: Map<Char, List<Point>> = input
        .flatMapIndexed { rowIndex: Int, row: String ->
            row.mapIndexed { columnIndex, character ->
                character to Point(rowIndex.toLong(), columnIndex.toLong())
            }
        }
        .filter { it.first != '.' }
        .groupBy { it.first }
        .mapValues { it.value.map { it.second } }

    measureTime {
        val part1: Int = mapOfAntennas.flatMap { antennaType ->
            antennaType.value.foldIndexed(mutableListOf<Point>()) { index, acc, point ->
                acc.apply { addAll(calculateAntiNodesPart1(antennaType.value.slice(index + 1..antennaType.value.lastIndex), point)) }
            }
        }.filter { it.rowIndex in rowRange && it.columnIndex in columnRange }.toSet().size
        println("Part 1: $part1")
    }.println()

    measureTime {
        val part2: Int = mapOfAntennas.flatMap { antennaType ->
            antennaType.value.foldIndexed(mutableListOf<Point>()) { index, acc, point ->
                acc.apply { addAll(calculateAntiNodesPart2(antennaType.value.slice(index + 1..antennaType.value.lastIndex), point, rowRange, columnRange)) }
            }
        }.filter { it.rowIndex in input.indices && it.columnIndex in input.first().indices }.plus(mapOfAntennas.filter { it.value.size > 1 }.values.flatten()).toSet().size
        println("Part 2: $part2")
    }.println()
}

fun calculateAntiNodesPart2(remainingListOfAntennas: List<Point>, point: Point, rowRange: IntRange, columnRange: IntRange): List<Point> =
    remainingListOfAntennas.flatMap { remainingAntenna ->
        val antinodesToAdd = mutableListOf<Point>()
        var steps = 1
        while (antinodesToAdd.isEmpty() || antinodesToAdd.takeLast(2).any { it.rowIndex in rowRange && it.columnIndex in columnRange }) {
            antinodesToAdd.add(remainingAntenna.getNewPointUsingDifference(remainingAntenna.getPositiveDifference(point, steps)))
            antinodesToAdd.add(point.getNewPointUsingDifference(remainingAntenna.getNegativeDifference(point, steps)))
            steps++
        }
        antinodesToAdd
    }

fun calculateAntiNodesPart1(remainingListOfAntennas: List<Point>, point: Point): List<Point> =
    remainingListOfAntennas.flatMap { remainingAntenna ->
        listOf(
            remainingAntenna.getNewPointUsingDifference(remainingAntenna.getPositiveDifference(point)),
            point.getNewPointUsingDifference(remainingAntenna.getNegativeDifference(point))
        )
    }

fun Point.getPositiveDifference(point: Point, times: Int = 1): Pair<Long, Long> =
    (rowIndex - point.rowIndex) * times to (columnIndex - point.columnIndex) * times

fun Point.getNegativeDifference(point: Point, times: Int = 1): Pair<Long, Long> =
    -((rowIndex - point.rowIndex) * times) to -((columnIndex - point.columnIndex) * times)

fun Point.getNewPointUsingDifference(difference: Pair<Long, Long>): Point =
    Point(rowIndex + difference.first, columnIndex + difference.second)
