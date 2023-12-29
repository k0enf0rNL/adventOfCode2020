package adventofcode.y2023

import adventofcode.utils.readInput

data class Game(val number: Int, val sets: List<Day2Set>)

data class Day2Set(val balls: Map<String, Int>)

fun main() {

    val input: List<Game> = readInput("2023/day2.txt")
        .map {
            Game(
                it.substring(5, it.indexOf(":")).toInt(),
                it.removeRange(0, it.indexOf(":") + 2).split("; ").map {
                    Day2Set(it.split(", ").map {
                        val temp = it.split(" ")
                        temp[1] to temp[0].toInt()
                    }.toMap())
                })
        }

    input.filter { it.sets.all { (it.balls["red"] ?: 0) <= 12 && (it.balls["green"] ?: 0) <= 13 && (it.balls["blue"] ?: 0) <= 14 } }.sumOf { it.number }.let { println(it) }

    input.map {
        (it.sets.maxBy { it.balls["green"] ?: 1 }.balls["green"] ?: 1) * (it.sets.maxBy { it.balls["blue"] ?: 1 }.balls["blue"] ?: 1) * (it.sets.maxBy {
            it.balls["red"] ?: 1
        }.balls["red"] ?: 1)
    }.sum().let { println(it) }
}
