package adventofcode.y2024

import adventofcode.utils.Point
import adventofcode.utils.PointWithChar
import adventofcode.utils.countCorners
import adventofcode.utils.getNeighbours
import adventofcode.utils.getNeighboursWithDirection
import adventofcode.utils.readInput

fun main() {
    val input = readInput("2024/day12/input.txt").mapIndexed { rowIndex, row -> row.mapIndexed { columnIndex, char -> PointWithChar(Point(rowIndex.toLong(), columnIndex.toLong()), char) } }
    val seen = mutableSetOf<PointWithChar>()
    val regions = mutableListOf<Long>()

    input.flatten().forEach { pointWithChar ->
        if (pointWithChar !in seen) {
            val entireRegion = getAllPlantsInRegionOfChar(input, pointWithChar)
            regions.add(entireRegion.first.size * entireRegion.second)
            seen.addAll(entireRegion.first)
        }
    }

    val part1 = regions.sum()
    println("Part1: $part1")

    val seen2 = mutableSetOf<PointWithChar>()
    val regions2 = mutableListOf<Long>()
    input.flatten().forEach { pointWithChar ->
        if (pointWithChar !in seen2) {
            val entireRegion = getAllPlantsInRegionOfCharPart2(input, pointWithChar)
            regions2.add(entireRegion.first.size * entireRegion.second)
            seen2.addAll(entireRegion.first)
        }
    }

    val part2 = regions2.sum()
    println("Part2: $part2")
}

fun getAllPlantsInRegionOfChar(input: List<List<PointWithChar>>, pointWithChar: PointWithChar): Pair<Collection<PointWithChar>, Long> {
    val seenPointsWithSameChar = mutableSetOf<PointWithChar>()
    var edges = 0L
    val remainingPointsToSee = mutableSetOf(pointWithChar)
    while (remainingPointsToSee.isNotEmpty()) {
        val currentPoint = remainingPointsToSee.first()
        val neighbours = input.getNeighbours(currentPoint.point)
        remainingPointsToSee.addAll(neighbours.filter { it.value == pointWithChar.value && !seenPointsWithSameChar.contains(it)})
        edges += neighbours.filter { it.value != pointWithChar.value }.size + (4 - neighbours.size)
        seenPointsWithSameChar.add(currentPoint)
        remainingPointsToSee.remove(currentPoint)
    }
    return seenPointsWithSameChar.filter { it.value == pointWithChar.value } to edges
}

fun getAllPlantsInRegionOfCharPart2(input: List<List<PointWithChar>>, pointWithChar: PointWithChar): Pair<Collection<PointWithChar>, Long> {
    val seenPointsWithSameChar = mutableSetOf<PointWithChar>()
    var corners = 0L
    val remainingPointsToSee = mutableSetOf(pointWithChar)
    while (remainingPointsToSee.isNotEmpty()) {
        val currentPoint = remainingPointsToSee.first()
        val neighbours = input.getNeighboursWithDirection(currentPoint.point)
        remainingPointsToSee.addAll(neighbours.mapNotNull { it.first }.filter { it.value == pointWithChar.value && !seenPointsWithSameChar.contains(it)})
        corners += input.countCorners(currentPoint)
        seenPointsWithSameChar.add(currentPoint)
        remainingPointsToSee.remove(currentPoint)
    }
    return seenPointsWithSameChar.filter { it.value == pointWithChar.value } to corners
}

