package adventofcode.y2023

import adventofcode.utils.println
import adventofcode.utils.readInput

private fun String.calculateAsciiTotal(): Int {
    var result = 0
    forEach {
        result = ((result + it.code) * 17) % 256
    }
    return result
}

private fun List<Pair<String, String?>>.getFocusingPower(): Long {
    val boxes: HashMap<Int, LinkedHashMap<String, Int>> = hashMapOf()
    forEach { operation ->
        boxes.getOrPut(operation.first.calculateAsciiTotal()) { linkedMapOf() }.let { lensesMap ->
            if (operation.second.isNullOrBlank()) {
                lensesMap.remove(operation.first)
            } else {
                lensesMap[operation.first] = operation.second!!.toInt()
            }
        }
    }
    return boxes.map { box -> box.value.toList().mapIndexed { index, lens -> (box.key + 1) * (index + 1) * lens.second }.sum().toLong() }.sum()
}

fun main() {
    val input = readInput("2023/day15.txt")

    input.first().split(",").sumOf { it.calculateAsciiTotal() }.println()

    input.first().split(",").map { it.split(Regex("[\\-=]")).let { it.first() to it.last() } }.getFocusingPower().println()
}


