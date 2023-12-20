package adventofcode.y2023

import adventofcode.utils.println
import adventofcode.utils.readInput
import adventofcode.utils.split
import kotlin.math.max
import kotlin.math.min

data class Condition(val char: Char, val operation: Char, val value: Int, val resultIfConditionSatisfied: String) {
    fun evaluateCondition(part: Part): String? =
        when (char) {
            'x' -> checkSpecificValue(part.x)
            'm' -> checkSpecificValue(part.m)
            'a' -> checkSpecificValue(part.a)
            's' -> checkSpecificValue(part.s)
            else -> throw IllegalArgumentException("$char is geen geldige character")
        }

    private fun checkSpecificValue(valueToCheck: Int) =
        if (operation == '>' && valueToCheck > value) {
            resultIfConditionSatisfied
        } else if (operation == '<' && valueToCheck < value) {
            resultIfConditionSatisfied
        } else {
            null
        }

    fun getIntRangeToGetResult(): Pair<Char, IntRange> =
        if (operation == '>') {
            char to value + 1..4000
        } else {
            char to 1..<value
        }

    fun getIntRangeToGetNull(): Pair<Char, IntRange> =
        if (operation == '<') {
            char to value..4000
        } else {
            char to 1..value
        }
}

data class Rule(val key: String, val conditions: List<Condition>, val default: String) {
    fun getPartResult(rules: HashMap<String, Rule>, part: Part): Pair<Part, String> =
        when (val result = conditions.firstNotNullOfOrNull { it.evaluateCondition(part) } ?: default) {
            "R" -> part to "R"
            "A" -> part to "A"
            else -> rules[result]?.getPartResult(rules, part) ?: throw IllegalArgumentException("$result matcht niet aan een andere rule")
        }

    fun getPartCombinationsThatLeadToDesiredResult(rules: HashMap<String, Rule>, desiredResult: String, condition: Condition?): RangePart {
        val conditionRanges: MutableMap<Char, List<IntRange>> = mutableMapOf()
        if (default == desiredResult && condition == null) {
            conditionRanges.putAll(conditions.map { it.getIntRangeToGetNull() }.groupBy { it.first }
                .map { it.key to it.value.map { it.second } }.toMap())
        } else {
            val conditionsToNegate = conditions.split { it == condition }.firstOrNull() ?: emptyList()
            val conditionToMatch = conditions.first { if (condition != null) it == condition else it.resultIfConditionSatisfied == desiredResult }.getIntRangeToGetResult()
            conditionRanges.putAll(conditionsToNegate.asSequence().map { it.getIntRangeToGetNull() }.plus(listOf(conditionToMatch)).groupBy { it.first }.map { it.key to it.value.map { it.second } }.toList().toMap())
        }
        val newlyCreatedRangePart = RangePart(
            xRange = conditionRanges['x']?.reduce { acc, intRange -> acc.intersect(intRange) } ?: 1..4000,
            mRange = conditionRanges['m']?.reduce { acc, intRange -> acc.intersect(intRange) } ?: 1..4000,
            aRange = conditionRanges['a']?.reduce { acc, intRange -> acc.intersect(intRange) } ?: 1..4000,
            sRange = conditionRanges['s']?.reduce { acc, intRange -> acc.intersect(intRange) } ?: 1..4000
        )
        return newlyCreatedRangePart.intersect(rules.filter { it.value.hasPossibleResult(key) }.map { it.value.recursivePartCombinationsThatLeadToDesiredResult(rules, key) }
            .reduce { acc, rangePart -> acc.intersect(rangePart) })
    }

