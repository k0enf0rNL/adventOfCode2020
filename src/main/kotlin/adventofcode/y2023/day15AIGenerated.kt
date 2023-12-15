package adventofcode.y2023

import adventofcode.utils.readInput

data class Lens(val label: String, var focalLength: Int)

fun main() {
    val input = readInput("2023/day15.txt").first()
    val steps = input.split(",")

    // Part 1
    var sum = 0
    for (step in steps) {
        sum += hash(step.filter { it.isLetter() })
    }
    println("The sum of the results (Part 1) is: $sum")

    // Part 2
    val boxes = MutableList(256) { mutableListOf<Lens>() }
    for (step in steps) {
        val label = step.filter { it.isLetter() }
        val boxNumber = hash(label)
        val operation = step.drop(label.length)

        when (operation.first()) {
            '-' -> boxes[boxNumber].removeIf { it.label == label }
            '=' -> {
                val fontSize = operation.drop(1).toInt()
                val lensIndex = boxes[boxNumber].indexOfFirst { it.label == label }
                if (lensIndex == -1) {
                    boxes[boxNumber].add(Lens(label, fontSize))
                } else {
                    boxes[boxNumber][lensIndex].focalLength = fontSize
                }
            }
        }
    }

    var focusingPower = 0
    boxes.forEachIndexed { boxNumber, box ->
        box.forEachIndexed { lensIndex, lens ->
            focusingPower += (boxNumber + 1) * (lensIndex + 1) * lens.focalLength
        }
    }
    println("The focusing power of the resulting lens configuration (Part 2) is: $focusingPower")
}

private fun hash(string: String): Int {
    var currentValue = 0
    for (ch in string) {
        val ascii = ch.toInt()
        currentValue += ascii
        currentValue *= 17
        currentValue %= 256
    }
    return currentValue
}
