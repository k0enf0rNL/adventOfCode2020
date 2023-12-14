package adventofcode.y2023

import adventofcode.utils.readInput
import adventofcode.utils.transpose

fun main() {
    val input = readInput("2023/day14.txt")
    val movedToNorth: List<List<Char>> = moveBouldersUp(input.map { it.toCharArray().toList() })
    println(movedToNorth.mapIndexed { index, chars -> chars.count { it == 'O' } * (movedToNorth.size - index) }.sum())

    var resultPart2 = input.map { it.toCharArray().toList() }
    val results: HashMap<Int, MutableList<Int>> = hashMapOf()
    (1..1_000_000_000).forEach { rotation ->
        resultPart2 = moveBouldersUp(resultPart2)

        resultPart2 = moveBouldersUp(resultPart2.transpose()).transpose()

        resultPart2 = moveBouldersUp(resultPart2.reversed()).reversed()

        resultPart2 = moveBouldersUp(resultPart2.transpose().reversed()).reversed().transpose()

        if (rotation % 1_000 == 0) {
            results.getOrPut(resultPart2.mapIndexed { index, chars -> chars.count { it == 'O' } * (resultPart2.size - index) }.sum()) { mutableListOf() }.add(rotation)
            println(results)
        }
    }
    println(resultPart2.mapIndexed { index, chars -> chars.count { it == 'O' } * (resultPart2.size - index) }.sum()) // 105610 too high 105605 too low
}

private fun moveBouldersUp(input: List<List<Char>>): List<List<Char>> {
    val transposedMutableListOfChars: List<List<Char>> = input.transpose()
    val movedToNorth: List<List<Char>> = transposedMutableListOfChars.map { line ->
        val result: MutableList<Char> = line.toMutableList()
        (0..<result.size).map { index ->
            if (result[index] == '.') {
                val nextCharList = result.subList(index, result.size)
                val nextCharNotADot = nextCharList.firstOrNull { it != '.' }
                if (nextCharNotADot != null && nextCharNotADot == 'O') {
                    result[index + (nextCharList.indexOf(nextCharNotADot))] = '.'
                    result[index] = 'O'
                }
            }
        }
        result
    }
    return movedToNorth.transpose()
}
