package adventofcode.y2024

import adventofcode.utils.Point
import adventofcode.utils.PointWithChar
import adventofcode.utils.findShortestPathByPredicate
import adventofcode.utils.getNeighbours
import adventofcode.utils.getNeighboursWithDirection
import adventofcode.utils.getNextPoint
import adventofcode.utils.getPointOrNull
import adventofcode.utils.readInput

fun main() {
    val input = readInput("2024/day20/input.txt").mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, value ->
            PointWithChar(
                Point(rowIndex.toLong(), columnIndex.toLong()),
                value
            )
        }
    }
    val amountToSave = 100
    val startPoint = input.flatMap { it.filter { it.value == 'S' } }.first()
    val shortestPathWithoutCheating = findShortestPathByPredicate(
        start = startPoint,
        endFunction = { it.value == 'E' },
        neighbours = { input.getNeighbours(it.point).filter { it.value != '#' } }
    )
    val part1 = shortestPathWithoutCheating.getResult().map { resultPoint ->
        val newPointsAfterCheating = input.getNeighboursWithDirection(resultPoint.key.point)
            .filter { it.first!!.value == '#' }
            .mapNotNull { input.getPointOrNull(it.first!!.point.getNextPoint(it.second)) }
            .filter { it.value != '#' }
        newPointsAfterCheating.count {
            val knownCost = shortestPathWithoutCheating.getResult()[it]?.cost
            if (knownCost != null) {
                knownCost - resultPoint.value.cost - 2 >= amountToSave
            } else {
                val newPath = findShortestPathByPredicate(
                    start = it,
                    endFunction = { it.value == 'E' },
                    neighbours = { input.getNeighbours(it.point).filter { it.value != '#' } }
                )
                if (newPath.end != null) {
                    newPath.getScore() + resultPoint.value.cost + 2 + amountToSave <= shortestPathWithoutCheating.getScore()
                } else {
                    false
                }
            }
        }
    }.sum()
    println("Part 1: $part1")

    val part2 = shortestPathWithoutCheating.getResult().map { resultPoint ->
        val cheatExitPoints: MutableSet<PointWithChar> = mutableSetOf(resultPoint.key)
        var nextPointsToCheck: Set<PointWithChar> = input.getNeighbours(resultPoint.key.point).toSet()

        (1..19).sumOf { stepsInCheat ->
            val neighbours = nextPointsToCheck
                .flatMap { input.getNeighbours(it.point) }
            val newExitPoints = neighbours
                .filter { it.value != '#' }
                .filter { it !in cheatExitPoints }
                .toSet()
            nextPointsToCheck = neighbours.toSet()
            cheatExitPoints.addAll(newExitPoints)
            newExitPoints.count {
                val knownCost = shortestPathWithoutCheating.getResult()[it]?.cost
                if (knownCost != null) {
                    knownCost - resultPoint.value.cost - (stepsInCheat+1) >= amountToSave
                } else {
                    val newPath = findShortestPathByPredicate(
                        start = it,
                        endFunction = { it.value == 'E' },
                        neighbours = { input.getNeighbours(it.point).filter { it.value != '#' } }
                    )
                    if (newPath.end != null) {
                        newPath.getScore() + resultPoint.value.cost + (stepsInCheat+1) + amountToSave <= shortestPathWithoutCheating.getScore()
                    } else {
                        false
                    }
                }
            }
        }
    }.sum()
    println("Part 2: $part2")
}