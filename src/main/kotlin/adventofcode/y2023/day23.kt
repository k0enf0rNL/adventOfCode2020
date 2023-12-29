package adventofcode.y2023

import adventofcode.utils.JunctionConnection
import adventofcode.utils.LongestPathNode
import adventofcode.utils.longestPathUsingOnlyJunctions
import adventofcode.utils.println
import adventofcode.utils.readInput

private fun printMatrixWithPath(matrix: List<List<Char>>, path: Set<JunctionConnection>) {
    matrix.mapIndexed { rowIndex, chars ->
        chars.mapIndexed { columnIndex, char ->
            path.firstOrNull { (it.fromJunction.row == rowIndex && it.fromJunction.col == columnIndex) || (it.toJunction.row == rowIndex && it.toJunction.col == columnIndex) }?.let { "O" } ?: char
        }.joinToString("")
    }.joinToString("\n").println()
}

fun main() {
    val input = readInput("2023/day23.txt")
    val matrix = input.map { it.map { it } }

    val longestPathWithSlopes =
        longestPathUsingOnlyJunctions(matrix, LongestPathNode(0, 1), LongestPathNode(matrix.size - 1, matrix.size - 2)) { nextNode: Pair<Char, LongestPathNode>, currentNode: LongestPathNode ->
            nextNode.first == '.' || (nextNode.first == '>' && nextNode.second.col == currentNode.col + 1) || (nextNode.first == 'v' && nextNode.second.row == currentNode.row + 1)
        }
    println(longestPathWithSlopes.joinToString("\n"))
    printMatrixWithPath(matrix, longestPathWithSlopes)
    println("The length of the longest path with slopes is: ${longestPathWithSlopes.sumOf { it.steps }}")

    val longestPathWithoutSlopes =
        longestPathUsingOnlyJunctions(matrix, LongestPathNode(0, 1), LongestPathNode(matrix.size - 1, matrix.size - 2)) { nextNode: Pair<Char, LongestPathNode>, currentNode: LongestPathNode ->
            nextNode.first != '#'
        }
    println(longestPathWithoutSlopes.joinToString("\n"))
    printMatrixWithPath(matrix, longestPathWithoutSlopes)
    println("The length of the longest path without slopes is: ${longestPathWithoutSlopes.sumOf { it.steps }}") // 6294 too low
}
