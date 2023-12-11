package adventofcode.y2023

import adventofcode.utils.readInput

fun main() {

    val input: List<String> = readInput("2023/day1.txt")

    val resultpart1 = input.map { it.first { it.isDigit() }.toString() + it.last { it.isDigit() }.toString() }.sumOf { it.toInt() }

    println(resultpart1)

    val mapOfStringAndDigits = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)

    val resultpart2 = input.map { line ->
        println("Line: $line")
        val firstStringDigit = mapOfStringAndDigits.minBy {
            if (line.indexOf(it.key) == -1) {
                999
            } else {
                line.indexOf(it.key)
            }
        }
        println("First String Digit: $firstStringDigit")
        val firstRegularDigitIndex = line.indexOfFirst { it.isDigit() }
        val firstStringDigitIndex = if (line.indexOf(firstStringDigit.key) == -1) 999 else line.indexOf(firstStringDigit.key)
        println("First Regular digit index: $firstRegularDigitIndex")
        println("First string digit index: $firstStringDigitIndex")
        val firstDigit = if (firstRegularDigitIndex < firstStringDigitIndex) {
            line.first { it.isDigit() }.toString()
        } else {
            firstStringDigit.value.toString()
        }

        println("First digit: $firstDigit")

        val lastStringDigit = mapOfStringAndDigits.maxBy {
            line.lastIndexOf(it.key)
        }
        println("Last string digit: $lastStringDigit")
        println("Last string digit index: ${line.lastIndexOf(lastStringDigit.key)}")
        val lastRegularDigitIndex = line.indexOfLast { it.isDigit() }
        println("Last regular digit index: $lastRegularDigitIndex")
        val lastDigit = if (lastRegularDigitIndex > line.lastIndexOf(lastStringDigit.key)) {
            line.last { it.isDigit() }.toString()
        } else {
            lastStringDigit.value.toString()
        }
        println("Last digit: $lastDigit")

        println("Result: $firstDigit$lastDigit")
        val result = firstDigit + lastDigit
        result.toInt()
    }.sum()

    println(resultpart2)
}
