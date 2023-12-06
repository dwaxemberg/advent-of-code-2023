package day5

import utils.InputReader.readFileAsList


private lateinit var seeds: List<Long>
private lateinit var seedToSoil: MappyList
private lateinit var soilToFertilizer: MappyList
private lateinit var fertilizerToWater: MappyList
private lateinit var waterToLight: MappyList
private lateinit var lightToTemperature: MappyList
private lateinit var temperatureToHumidity: MappyList
private lateinit var humidityToLocation: MappyList

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
    }.minOf { it }
}

fun part2(input: List<String>): Long {
    parseData(input)
    val seedRanges = mutableListOf<LongRange>()
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

    humidityToLocation.sortedBy { it.destination }
    humidityToLocation.firstNotNullOf {
        it.source
    }

    return -1
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
}

data class RangeMap(val source: Long, val destination: Long, val range: Long) {

    fun getDestinationForSource(value: Long): Long? {
        return if (value >= source && value < source + range) {
            destination + (value - source)
        } else {
            null
        }
    }
}
