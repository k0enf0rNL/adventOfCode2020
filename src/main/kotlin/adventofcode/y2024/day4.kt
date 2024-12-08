package adventofcode.y2024

import adventofcode.utils.readInput

fun main() {
    val rows = readInput("2024/day4/input.txt")
    val part1 = rows.mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, value ->
            if (value == 'X') {
                findVerticalUp(rows, rowIndex, columnIndex) +
                    findVerticalDown(rows, rowIndex, columnIndex) +
                    findHorizontalRight(rows, rowIndex, columnIndex) +
                    findHorizontalLeft(rows, rowIndex, columnIndex) +
                    findDiagonalLeftUpToRightDown(rows, rowIndex, columnIndex) +
                    findDiagonalRightUpToLeftDown(rows, rowIndex, columnIndex) +
                    findDiagonalLeftDownToRightUp(rows, rowIndex, columnIndex) +
                    findDiagonalRightDownToLeftUp(rows, rowIndex, columnIndex)
            } else {
                0
            }
        }.sum()
    }.sum()
    println("Part 1: $part1")

    val part2 = rows.mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, value ->
            if (value == 'A') {
                if (findDiagonalLeftUpToRightDown(rows, rowIndex-2, columnIndex-2) +
                    findDiagonalRightUpToLeftDown(rows, rowIndex-2, columnIndex+2) +
                    findDiagonalLeftDownToRightUp(rows, rowIndex+2, columnIndex-2) +
                    findDiagonalRightDownToLeftUp(rows, rowIndex+2, columnIndex+2) == 2) {
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

fun findDiagonalLeftUpToRightDown(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (curRowIndex + 1 < 0 || curColumnIndex + 1 < 0 || rows.size - 1 < curRowIndex + 3 || rows.first().length - 1 < curColumnIndex + 3) {
        0
    } else if (rows[curRowIndex + 1][curColumnIndex + 1] == 'M' && rows[curRowIndex + 2][curColumnIndex + 2] == 'A' && rows[curRowIndex + 3][curColumnIndex + 3] == 'S') {
        1
    } else {
        0
    }

fun findDiagonalRightUpToLeftDown(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (curRowIndex + 1 < 0 || rows.first().length - 1 < curColumnIndex - 1 || rows.size - 1 < curRowIndex + 3 || 0 > curColumnIndex - 3) {
        0
    } else if (rows[curRowIndex + 1][curColumnIndex - 1] == 'M' && rows[curRowIndex + 2][curColumnIndex - 2] == 'A' && rows[curRowIndex + 3][curColumnIndex - 3] == 'S') {
        1
    } else {
        0
    }

fun findDiagonalLeftDownToRightUp(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (rows.size - 1 < curRowIndex - 1 || curColumnIndex + 1 < 0 || 0 > curRowIndex - 3 || rows.first().length - 1 < curColumnIndex + 3) {
        0
    } else if (rows[curRowIndex - 1][curColumnIndex + 1] == 'M' && rows[curRowIndex - 2][curColumnIndex + 2] == 'A' && rows[curRowIndex - 3][curColumnIndex + 3] == 'S') {
        1
    } else {
        0
    }

fun findDiagonalRightDownToLeftUp(rows: List<String>, curRowIndex: Int, curColumnIndex: Int): Int =
    if (rows.size - 1 < curRowIndex - 1 || rows.first().length - 1 < curColumnIndex - 1 || 0 > curRowIndex - 3 || 0 > curColumnIndex - 3) {
        0
    } else if (rows[curRowIndex - 1][curColumnIndex - 1] == 'M' && rows[curRowIndex - 2][curColumnIndex - 2] == 'A' && rows[curRowIndex - 3][curColumnIndex - 3] == 'S') {
        1
    } else {
        0
    }
