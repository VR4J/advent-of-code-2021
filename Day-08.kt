package be.vreijsen.aoc.day_8

import java.util.Collections;
import kotlin.math.abs;
import be.vreijsen.aoc.utils.*;

/*
        0
     -- -- --
    |        |
  1 |        | 2
    |   3    |
     -- -- --
    |        |
  4 |        | 5
    |   6    |
     -- -- --
*/

val digits = mutableMapOf(
    Pair(0, listOf(0, 1, 2, 4, 5, 6)),
    Pair(1, listOf(2, 5)),
    Pair(2, listOf(0, 2, 3, 4, 6)),
    Pair(3, listOf(0, 2, 3, 5, 6)),
    Pair(4, listOf(1, 2, 3, 5)),
    Pair(5, listOf(0, 1, 3, 5, 6)),
    Pair(6, listOf(0, 1, 3, 4, 5, 6)),
    Pair(7, listOf(0, 2, 5)),
    Pair(8, listOf(0, 1, 2, 3, 4, 5, 6)),
    Pair(9, listOf(0, 1, 2, 3, 5, 6))
)

fun main(args: Array<String>) {
    var input = getPuzzleInput(8, 1)

    // runPartOne()
    runPartTwo(parse(input))
}

fun runPartTwo(readings: List<Pair<List<String>, List<String>>>)  {
    var sum = 0;

    readings.forEach { reading -> 
        val mapping = getMapping(reading.first)
        val output = getOutput(reading.second, mapping)

        sum += output
    }

    println("Sum is $sum")
}

fun parse(input: List<String>): List<Pair<List<String>, List<String>>> {
    return input.map { line ->
        Pair(
            line.split(" | ")[0].split(" ").map { it.toUpperCase() },
            line.split(" | ")[1].split(" ").map { it.toUpperCase() }
        )
    }
}

fun getOutput(reading: List<String>, mapping: List<String>): Int {
    return reading
        .map { digit ->
            val positions = digit.map { mapping.indexOf(it.toString()) }

            digits.filter { it.value.hasEqualContents(positions) }
            .map { it.key.toString() }
            .first()
        }
        .joinToString("")
        .toInt()
}

fun getMapping(outputs: List<String>): List<String>  {
    val values = outputs.sortedByDescending() { it.length }
    val mapping = mutableMapOf<Int, List<String>>()

    val byLength = values.groupBy { it.length } 

    byLength.forEach {

        // cdfbe
        // gcdfa
        // fbcad

        // Pair(2, listOf(0, 2, 3, 4, 6)),
        // Pair(3, listOf(0, 2, 3, 5, 6)),
        // Pair(5, listOf(0, 1, 3, 5, 6))
        val digitsWithLength = getDigitsWithLength(it.key)

        if(digitsWithLength.size > 0) {

            // 0, 3, 6
            var commonSigmentPositions = listOf<Int>()

            digitsWithLength.forEach { 
                if(commonSigmentPositions.size == 0) {
                    commonSigmentPositions = it.second
                } else {
                    commonSigmentPositions = it.second.filter { v -> commonSigmentPositions.contains(v) }
                }
            }

            // c, d, f
            var commonValues = listOf<String>()

            it.value.forEach { 
                if(commonValues.size == 0) {
                    commonValues = it.map { it.toString() }
                } else {
                    commonValues = it.map { it.toString() }.filter { v -> commonValues.contains(v) }
                }
            }
    
            commonSigmentPositions.forEach { position -> 

                if(! mapping.containsKey(position)) {
                    mapping[position] = commonValues
                } else {
                    mapping[position] = mapping[position] !!.filter { commonValues.contains(it) }
                }
            }
        }
    }

    do {
        var uncertainties = mapping.values.filter { it.size > 1 }
        var certainties = mapping.values.filter { it.size == 1 }.map { it[0] }

        mapping.filter { (key, value) -> value.size > 1 }
           .forEach { (key, value) -> 
                mapping[key] = value.filter { ! certainties.contains(it) }
           }
    } while (uncertainties.size > 0)

    return mapping.map { it.value.first() }
}

fun getDigitsWithLength(length: Int): List<Pair<Int, List<Int>>> {
    return digits.entries.filter { (number, segments) -> segments.size == length }
                          .map { Pair(it.key, it.value) }
}