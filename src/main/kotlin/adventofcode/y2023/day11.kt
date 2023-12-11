package adventofcode.y2023

import adventofcode.utils.readInput
import adventofcode.utils.transpose
import kotlin.math.abs

sealed class SkyChar(open var rowIndex: Int, open var columnIndex: Int) {
    data class Galaxy(override var rowIndex: Int, override var columnIndex: Int) : SkyChar(rowIndex, columnIndex)
    data class Space(override var rowIndex: Int, override var columnIndex: Int) : SkyChar(rowIndex, columnIndex)
}

fun main() {

    fun List<List<Char>>.expandUniverse(): List<List<Char>> {
        val expandedUniverse: MutableList<List<Char>> = mutableListOf()

        for (row in this) {
            if (row.all { it == '.' }) {
                expandedUniverse.add(row)
            }
            expandedUniverse.add(row)
        }

        return expandedUniverse
    }

    fun SkyChar.Galaxy.getDistanceToOtherGalaxies(galaxyList: List<SkyChar.Galaxy>, currentIndex: Int): Long {
        if (currentIndex == galaxyList.size - 1) {
            return 0
        }
        return galaxyList.subList(currentIndex + 1, galaxyList.size).sumOf { (abs(this.rowIndex - it.rowIndex) + abs(this.columnIndex - it.columnIndex)).toLong() }
    }

    val input: List<String> = readInput("2023/day11.txt")

    val universe: List<List<Char>> = input.map { it.map { it } }

    val expandedUniverse: List<List<SkyChar>> = universe.expandUniverse().transpose().expandUniverse()
        .mapIndexed { rowIndex, chars ->
            chars.mapIndexed { columnIndex, char ->
                if (char == '#') SkyChar.Galaxy(rowIndex, columnIndex) else SkyChar.Space(
                    rowIndex,
                    columnIndex
                )
            }
        }

    val galaxies: List<SkyChar.Galaxy> = expandedUniverse.flatMap { it.filterIsInstance<SkyChar.Galaxy>() }

//Part 1
    println(galaxies.mapIndexed { index, galaxy -> galaxy.getDistanceToOtherGalaxies(galaxies, index) }.sum())

    fun List<List<SkyChar>>.expandUniversePart2(expand: (SkyChar, Int) -> Unit): List<List<SkyChar>> {
        var timesExpanded = 0
        forEach { skyChars ->
            skyChars.forEach { galaxy ->
                expand(galaxy, timesExpanded)
            }
            if (skyChars.all { it is SkyChar.Space }) {
                timesExpanded++
            }
        }
        return this
    }

    val galaxiesFurtherExpanded = universe
        .mapIndexed { rowIndex, chars ->
            chars.mapIndexed { columnIndex, char ->
                if (char == '#') SkyChar.Galaxy(rowIndex, columnIndex) else SkyChar.Space(
                    rowIndex,
                    columnIndex
                )
            }
        }
        .expandUniversePart2 { skyChar, timesExpanded -> skyChar.rowIndex += timesExpanded * 999_999 }
        .transpose()
        .expandUniversePart2 { skyChar, timesExpanded -> skyChar.columnIndex += timesExpanded * 999_999 }
        .flatMap { it.filterIsInstance<SkyChar.Galaxy>() }

// Part 2
    println(galaxiesFurtherExpanded.mapIndexed { index, galaxy -> galaxy.getDistanceToOtherGalaxies(galaxiesFurtherExpanded, index) }.sum())
}
