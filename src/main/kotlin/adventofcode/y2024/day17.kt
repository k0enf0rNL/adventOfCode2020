package adventofcode.y2024

import adventofcode.utils.readInput
import adventofcode.utils.split
import kotlin.math.pow

fun main() {
    val input = readInput("2024/day17/input.txt").split { it.isBlank() }
    val instructions = input.last().first().split(": ").last().split(",").map { it.toInt() }
    val outputPart1 = getOutput(input, input.first().first().split(": ").last().toLong(), instructions)
    println("Part 1: ${outputPart1.joinToString(",")}")

    var aValue = 0L
    for (i: Int in instructions.size-2 downTo 0) {
        var outputPart2 = getOutput(input, aValue + 8.toDouble().pow(i).toLong(), instructions)
        var offset = 2
        var newAValue = aValue + 8.toDouble().pow(i).toLong()
        while (outputPart2.reversed().take(instructions.size - i) != instructions.reversed().take(instructions.size - i)) {
            newAValue = aValue + (8.toDouble().pow(i).toLong() * offset)
            outputPart2 = getOutput(input, newAValue, instructions)
            println("Output with offset for value a $newAValue $offset: $outputPart2")
            offset++
        }
        aValue = newAValue
        println("${aValue}: ${outputPart2.joinToString(",")}")
    }

    // 105875099939593 Too high
    for (i: Long in 105875099939593 downTo 105875099939593 - 50_000) {
        val outputPart2 = getOutput(input, i, instructions)
        if (outputPart2 == instructions) {
            println("$i also equals")
        }
    }

}

private fun getOutput(input: List<List<String>>, aValue: Long, instructions: List<Int>): MutableList<Int> {
    var registerA: Long = aValue
    var registerB: Long = 0
    var registerC: Long = 0
    val output = mutableListOf<Int>()
    var index = 0
    while (index <= instructions.lastIndex) {
        when (instructions[index]) {
            0 -> {
                registerA = registerA / (combo(instructions[index + 1], registerA, registerB, registerC)).let { 2.0.pow(it.toDouble()).toLong() }
                index += 2
            }

            1 -> {
                registerB = registerB xor instructions[index + 1].toLong()
                index += 2
            }

            2 -> {
                registerB = combo(instructions[index + 1], registerA, registerB, registerC) % 8
                index += 2
            }

            3 -> {
                if (registerA == 0L) {
                    index += 2
                } else {
                    index = instructions[index + 1]
                }
            }

            4 -> {
                registerB = registerB xor registerC
                index += 2
            }

            5 -> {
                output.add((combo(instructions[index + 1], registerA, registerB, registerC) % 8).toInt())
                index += 2
            }

            6 -> {
                registerB = registerA / (combo(instructions[index + 1], registerA, registerB, registerC)).let { 2.0.pow(it.toDouble()).toLong() }
                index += 2
            }

            7 -> {
                registerC = registerA / (combo(instructions[index + 1], registerA, registerB, registerC)).let { 2.0.pow(it.toDouble()).toLong() }
                index += 2
            }

            else -> IllegalStateException("Unknown instruction index: $index")
        }
    }
    return output
}

fun combo(operand: Int, registerA: Long, registerB: Long, registerC: Long): Long =
    when(operand) {
        0, 1, 2, 3 -> operand.toLong()
        4 -> registerA
        5 -> registerB
        6 -> registerC
        else -> throw IllegalStateException()
    }
