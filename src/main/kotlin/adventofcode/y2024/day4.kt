package adventofcode.y2024

import adventofcode.utils.println
import adventofcode.utils.readInput

fun main() {
    val rows = readInput("2024/day4.txt")
    val part1 = rows.mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, value ->
            if (value == 'X') {
                findVerticalUp(rows, rowIndex, columnIndex) +
                    findVerticalDown(rows, rowIndex, columnIndex) +
                    findHorizontalRight(rows, rowIndex, columnIndex) +
                    findHorizontalLeft(rows, rowIndex, columnIndex) +
                    findDiagonalRightDown(rows, rowIndex, columnIndex) +
                    findDiagonalLeftDown(rows, rowIndex, columnIndex) +
                    findDiagonalRightUp(rows, rowIndex, columnIndex) +
                    findDiagonalLeftUp(rows, rowIndex, columnIndex)
            } else {
                0
            }
        }.sum()
    }.sum()
    println("Part 1: $part1")

    val part2 = rows.mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, value ->
            if (value == 'A') {
                if (findDiagonalLeftUpToRightDownPart2(rows, rowIndex-2, columnIndex-2) +
                    findDiagonalRightUpToLeftDownPart2(rows, rowIndex-2, columnIndex+2) +
                    findDiagonalLeftDownToRightUpPart2(rows, rowIndex+2, columnIndex-2) +
                    findDiagonalRightDownToLeftUpPart2(rows, rowIndex+2, columnIndex+2) == 2) {
                    1
                } else {
                    0
                }
            } else {
                0
            }
        }.sum()
    }.sum()
    println("Part 2: $part2")
}

fun findVerticalUp(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (0 > curRowIndex - 3) {
        0
    } else if (rows[curRowIndex - 1][curColumnIndex] == 'M' && rows[curRowIndex - 2][curColumnIndex] == 'A' && rows[curRowIndex - 3][curColumnIndex] == 'S') {
        1
    } else {
        0
    }

fun findVerticalDown(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (rows.size - 1 < curRowIndex + 3) {
        0
    } else if (rows[curRowIndex + 1][curColumnIndex] == 'M' && rows[curRowIndex + 2][curColumnIndex] == 'A' && rows[curRowIndex + 3][curColumnIndex] == 'S') {
        1
    } else {
        0
    }

fun findHorizontalRight(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (rows.first().length - 1 < curColumnIndex + 3) {
        0
    } else if (rows[curRowIndex][curColumnIndex + 1] == 'M' && rows[curRowIndex][curColumnIndex + 2] == 'A' && rows[curRowIndex][curColumnIndex + 3] == 'S') {
        1
    } else {
        0
    }

fun findHorizontalLeft(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (0 > curColumnIndex - 3) {
        0
    } else if (rows[curRowIndex][curColumnIndex - 1] == 'M' && rows[curRowIndex][curColumnIndex - 2] == 'A' && rows[curRowIndex][curColumnIndex - 3] == 'S') {
        1
    } else {
        0
    }

fun findDiagonalRightDown(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (rows.size - 1 < curRowIndex + 3 || rows.first().length - 1 < curColumnIndex + 3) {
        0
    } else if (rows[curRowIndex + 1][curColumnIndex + 1] == 'M' && rows[curRowIndex + 2][curColumnIndex + 2] == 'A' && rows[curRowIndex + 3][curColumnIndex + 3] == 'S') {
        1
    } else {
        0
    }

fun findDiagonalLeftDown(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (rows.size - 1 < curRowIndex + 3 || 0 > curColumnIndex - 3) {
        0
    } else if (rows[curRowIndex + 1][curColumnIndex - 1] == 'M' && rows[curRowIndex + 2][curColumnIndex - 2] == 'A' && rows[curRowIndex + 3][curColumnIndex - 3] == 'S') {
        1
    } else {
        0
    }

fun findDiagonalRightUp(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (0 > curRowIndex - 3 || rows.first().length - 1 < curColumnIndex + 3) {
        0
    } else if (rows[curRowIndex - 1][curColumnIndex + 1] == 'M' && rows[curRowIndex - 2][curColumnIndex + 2] == 'A' && rows[curRowIndex - 3][curColumnIndex + 3] == 'S') {
        1
    } else {
        0
    }

fun findDiagonalLeftUp(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (0 > curRowIndex - 3 || 0 > curColumnIndex - 3) {
        0
    } else if (rows[curRowIndex - 1][curColumnIndex - 1] == 'M' && rows[curRowIndex - 2][curColumnIndex - 2] == 'A' && rows[curRowIndex - 3][curColumnIndex - 3] == 'S') {
        1
    } else {
        0
    }

fun findDiagonalLeftUpToRightDownPart2(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (curRowIndex + 1 < 0 || curColumnIndex + 1 < 0 || rows.size - 1 < curRowIndex + 3 || rows.first().length - 1 < curColumnIndex + 3) {
        0
    } else if (rows[curRowIndex + 1][curColumnIndex + 1] == 'M' && rows[curRowIndex + 2][curColumnIndex + 2] == 'A' && rows[curRowIndex + 3][curColumnIndex + 3] == 'S') {
        1
    } else {
        0
    }

fun findDiagonalRightUpToLeftDownPart2(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (curRowIndex + 1 < 0 || rows.first().length - 1 < curColumnIndex - 1 || rows.size - 1 < curRowIndex + 3 || 0 > curColumnIndex - 3) {
        0
    } else if (rows[curRowIndex + 1][curColumnIndex - 1] == 'M' && rows[curRowIndex + 2][curColumnIndex - 2] == 'A' && rows[curRowIndex + 3][curColumnIndex - 3] == 'S') {
        1
    } else {
        0
    }

fun findDiagonalLeftDownToRightUpPart2(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (rows.size - 1 < curRowIndex - 1 || curColumnIndex + 1 < 0 || 0 > curRowIndex - 3 || rows.first().length - 1 < curColumnIndex + 3) {
        0
    } else if (rows[curRowIndex - 1][curColumnIndex + 1] == 'M' && rows[curRowIndex - 2][curColumnIndex + 2] == 'A' && rows[curRowIndex - 3][curColumnIndex + 3] == 'S') {
        1
    } else {
        0
    }

fun findDiagonalRightDownToLeftUpPart2(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (rows.size - 1 < curRowIndex - 1 || rows.first().length - 1 < curColumnIndex - 1 || 0 > curRowIndex - 3 || 0 > curColumnIndex - 3) {
        0
    } else if (rows[curRowIndex - 1][curColumnIndex - 1] == 'M' && rows[curRowIndex - 2][curColumnIndex - 2] == 'A' && rows[curRowIndex - 3][curColumnIndex - 3] == 'S') {
        1
    } else {
        0
    }
