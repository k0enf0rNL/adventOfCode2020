package adventofcode.y2023

import adventofcode.utils.readInput

data class RowOfSprings(val springs: String, val groupsOfBrokenSprings: List<Int>) {
    fun getAllPossibleArrangements(): Int =
        getAllArrangements().count { it.second == groupsOfBrokenSprings }

    private fun getAllArrangements(): HashSet<Pair<String, List<Int>>> {
        val resultList: MutableSet<Pair<String, List<Int>>> = mutableSetOf(springs to listOf(0))

        (0..springs.count { it == '?' }).forEach {
            val tempList: MutableSet<Pair<String, List<Int>>> = mutableSetOf()
            resultList.forEach { current ->
                listOf('.', '#').forEach { replacement ->
                    val nextValue = current.first.replaceFirst('?', replacement).let { line -> line.indexOfAny(listOf("#", "?")).let { if (it != -1) line.substring(it) else line } }
                    val replacedString = nextValue.substringBefore("?")
                    val foundGroups = replacedString.calculateGroups()
                    if (foundGroups.isStillPossible(replacedString, nextValue.length - replacedString.length)) {
                        tempList.add(nextValue to foundGroups)
                    }
                }
            }
            resultList.clear()
            resultList.addAll(tempList)
        }
        return resultList.toHashSet()
    }

    private fun List<Int>.isStillPossible(replacedString: String, remainingLength: Int): Boolean =
        if (size > groupsOfBrokenSprings.size || (groupsOfBrokenSprings.takeLast(groupsOfBrokenSprings.size - size)).sum() - remainingLength > (groupsOfBrokenSprings.size - size)) {
            false
        } else {
             mapIndexed { index, i -> groupsOfBrokenSprings[index] == i || (this.size - 1 == index && replacedString.last() == '#') }.all { it }
        }

    private fun String.calculateGroups(): List<Int> =
        Regex("([^.]+)").findAll(this).map { it.value.length }.toList()
}


// Not my code, still trying to figure out how and why this works
private val cache = hashMapOf<Pair<String, List<Int>>, Long>()
private fun count(config: String, groups: List<Int>): Long {
    if (groups.isEmpty()) return if ("#" in config) 0 else 1
    if (config.isEmpty()) return 0

    return cache.getOrPut(config to groups) {
        var result = 0L
        if (config.first() in ".?")
            result += count(config.drop(1), groups)
        if (config.first() in "#?" && groups.first() <= config.length && "." !in config.take(groups.first()) && (groups.first() == config.length || config[groups.first()] != '#'))
            result += count(config.drop(groups.first() + 1), groups.drop(1))
        result
    }
}

fun main() {
    val input = readInput("2023/day12.txt")
    val rowOfSprings = input.map { it.split(" ").let { RowOfSprings(it.first(), Regex("(\\d+)").findAll(it.last()).map { it.value.toInt() }.toList()) } }
    println(rowOfSprings.sumOf { it.getAllPossibleArrangements() })

    val rowOfSpringsPart2 = input.asSequence().map {
        it.split(" ").let { line ->
            RowOfSprings(
                (1..5).joinToString("?") { line.first() },
                (1..5).joinToString(",") { line.last() }.let { Regex("(\\d+)").findAll(it).map { it.value.toInt() }.toList() }
            )
        }
    }
    println(rowOfSpringsPart2.sumOf { count(it.springs, it.groupsOfBrokenSprings) })
    println(cache)
    println(cache.size)
}
