package day5

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import utils.InputReader.readFileAsList


private lateinit var seeds: List<Long>
private lateinit var seedToSoil: MappyList
private lateinit var soilToFertilizer: MappyList
private lateinit var fertilizerToWater: MappyList
private lateinit var waterToLight: MappyList
private lateinit var lightToTemperature: MappyList
private lateinit var temperatureToHumidity: MappyList
private lateinit var humidityToLocation: MappyList
private lateinit var seedRanges: MutableList<LongRange>

fun main() {
    val input = readFileAsList("Day5.txt")

    println("Part 1: ${part1(input)}, Part 2: ${part2(input)}")
}

fun part1(input: List<String>): Long {
    parseData(input)
    println("seeds:                   $seeds")
    println("seed to soil:            $seedToSoil")
    println("soil to fertilizer:      $soilToFertilizer")
    println("fertilizer to water:     $fertilizerToWater")
    println("water to light:          $waterToLight")
    println("light to temperature:    $lightToTemperature")
    println("temperature to humidity: $temperatureToHumidity")
    println("humidity to location:    $humidityToLocation")
    return seeds.map {
        val soil = seedToSoil.getDestination(it)
        val fertilizer = soilToFertilizer.getDestination(soil)
        val water = fertilizerToWater.getDestination(fertilizer)
        val light = waterToLight.getDestination(water)
        val temperature = lightToTemperature.getDestination(light)
        val humidity = temperatureToHumidity.getDestination(temperature)
        humidityToLocation.getDestination(humidity)
    }.min()
}

fun part2(input: List<String>): Long {
    parseData(input)
    seedRanges = mutableListOf()
    for (i in seeds.indices step 2) {
        seedRanges.add(LongRange(seeds[i], seeds[i] + seeds[i + 1] - 1))
    }

    println("seeds:                   $seeds")
    println("seed ranges:             $seedRanges")
    println("seed to soil:            $seedToSoil")
    println("soil to fertilizer:      $soilToFertilizer")
    println("fertilizer to water:     $fertilizerToWater")
    println("water to light:          $waterToLight")
    println("light to temperature:    $lightToTemperature")
    println("temperature to humidity: $temperatureToHumidity")
    println("humidity to location:    $humidityToLocation")

    return runBlocking {
        val bruteForce = withContext(Dispatchers.IO) {
            seedRanges.mapAsyncIndexed { index, range ->
                println("SeedRange: $index of ${seedRanges.size}")
                range.minOf { seed ->
                    val soil = seedToSoil.getDestination(seed)
                    val fertilizer = soilToFertilizer.getDestination(soil)
                    val water = fertilizerToWater.getDestination(fertilizer)
                    val light = waterToLight.getDestination(water)
                    val temperature = lightToTemperature.getDestination(light)
                    val humidity = temperatureToHumidity.getDestination(temperature)
                    humidityToLocation.getDestination(humidity)
                }
            }.min()
        }
        return@runBlocking bruteForce
    }
//    return humidityToLocation.sortedBy { it.destination }.firstNotNullOf {
//        search(it)
//    }
}

private suspend fun <T, R> List<T>.mapAsyncIndexed(
    mapper: suspend (Int, T) -> R
): List<R> = coroutineScope { mapIndexed { index, it -> async { mapper(index, it) } }.awaitAll() }

fun search(mapping: RangeMap): Long? {
    for (i in mapping.destination..<mapping.destination + mapping.range) {
        println("location: $i")
        val humidity = mapping.getSourceForDestination(i)!!
        println("\thumidity: $humidity")
        temperatureToHumidity.getSource(humidity)?.let { temperature ->
            println("\t\ttemperature: $temperature")
            lightToTemperature.getSource(temperature)?.let { light ->
                println("\t\t\tlight: $light")
                waterToLight.getSource(light)?.let { water ->
                    println("\t\t\t\twater: $water")
                    fertilizerToWater.getSource(water)?.let { fertilizer ->
                        println("\t\t\t\t\tfertilizer: $fertilizer")
                        soilToFertilizer.getSource(fertilizer)?.let { soil ->
                            println("\t\t\t\t\t\tsoil: $soil")
                            seedRanges.firstNotNullOfOrNull {
                                if (seedToSoil.getSource(soil) in it) seedToSoil.getSource(soil) else null
                            }?.let { return it }
                        }
                    }
                }
            }
        }
    }
    return null
}

private fun parseData(input: List<String>) {
    seeds = input[0].split(':')[1].split(' ').filter { it.isNotBlank() }.map { it.trim().toLong() }

    var i = 1
    while (i < input.size) {
        when (input[i]) {
            "seed-to-soil map:" -> {
                seedToSoil = parseMaps(input, i + 1)
                i += seedToSoil.size
            }

            "soil-to-fertilizer map:" -> {
                soilToFertilizer = parseMaps(input, i + 1)
                i += soilToFertilizer.size
            }

            "fertilizer-to-water map:" -> {
                fertilizerToWater = parseMaps(input, i + 1)
                i += fertilizerToWater.size
            }

            "water-to-light map:" -> {
                waterToLight = parseMaps(input, i + 1)
                i += waterToLight.size
            }

            "light-to-temperature map:" -> {
                lightToTemperature = parseMaps(input, i + 1)
                i += lightToTemperature.size
            }

            "temperature-to-humidity map:" -> {
                temperatureToHumidity = parseMaps(input, i + 1)
                i += temperatureToHumidity.size
            }

            "humidity-to-location map:" -> {
                humidityToLocation = parseMaps(input, i + 1)
                i += humidityToLocation.size
            }

            else -> {
                i++
            }
        }
    }
}

private fun parseMaps(input: List<String>, offset: Int): MappyList {
    val maps = MappyList()
    var i = offset
    while (i < input.size && input[i][0].isDigit()) {
        val (destination, source, range) = input[i].split(' ').map { it.toLong() }
        maps.add(RangeMap(source, destination, range))
        i++
    }
    return maps
}

class MappyList : ArrayList<RangeMap>() {

    fun getDestination(source: Long): Long {
        return firstNotNullOfOrNull { it.getDestinationForSource(source) } ?: source
    }

    fun getSource(destination: Long): Long {
        return firstNotNullOfOrNull { it.getSourceForDestination(destination) } ?: destination
    }
}

data class RangeMap(val source: Long, val destination: Long, val range: Long) {

    fun getDestinationForSource(value: Long): Long? {
        return if (value >= source && value < source + range) {
            destination + (value - source)
        } else {
            null
        }
    }

    fun getSourceForDestination(value: Long): Long? {
        return if (value >= destination && value < destination + range) {
            source + (value - destination)
        } else {
            null
        }
    }
}
