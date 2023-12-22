package adventofcode.y2023

import adventofcode.utils.readInput

fun part1(input: List<String>) {
    val emptyMap: List<List<Char>> = input.map { it.map { it } }
    val newMap = emptyMap.map { it.map { if (it == 'S') 'O' else it }.toMutableList() }.toMutableList()
    repeat(64) {
        val newIndices: MutableList<Pair<Int, Int>> = mutableListOf()
        newMap.forEachIndexed { rowIndex, chars ->
            chars.forEachIndexed { columnIndex, char ->
                if (char == 'O') {
                    newIndices.addAll(listOf(rowIndex + 1 to columnIndex, rowIndex - 1 to columnIndex, rowIndex to columnIndex + 1, rowIndex to columnIndex - 1))
                    newMap[rowIndex][columnIndex] = '.'
                }
            }
        }
        newIndices.forEach {
            if (it.first in (0..<newMap.size) && it.second in (0..<newMap[0].size) && newMap[it.first][it.second] != '#') {
                newMap[it.first][it.second] = 'O'
            }
        }
    }
    println(newMap.sumOf { it.count { it == 'O' } })
}

fun part2(input: List<String>) {
    val emptyMap: List<List<Char>> =
        input.map { it.repeat(6).map { it } }.plus(input.map { it.repeat(6).map { it } }).plus(input.map { it.repeat(6).map { it } }).plus(input.map { it.repeat(6).map { it } }).plus(input.map { it.repeat(6).map { it } }).plus(input.map { it.repeat(6).map { it } })
    val newMap: MutableList<MutableList<Char>> = emptyMap.map { it.map { if (it == 'S') '.' else it }.toMutableList() }.toMutableList()
    newMap[327][327] = 'O'
    repeat(65 + 131 + 131 + 1) {
        val newIndices: MutableList<Pair<Int, Int>> = mutableListOf()
        newMap.forEachIndexed { rowIndex, chars ->
            chars.forEachIndexed { columnIndex, char ->
                if (char == 'O') {
                    newIndices.addAll(listOf(rowIndex + 1 to columnIndex, rowIndex - 1 to columnIndex, rowIndex to columnIndex + 1, rowIndex to columnIndex - 1))
                    newMap[rowIndex][columnIndex] = '.'
                }
            }
        }
        newIndices.forEach {
            if (it.first in (0..<newMap.size) && it.second in (0..<newMap[0].size) && newMap[it.first][it.second] != '#') {
                newMap[it.first][it.second] = 'O'
            }
        }
        if ((it+1)%131 == 65) {
            println(newMap.sumOf { it.count { it == 'O' } })
        }
    }
    println(newMap.sumOf { it.count { it == 'O' } })
}


fun main() {
    val input = readInput("2023/day21.txt")
    part1(input)
    part2(input)
    println((26501365-65)/131)
}
