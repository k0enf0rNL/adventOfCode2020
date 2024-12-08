package adventofcode.y2023

import adventofcode.utils.readInput

data class NumberOnRow(val number: Int, val indices: IntRange, val rowNumber: Int)

data class SpecialCharacter(val char: Char, val index: Int, val rowNumber: Int)


fun main() {
    fun NumberOnRow.isInRange(specialCharacter: SpecialCharacter): Boolean =
        listOf(
            specialCharacter.rowNumber,
            specialCharacter.rowNumber + 1,
            specialCharacter.rowNumber - 1
        ).contains(rowNumber) && specialCharacter.index in (indices.first - 1..indices.last + 1)

    fun getGearRatio(numberOnRows: List<NumberOnRow>): Int =
        if (numberOnRows.size == 2) {
            numberOnRows.first().number * numberOnRows.last().number
        } else {
            0
        }


    val input: List<String> = readInput("2023/input.txt")

    val numberOnRows = input.flatMapIndexed { index, line -> Regex("(\\d+)").findAll(line).map { NumberOnRow(it.value.toInt(), it.range, index + 1) } }

    val specialCharacters = input.flatMapIndexed { index, line -> Regex("([^\\d\\.])").findAll(line).map { SpecialCharacter(it.value.first(), it.range.first, index + 1) } }

    println(numberOnRows.filter { number -> specialCharacters.any { number.isInRange(it) } }.sumOf { it.number })

    println(specialCharacters.filter { it.char == '*' }.sumOf { specialChar -> getGearRatio(numberOnRows.filter { it.isInRange(specialChar) }) })
}



