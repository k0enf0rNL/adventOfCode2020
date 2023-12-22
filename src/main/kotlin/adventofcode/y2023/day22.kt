package adventofcode.y2023

import adventofcode.utils.println
import adventofcode.utils.readInput

data class Brick(var x: IntRange, var y: IntRange, var z: IntRange) {
    val supporting: MutableList<Brick> = mutableListOf()
    val supportedBy: MutableList<Brick> = mutableListOf()

    fun canBeDisintegrated(): Boolean =
        supporting.all { it.supportedBy.size > 1 }

    fun wouldFall(fallenBricks: List<Brick>): Boolean =
        supportedBy.isNotEmpty() && supportedBy.all { fallenBricks.contains(it) }
}

fun dropBricks(bricks: List<Brick>): List<Brick> {
    val fallenBricks: MutableList<Brick> = mutableListOf()
    bricks.forEach { newBrick ->
        if (newBrick.z.first > 1) {
            val supportedBy: Pair<Int, List<Brick>> =
                fallenBricks.filter { (newBrick.x.first in it.x || newBrick.x.last in it.x || it.x.first in newBrick.x || it.x.last in newBrick.x) && (newBrick.y.first in it.y || newBrick.y.last in it.y || it.y.first in newBrick.y || it.y.last in newBrick.y) }
                    .groupBy { it.z.last }.maxByOrNull { it.key }?.toPair() ?: (0 to emptyList())
            newBrick.z = supportedBy.first + 1..newBrick.z.count() + supportedBy.first
            newBrick.supportedBy.addAll(supportedBy.second)
            supportedBy.second.forEach { it.supporting.add(newBrick) }
        }
        fallenBricks.add(newBrick)
        fallenBricks.sortBy { it.z.first }
    }
    return fallenBricks
}

fun main() {
    val input = readInput("2023/day22.txt")
    val bricks: List<Brick> =
        input.map { Regex("(\\d+)").findAll(it).map { it.value.toInt() }.toList().let { Brick(it[0]..it[3], it[1]..it[4], it[2]..it[5]) } }.sortedBy { it.z.first }
    val droppedBricks: List<Brick> = dropBricks(bricks)

    println(droppedBricks.map { it.canBeDisintegrated() }.count { it })

    val supportingBricks = droppedBricks.filter { !it.canBeDisintegrated() }
    supportingBricks.sumOf { disintegratedBrick ->
        val allFallenBricks: MutableList<Brick> = mutableListOf(disintegratedBrick)
        val collapsingBricks = droppedBricks.toMutableList()
        while (true) {
            val newFallenBricks: List<Brick> = collapsingBricks.filter { it.wouldFall(allFallenBricks) }
            collapsingBricks.removeAll(newFallenBricks)
            allFallenBricks.addAll(newFallenBricks)
            if (newFallenBricks.isEmpty()) {
                break
            }
        }
        allFallenBricks.size - 1
    }.println()
}
