package adventofcode.y2024

import adventofcode.utils.println
import adventofcode.utils.readInput
import kotlin.time.measureTime

data class ProcessStats(
    val result: MutableList<Int> = mutableListOf(),
    var startId: Int,
    var endId: Int,
    var endIndex: Int,
    var remainingWhiteSpace: Int = 0,
    var remainingLastIdValues: Int = 0,
    val movedFilesIds: MutableSet<Int> = mutableSetOf()
)

fun main() {
    val input = readInput("2024/day9/input.txt").first().toCharArray().toList().map { it.digitToInt() }
    measureTime {
        val processResult = input.foldIndexed(ProcessStats(startId = 0, endId = (input.size / 2) + 1, endIndex = input.size + 1)) { index: Int, acc: ProcessStats, blockSize: Int ->
            acc.apply {
                if (startId < endId) {
                    if (index % 2 == 0) {
                        for (i in 0 until blockSize) {
                            result.add(startId)
                        }
                        startId++
                    } else {
                        remainingWhiteSpace = blockSize
                        while (remainingWhiteSpace != 0) {
                            while (remainingLastIdValues == 0) {
                                endIndex -= 2
                                endId--
                                remainingLastIdValues = input[endIndex]
                            }
                            result.add(endId)
                            remainingLastIdValues--
                            remainingWhiteSpace--
                        }
                    }
                } else {
                    while (remainingLastIdValues != 0) {
                        result.add(endId)
                        remainingLastIdValues--
                    }
                }
            }
        }
        val part1 = processResult.result.mapIndexed { index, id -> (index * id).toLong() }.sum()
        println("Part1: $part1")
    }.println()

    measureTime {
        val processResultPart2 =
            input.foldIndexed(ProcessStats(startId = 0, endId = (input.size / 2), endIndex = input.lastIndex)) { index: Int, acc: ProcessStats, blockSize: Int ->
                acc.apply {
                    if (index % 2 == 0) {
                        if (!movedFilesIds.contains(startId)) {
                            for (i in 0 until blockSize) {
                                result.add(startId)
                            }
                            movedFilesIds.add(startId)
                        } else {
                            for (i in 0 until blockSize) {
                                result.add(0)
                            }
                        }
                        startId++
                    } else {
                        remainingWhiteSpace = blockSize
                        for (id in endId downTo startId) {
                            val rightMostValue = input[id * 2]
                            if (rightMostValue <= remainingWhiteSpace && !movedFilesIds.contains(id)) {
                                for (i in 0 until rightMostValue) {
                                    result.add(id)
                                }
                                movedFilesIds.add(id)
                                remainingWhiteSpace -= rightMostValue
                            }
                        }
                        while (remainingWhiteSpace != 0) {
                            result.add(0)
                            remainingWhiteSpace--
                        }
                    }
                }
            }
        val part2 = processResultPart2.result.mapIndexed { index, id -> (index * id).toLong() }.sum()
        println("Part2: $part2")
    }.println()
}