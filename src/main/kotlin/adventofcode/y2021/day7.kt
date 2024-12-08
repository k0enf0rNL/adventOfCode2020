import adventofcode.utils.readInput
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

fun main() {

    val input: List<Int> = readInput("2021/input.txt")
        .first().split(",")
        .filter { it.isNotBlank() }
        .map { it.toInt() }
        .sorted()

    fun List<Int>.median() = sorted().let {
        if (it.size % 2 == 0)
            (it[it.size / 2] + it[(it.size - 1) / 2]) / 2
        else
            it[it.size / 2]
    }

//Part 1
    input.median().let { median -> input.map { (it - median).absoluteValue } }.sum().also {
        println("Part 1: $it")
    }

//Part 2
    input.average().toInt().toDouble().let { average ->
        input.map {
            val diff = (it - average).absoluteValue
            (diff * (1 + diff) / 2).toLong()
        }
    }.sum().also { println("Part 2: $it") }
}


