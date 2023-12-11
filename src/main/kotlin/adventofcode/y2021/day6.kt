import adventofcode.utils.readInput

fun main() {

    val input: List<Int> = readInput("2021/day6.txt")
        .first().split(",")
        .filter { it.isNotBlank() }
        .map { it.toInt() }
        .sorted()

    fun calculateFishPool(days: Int): Long {
        val ages = mutableMapOf(0 to 0L, 1 to 0L, 2 to 0L, 3 to 0L, 4 to 0L, 5 to 0L, 6 to 0L, 7 to 0L, 8 to 0L)
        input.groupingBy { it }.eachCount().forEach { ages[it.key] = it.value.toLong() }

        (1..days).forEach { _ ->
            val zeroDayFish = ages[0]!!

            ages.keys.forEach {
                ages[it] = when (it) {
                    6 -> {
                        ages[it + 1]!! + zeroDayFish
                    }

                    8 -> {
                        zeroDayFish
                    }

                    else -> {
                        ages[it + 1]!!
                    }
                }
            }
        }
        return ages.values.sum()
    }

// Part 1
    println("Part 1: ${calculateFishPool(80)}")

// Part 2
    println("Part 2: ${calculateFishPool(256)}")
}