    private fun recursivePartCombinationsThatLeadToDesiredResult(rules: HashMap<String, Rule>, desiredResult: String): RangePart {
        val conditionRanges: MutableMap<Char, List<IntRange>> = mutableMapOf()
        if (default == desiredResult) {
            conditionRanges.putAll(conditions.map { it.getIntRangeToGetNull() }.groupBy { it.first }
                .map { it.key to it.value.map { it.second } }.toMap())
        } else {
            val conditionsToNegate = conditions.split { it.resultIfConditionSatisfied == desiredResult }.firstOrNull() ?: emptyList()
            val conditionToMatch = conditions.first { it.resultIfConditionSatisfied == desiredResult }.getIntRangeToGetResult()
            conditionRanges.putAll(conditionsToNegate.asSequence().map { it.getIntRangeToGetNull() }.plus(listOf(conditionToMatch)).groupBy { it.first }.map { it.key to it.value.map { it.second } }.toList().toMap())
        }
        val newlyCreatedRangePart = RangePart(
            xRange = conditionRanges['x']?.reduce { acc, intRange -> acc.intersect(intRange) } ?: 1..4000,
            mRange = conditionRanges['m']?.reduce { acc, intRange -> acc.intersect(intRange) } ?: 1..4000,
            aRange = conditionRanges['a']?.reduce { acc, intRange -> acc.intersect(intRange) } ?: 1..4000,
            sRange = conditionRanges['s']?.reduce { acc, intRange -> acc.intersect(intRange) } ?: 1..4000
        )
        if (key != "in") {
            return newlyCreatedRangePart.intersect(rules.filter { it.value.hasPossibleResult(key) }.map { it.value.recursivePartCombinationsThatLeadToDesiredResult(rules, key) }
                .reduce { acc, rangePart -> acc.intersect(rangePart) })
        }
        return newlyCreatedRangePart
    }

    private fun hasPossibleResult(searchKey: String): Boolean =
        default == searchKey || conditions.any { it.resultIfConditionSatisfied == searchKey }
}

data class Part(val x: Int, val m: Int, val a: Int, val s: Int)

data class RangePart(var xRange: IntRange = 1..4000, var mRange: IntRange = 1..4000, var aRange: IntRange = 1..4000, var sRange: IntRange = 1..4000) {

    fun intersect(rangePart: RangePart): RangePart {
        xRange = xRange.intersect(rangePart.xRange)
        mRange = mRange.intersect(rangePart.mRange)
        aRange = aRange.intersect(rangePart.aRange)
        sRange = sRange.intersect(rangePart.sRange)
        return this
    }

    fun totalDistinctCombinations(): Long =
        xRange.count().toLong() * mRange.count().toLong() * aRange.count().toLong() * sRange.count().toLong()
}

fun IntRange.intersect(other: IntRange): IntRange =
    max(first, other.first)..min(last, other.last)

fun main() {
    val input = readInput("2023/day19.txt").split { it.isBlank() }
    val rules: HashMap<String, Rule> = input.first().associate { line ->
        Regex("(\\w+)").findAll(line).first().value to line.substringAfter('{').dropLast(1).split(",")
            .let {
                Rule(
                    Regex("(\\w+)").findAll(line).first().value,
                    it.filter { it.contains(':') }.map { Condition(it[0], it[1], Regex("(\\d+)").findAll(it).first().value.toInt(), it.substringAfter(':')) },
                    it.last()
                )
            }
    }.let { HashMap(it) }
    val parts = input.last().map { Regex("(\\d+)").findAll(it).toList().map { it.value.toInt() }.let { Part(it[0], it[1], it[2], it[3]) } }
    val inputRule = rules["in"]!!
    val partsResult = parts.map { inputRule.getPartResult(rules, it) }
    println(partsResult.filter { it.second == "A" }.sumOf { it.first.x + it.first.m + it.first.a + it.first.s })

    // Start bij iedere A en bepaal waar je input aan moet voldoen om bij die A te komen
    val rulesThatCanResultInAAsDefault = rules.filter { it.value.default == "A" }
    val rulesThatCanResultInA = rules.flatMap { rule -> rule.value.conditions.filter { it.resultIfConditionSatisfied == "A" }.map { rule to it } }

    val rangesAConditions = rulesThatCanResultInA.map {
        val result = it.first.value.getPartCombinationsThatLeadToDesiredResult(
            rules,
            "A",
            it.second
        )
        result.totalDistinctCombinations() to (it.first.key to result)
    }
    val rangesADefault = rulesThatCanResultInAAsDefault.map {
        val result = it.value.getPartCombinationsThatLeadToDesiredResult(
            rules,
            "A",
            null
        )
        result.totalDistinctCombinations() to (it.key to result)
    }
    println(rulesThatCanResultInAAsDefault.values.joinToString("\n"))
    println(rulesThatCanResultInA.joinToString("\n"))
    println((rangesADefault + rangesAConditions).sortedBy { it.second.first }.map { "(${it.second.first}, [X = ${it.second.second.xRange}, M = ${it.second.second.mRange}, A = ${it.second.second.aRange}, S = ${it.second.second.sRange}])" }.joinToString("\n"))
    println(rangesADefault.sumOf { it.first } + rangesAConditions.sumOf {
        it.first
    })
    // 179895751051988 too high

    println(rulesThatCanResultInA.size + rulesThatCanResultInAAsDefault.size)

}
