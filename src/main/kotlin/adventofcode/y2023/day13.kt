package adventofcode.y2023

import adventofcode.utils.println
import adventofcode.utils.readInput
import adventofcode.utils.split
import adventofcode.utils.transpose

fun List<String>.getMirrorValue(): Int {
    var foundMirrorIndex = this.getMirrorIndex() * 100

    if (foundMirrorIndex == 0) {
        foundMirrorIndex = this.map { it.toCharArray().toList() }.transpose().map { String(it.toCharArray()) }.getMirrorIndex()
    }
    return foundMirrorIndex
}

fun List<String>.getMirrorIndex(): Int {
    var foundMirrorIndex = 0
    forEachIndexed { index, string ->
        if (foundMirrorIndex == 0 && (string == this.getOrNull(index + 1))) {
            if ((0..<index).reversed().all { this.getOrNull(it + ((index - it) * 2) + 1) == null || this[it] == this.getOrNull(it + ((index - it) * 2) + 1) }) {
                foundMirrorIndex = index + 1
            }
        }
    }
    return foundMirrorIndex
}

fun List<String>.getMirrorValueWithSmudges(): Int {
    var foundMirrorIndex = this.getMirrorIndexWithSmudges(true) * 100

    if (foundMirrorIndex == 0) {
        foundMirrorIndex = this.map { it.toCharArray().toList() }.transpose().map { String(it.toCharArray()) }.getMirrorIndexWithSmudges(false)
    }
    return foundMirrorIndex
}

fun List<String>.getMirrorIndexWithSmudges(isRow: Boolean): Int {
    var foundMirrorIndex = 0

    forEachIndexed { index, string ->
        if (foundMirrorIndex == 0) {
            var allowedSmudges = 1
            val smudgeFirstLine = string.mapIndexed { charIndex, char -> this.getOrNull(index + 1) != null && char != this.getOrNull(index + 1)!![charIndex] }.count { it }
            if (string == this.getOrNull(index + 1) || allowedSmudges == smudgeFirstLine) {
                if (smudgeFirstLine > 0) {
                    allowedSmudges = 0
                }
                val isMirror = (0..<index).reversed().map {
                    val smudge = this[it].mapIndexed { charIndex, char -> this.getOrNull(it + ((index - it) * 2) + 1) != null && char != this.getOrNull(it + ((index - it) * 2) + 1)!![charIndex] }.count { it }
                    val result = this.getOrNull(it + ((index - it) * 2) + 1) == null || (this[it] == this.getOrNull(it + ((index - it) * 2) + 1) || allowedSmudges == smudge)
                    if (smudge > 0) {
                        allowedSmudges = 0
                    }
                    result
                }
                if (isMirror.all { it } && allowedSmudges == 0) {
                    println(this.joinToString("\n"))
                    println("Found Mirror Index = ${index + 1} in row: $isRow")
                    foundMirrorIndex = index + 1
                }
            }
        }
    }
    return foundMirrorIndex
}

fun main() {
    val input = readInput("2023/day13.txt")
    val fields: List<List<String>> = input.split { it.isBlank() }

    println(fields.sumOf { it.getMirrorValue() })

    println(fields.sumOf { it.getMirrorValueWithSmudges() }) // 35177 too high
}
