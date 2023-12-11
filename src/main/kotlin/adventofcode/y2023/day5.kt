package adventofcode.y2023

import adventofcode.utils.readInput

sealed class InOrOutOfRange {
    class InRange(val correspondingValue: Long, val remainingRangeLength: Long?) : InOrOutOfRange()
    data object OutOfRange : InOrOutOfRange()
}

fun main() {

    fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> =
        fold(mutableListOf(mutableListOf<T>())) { acc, t ->
            if (predicate(t)) acc.add(mutableListOf())
            else acc.last().add(t)
            acc
        }.filterNot { it.isEmpty() }

    data class SeedPartOne(val number: Long)

    data class ThreeValues(val first: Long, val second: Long, val third: Long) {
        fun isInRange(value: Long): InOrOutOfRange =
            if (value <= second + third - 1 && value >= second) {
                InOrOutOfRange.InRange(value - second + first, third - (value - second))
            } else {
                InOrOutOfRange.OutOfRange
            }
    }

    data class CorrespondingValueWithRange(val correspondingValue: Long, val remainingRange: Long?)

    data class ThreeValueMaps(val threeValues: List<ThreeValues>) {
        fun getCorrespondingValues(value: Long): CorrespondingValueWithRange =
            threeValues.map { it.isInRange(value) }
                .filterIsInstance<InOrOutOfRange.InRange>()
                .map { CorrespondingValueWithRange(it.correspondingValue, it.remainingRangeLength) }.firstOrNull()
                ?: CorrespondingValueWithRange(value, threeValues.filter { it.second > value }.minByOrNull { it.second }?.let { it.second - value })

    }

    val input: List<List<String>> = readInput("2023/day5.txt").split { it.isBlank() }

    fun getThreeValueMapsFromInput(name: String): ThreeValueMaps =
        input.first { it.first().contains(name) }.drop(1).map {
            Regex("(\\d+)").findAll(it).map { it.value.toLong() }.toList().let { ThreeValues(it[0], it[1], it[2]) }
        }.let { ThreeValueMaps(it) }

    val seedsPartOne: List<SeedPartOne> = input.first { it.first().contains("seeds:") }
        .let { Regex("(\\d+)").findAll(it.first()).map { SeedPartOne(it.value.toLong()) }.toList() }
    val seedToSoilMap: ThreeValueMaps = getThreeValueMapsFromInput("seed-to-soil map:")
    val soilToFertilizerMap: ThreeValueMaps = getThreeValueMapsFromInput("soil-to-fertilizer map:")
    val fertilizerToWaterMap: ThreeValueMaps = getThreeValueMapsFromInput("fertilizer-to-water map:")
    val waterToLightMap: ThreeValueMaps = getThreeValueMapsFromInput("water-to-light map:")
    val lightToTemperatureMap: ThreeValueMaps = getThreeValueMapsFromInput("light-to-temperature map:")
    val temperatureToHumidityMap: ThreeValueMaps = getThreeValueMapsFromInput("temperature-to-humidity map:")
    val humidityToLocationMap: ThreeValueMaps = getThreeValueMapsFromInput("humidity-to-location map:")

    fun calculatePartOne(): List<CorrespondingValueWithRange> {
        val seedToSoilValues = seedsPartOne.map { seed -> seedToSoilMap.getCorrespondingValues(seed.number) }
        val soilToFertilizerValues = seedToSoilValues.map { value -> soilToFertilizerMap.getCorrespondingValues(value.correspondingValue) }
        val fertilizerToWaterValues = soilToFertilizerValues.map { value -> fertilizerToWaterMap.getCorrespondingValues(value.correspondingValue) }
        val waterToLightValues = fertilizerToWaterValues.map { value -> waterToLightMap.getCorrespondingValues(value.correspondingValue) }
        val lightToTemperatureValues = waterToLightValues.map { value -> lightToTemperatureMap.getCorrespondingValues(value.correspondingValue) }
        val temperatureToHumidityValues = lightToTemperatureValues.map { value -> temperatureToHumidityMap.getCorrespondingValues(value.correspondingValue) }
        return temperatureToHumidityValues.map { value -> humidityToLocationMap.getCorrespondingValues(value.correspondingValue) }
    }

    println(calculatePartOne().minBy { it.correspondingValue }.correspondingValue)

    data class SeedPartTwo(val number: LongRange, var lowestLocation: Long?)

    val seedsPartTwo: List<SeedPartTwo> = input.first { it.first().contains("seeds:") }
        .let { Regex("(\\d+)").findAll(it.first()).chunked(2).map { SeedPartTwo((it[0].value.toLong()..(it[0].value.toLong() + it[1].value.toLong())), null) }.toList() }

    val lowestLocations = seedsPartTwo.map { seedPartTwo ->
        var valueToTry: Long = seedPartTwo.number.first
        while (valueToTry <= seedPartTwo.number.last) {
            val seedToSoilValue = seedToSoilMap.getCorrespondingValues(valueToTry)
            val soilToFertilizerValue = soilToFertilizerMap.getCorrespondingValues(seedToSoilValue.correspondingValue)
            val fertilizerToWaterValue = fertilizerToWaterMap.getCorrespondingValues(soilToFertilizerValue.correspondingValue)
            val waterToLightValue = waterToLightMap.getCorrespondingValues(fertilizerToWaterValue.correspondingValue)
            val lightToTemperatureValue = lightToTemperatureMap.getCorrespondingValues(waterToLightValue.correspondingValue)
            val temperatureToHumidityValue = temperatureToHumidityMap.getCorrespondingValues(lightToTemperatureValue.correspondingValue)
            val humidityToLocationValue = humidityToLocationMap.getCorrespondingValues(temperatureToHumidityValue.correspondingValue)
            listOf(seedToSoilValue, soilToFertilizerValue, fertilizerToWaterValue, waterToLightValue, lightToTemperatureValue, temperatureToHumidityValue, humidityToLocationValue)
                .filter { it.remainingRange != null }
                .minByOrNull { it.remainingRange!! }
                ?.let { valueToTry += it.remainingRange!! }
            seedPartTwo.lowestLocation = listOf(humidityToLocationValue.correspondingValue, seedPartTwo.lowestLocation).filterNotNull().minBy { it }
        }
        seedPartTwo.lowestLocation
    }

    println(lowestLocations.minBy { it ?: -1 })
}
