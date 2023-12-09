package adventofcode.y2023

val input: List<String> = this::class.java.classLoader.getResource("2023/day9.txt")!!.readText().split(System.lineSeparator()).filter { it.isNotBlank() }

val baseSequences: List<List<Int>> = input.map { it.split(" ").map { it.toInt() } }

fun List<Int>.getNewLaterValueOfList(): Int {
    var difference = 0
    getListOfAllDifferences().reversed().forEach {
        difference += it.last()
    }
    return difference
}

fun List<Int>.getListOfAllDifferences(): List<List<Int>> {
    val result: MutableList<List<Int>> = mutableListOf(this)
    while (!result.last().all { it == 0 }) {
        result.add(result.last().getDifferences())
    }
    return result
}


fun List<Int>.getDifferences(): List<Int> =
    mapIndexedNotNull { index, number ->
        if (index != size - 1) {
            get(index + 1) - number
        } else {
            null
        }
    }

println(baseSequences.asSequence().map { it.getNewLaterValueOfList() }.sumOf { it })

println(baseSequences.asSequence().map { it.reversed().getNewLaterValueOfList() }.sumOf { it })

