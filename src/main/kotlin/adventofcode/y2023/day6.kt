package adventofcode.y2023

import adventofcode.utils.readInput

fun main() {
    data class Race(val time: Long, val distance: Long) {
        fun doIWin(holdingTime: Long): Boolean =
            (holdingTime * (time - holdingTime)) > distance
    }

    val input: List<String> = readInput("2023/input.txt")

    val times = input.first().split(": ").last().let { line -> Regex("(\\d+)").findAll(line).toList() }
    val durations = input.last().split(": ").last().let { line -> Regex("(\\d+)").findAll(line).toList() }

    val races = times.indices.map { Race(times[it].value.toLong(), durations[it].value.toLong()) }

    println(races
        .map { race ->
            (0..race.time).map { race.doIWin(it) }.count { it }
        }
        .reduce { accumulator, element ->
            accumulator * element
        }
    )

    Race(
        input.first().split(": ").last().replace(" ", "").toLong(),
        input.last().split(": ").last().replace(" ", "").toLong()
    ).let { race ->
        println((0..race.time).map { race.doIWin(it) }.count { it })
    }
}
