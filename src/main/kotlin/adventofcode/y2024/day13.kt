package adventofcode.y2024

import adventofcode.utils.readInput
import adventofcode.utils.split
import java.math.BigDecimal
import java.math.MathContext

data class Machine(val buttonA: Pair<BigDecimal, BigDecimal>, val buttonB: Pair<BigDecimal, BigDecimal>, val prize: Pair<BigDecimal, BigDecimal>) {
    fun calculateTotalButtonPressesToWin(): BigDecimal {
        val divide: BigDecimal = buttonA.first.multiply(buttonB.second).subtract(buttonB.first.multiply(buttonA.second))
        val buttonAPresses: BigDecimal = (prize.first.multiply(buttonB.second).subtract(buttonB.first.multiply(prize.second))).divide(divide, MathContext.DECIMAL128)
        val buttonBPresses: BigDecimal = (buttonA.first.multiply(prize.second).subtract(prize.first.multiply(buttonA.second))).divide(divide, MathContext.DECIMAL128)
        if (buttonBPresses.stripTrailingZeros().scale() <= 0 && buttonAPresses.stripTrailingZeros().scale() <= 0) {
            return BigDecimal(3) * buttonAPresses + buttonBPresses
        } else {
            return BigDecimal(0)
        }
    }
}

fun main () {
    val input = readInput("2024/day13/input.txt").split { it.isBlank() }.map { machine ->
        Machine(
            machine[0].split(", ").let { Regex("\\d+").find(it.first())!!.value.toBigDecimal(MathContext.DECIMAL128) to Regex("\\d+").find(it.last())!!.value.toBigDecimal(MathContext.DECIMAL128) },
            machine[1].split(", ").let { Regex("\\d+").find(it.first())!!.value.toBigDecimal(MathContext.DECIMAL128) to Regex("\\d+").find(it.last())!!.value.toBigDecimal(MathContext.DECIMAL128) },
            machine[2].split(", ").let { Regex("\\d+").find(it.first())!!.value.toBigDecimal(MathContext.DECIMAL128) to Regex("\\d+").find(it.last())!!.value.toBigDecimal(MathContext.DECIMAL128) }
        )
    }
    val part1 = input.sumOf { it.calculateTotalButtonPressesToWin() }
    println("Part1: $part1")

    val part2 = input.map { it.copy(prize = it.prize.first + BigDecimal(10000000000000) to it.prize.second + BigDecimal(10000000000000) ) }.sumOf { it.calculateTotalButtonPressesToWin() }
    println("Part2: $part2")
}