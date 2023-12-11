package adventofcode.y2023

import adventofcode.utils.readInput

fun main() {

    val input: List<String> = readInput("2023/day10.txt")

    val chars: List<List<Char>> = input.map { (it + '0').toCharArray().toList() }

    val startingRow = chars.indexOfFirst { it.contains('S') }

    val startingColumn = chars[startingRow].indexOf('S')

    val steps: MutableList<Pair<Char, Pair<Int, Int>>> = mutableListOf('S' to (startingRow to startingColumn))

    var previousIndex: Pair<Int, Int> = startingRow to startingColumn

    var currentIndex: Pair<Int, Int> = previousIndex

    fun List<Pair<Char, Pair<Int, Int>>>.getConnectedPipes(): List<Pair<Char, Pair<Int, Int>>> {
        val validNextSteps: MutableList<Pair<Char, Pair<Int, Int>>> = mutableListOf()
        if (listOf('|', 'L', 'J', 'S').contains(steps.last().first) && listOf('|', '7', 'F', 'S').contains(get(3).first)) {
            validNextSteps.add(get(3))
        }
        if (listOf('|', '7', 'F', 'S').contains(steps.last().first) && listOf('|', 'L', 'J', 'S').contains(get(2).first)) {
            validNextSteps.add(get(2))
        }
        if (listOf('-', 'J', '7', 'S').contains(steps.last().first) && listOf('-', 'L', 'F', 'S').contains(get(1).first)) {
            validNextSteps.add(get(1))
        }
        if (listOf('-', 'L', 'F', 'S').contains(steps.last().first) && listOf('-', 'J', '7', 'S').contains(get(0).first)) {
            validNextSteps.add(get(0))
        }
        return validNextSteps
    }

    while (true) {
        val possibleNextChars = listOf(
            chars[currentIndex.first][currentIndex.second + 1] to (currentIndex.first to currentIndex.second + 1),
            chars[currentIndex.first][currentIndex.second - 1] to (currentIndex.first to currentIndex.second - 1),
            chars[currentIndex.first + 1][currentIndex.second] to (currentIndex.first + 1 to currentIndex.second),
            chars[currentIndex.first - 1][currentIndex.second] to (currentIndex.first - 1 to currentIndex.second)
        )
        val nextPipePart: Pair<Char, Pair<Int, Int>> = possibleNextChars.getConnectedPipes().first { it.second != previousIndex }
        steps.add(nextPipePart)
        previousIndex = currentIndex
        currentIndex = nextPipePart.second
        if (steps.last().first == 'S') {
            break
        }
    }

    println((steps.size - 1) / 2)

    data class CharInMap(val char: Char, var openAir: Boolean)

    val dots: MutableList<MutableList<CharInMap>> =
        (1..140).mapIndexed { rowIndex, _ ->
            (1..140).mapIndexed { columnIndex, _ -> CharInMap('.', columnIndex == 0 || columnIndex == 139 || rowIndex == 0 || rowIndex == 139) }.toMutableList()
        }.toMutableList()

    steps.forEach { dots[it.second.first][it.second.second] = CharInMap(it.first, false) }

    fun markOpenAir(charInMap: CharInMap, rowIndex: Int, columnIndex: Int) {
        if (charInMap.char == '.' && !charInMap.openAir && (dots[rowIndex + 1][columnIndex].openAir || dots[rowIndex - 1][columnIndex].openAir || dots[rowIndex][columnIndex - 1].openAir || dots[rowIndex][columnIndex + 1].openAir)) {
            charInMap.openAir = true
        }
    }

    (1..100).forEach {
        dots.forEachIndexed { rowIndex, rowOfChars ->
            rowOfChars.forEachIndexed { columnIndex, charInMap ->
                markOpenAir(charInMap, rowIndex, columnIndex)
            }
        }

        dots.reversed().forEachIndexed { rowIndex, rowOfChars ->
            rowOfChars.reversed().forEachIndexed { columnIndex, charInMap ->
                markOpenAir(charInMap, rowIndex, columnIndex)
            }
        }

        dots.reversed().forEachIndexed { rowIndex, rowOfChars ->
            rowOfChars.forEachIndexed { columnIndex, charInMap ->
                markOpenAir(charInMap, rowIndex, columnIndex)
            }
        }

        dots.forEachIndexed { rowIndex, rowOfChars ->
            rowOfChars.reversed().forEachIndexed { columnIndex, charInMap ->
                markOpenAir(charInMap, rowIndex, columnIndex)
            }
        }
    }

    println(dots.joinToString("\n") { it.map { if (it.openAir) '?' else it.char }.joinToString("") })

    println(dots.sumOf { it.count { it.char == '.' && !it.openAir } })
}
