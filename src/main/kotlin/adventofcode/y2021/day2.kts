package adventofcode.y2021

import java.lang.IllegalArgumentException

enum class Direction {
    FORWARD,
    DOWN,
    UP
}

data class Move(
    val direction: Direction,
    val amount: Int
)

fun List<String>.toMove() =
    Move(get(0).toDirection(), get(1).toInt())

fun String.toDirection() =
    when(this@toDirection) {
        "forward" -> Direction.FORWARD
        "down" -> Direction.DOWN
        "up" -> Direction.UP
        else -> throw IllegalArgumentException("Dit klopt niet ${this@toDirection}")
    }

val input: List<Move> = this::class.java.classLoader.getResource("2021/day2.txt")!!.readText()
    .split(System.lineSeparator())
    .filter { it.isNotBlank() }
    .map { it.split(" ").toMove() }

// Part 1
part1()
fun part1() {
    var horizontal = 0
    var depth = 0

    input.forEach {
        when (it.direction) {
            Direction.FORWARD -> horizontal += it.amount
            Direction.DOWN -> depth += it.amount
            Direction.UP -> depth -= it.amount
        }
    }

    println("Horizontal $horizontal")
    println("Depth $depth")

    println("Result part 1: ${horizontal * depth}")
}

// Part 2
part2()
fun part2() {
    var horizontal = 0
    var depth = 0
    var aim = 0

    input.forEach {
        when (it.direction) {
            Direction.FORWARD -> {
                horizontal += it.amount
                depth += (aim * it.amount)
            }
            Direction.DOWN -> aim += it.amount
            Direction.UP -> aim -= it.amount
        }
    }

    println("Horizontal $horizontal")
    println("Depth $depth")

    println("Result part 2: ${horizontal * depth}")
}
