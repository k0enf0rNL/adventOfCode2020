package adventofcode.y2024

import adventofcode.utils.readInput
import adventofcode.utils.split

data class Input(val name: String, val value: Boolean)

data class Operation(val input1: String, val operation: String, val input2: String, val result: String) {
    fun getResult(inputs: List<Input>): Input =
        if (operation == "XOR") {
            Input(result, inputs.first { it.name == input1 }.value xor inputs.first { it.name == input2 }.value)
        } else if (operation == "AND") {
            Input(result, inputs.first { it.name == input1 }.value and inputs.first { it.name == input2 }.value)
        } else if (operation == "OR") {
            Input(result, inputs.first { it.name == input1 }.value or inputs.first { it.name == input2 }.value)
        } else {
            throw IllegalArgumentException("Operation '$operation' is not supported.")
        }
}

fun main() {
    val input = readInput("2024/day24/input.txt").split { it.isBlank() }
    val originalInputs = input.first().map { it.split(": ").let { Input(it.first(), it.last().toInt() == 1) } }
    val operations: List<Operation> = input.last().map { it.split(" -> ").let { operationLine -> operationLine.first().getOperation(operationLine.last()) } }
    val zValuePart1Binary = getZValue(originalInputs, operations)
    val part1 = zValuePart1Binary.toLong(2)
    println("Part 1: $part1")

    // XOR operations without result z and without input params
    val xorOperationsWithoutResultZAndWithoutInput =
        operations.filter { it.operation == "XOR" && !it.result.startsWith("z") && (!it.input1.startsWith("x") && !it.input1.startsWith("y")) }
    println(xorOperationsWithoutResultZAndWithoutInput.joinToString("\n"))
    println("---------------------------")
    // Outputs which don't result from XOR operation except the last bit
    val outputsWithoutXorOperation = operations.filter { it.operation != "XOR" && it.result.startsWith("z") && it.result != "z45" }
    println(outputsWithoutXorOperation.joinToString("\n"))
    println("---------------------------")
    // AND operations with output used wrong
    val inputsOfZValues = operations.filter { it.operation == "XOR" && it.result.startsWith("z") }
        .flatMap { listOf(it.input1, it.input2) } + xorOperationsWithoutResultZAndWithoutInput.flatMap { listOf(it.input1, it.input2) }
    val andOperationsWithOutputUsedWrong = operations.filter { it.operation == "AND" && it.result in inputsOfZValues && !it.input1.contains("00")}
    println(andOperationsWithOutputUsedWrong.joinToString("\n"))
    println("---------------------------")
    // OR operation inputs not from AND
    val inputsOfOrOperations = operations.filter { it.operation == "OR" }.flatMap { listOf(it.input1, it.input2) }
    val xorOperationThatGoesIntoOr = operations.filter { it.operation == "XOR" && it.result in inputsOfOrOperations && it !in xorOperationsWithoutResultZAndWithoutInput }
    println(xorOperationThatGoesIntoOr.joinToString("\n"))
    println("---------------------------")

    val part2 = (xorOperationsWithoutResultZAndWithoutInput + outputsWithoutXorOperation + andOperationsWithOutputUsedWrong + xorOperationThatGoesIntoOr).map { it.result }.sorted().joinToString(",")
    println("Part 2: $part2")
}

private fun getZValue(originalInputs: List<Input>, operations: List<Operation>): String {
    val inputs = originalInputs.toMutableList()
    while (true) {
        val currentInputNames = inputs.map { it.name }
        val firstParsableOperation = operations.firstOrNull { it.result !in currentInputNames && listOf(it.input1, it.input2).all { it in currentInputNames } }
        if (firstParsableOperation != null) {
            inputs.add(firstParsableOperation.getResult(inputs))
        } else {
            break
        }
    }
    val zValuePart1Binary = inputs.filter { it.name.startsWith("z") }.sortedByDescending { it.name }.map { if (it.value) 1 else 0 }.joinToString("")
    return zValuePart1Binary
}

private fun String.getOperation(resultName: String): Operation =
    if (contains("XOR")) {
        split(" XOR ").let { inputList -> Operation(inputList.first(), "XOR", inputList.last(), resultName) }
    } else if (contains("AND")) {
        split(" AND ").let { inputList -> Operation(inputList.first(), "AND", inputList.last(), resultName) }
    } else if (contains("OR")) {
        split(" OR ").let { inputList -> Operation(inputList.first(), "OR", inputList.last(), resultName) }
    } else {
        throw IllegalArgumentException("Invalid input string: $this")
    }