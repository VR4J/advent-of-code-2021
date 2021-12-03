package be.vreijsen.aoc.day_3

import be.vreijsen.aoc.utils.*;
import kotlin.comparisons.compareByDescending

val widthRange = 0..11

fun main(args: Array<String>) {
    val input = getPuzzleInput(3, 1)

    val powerConsumption = getPowerConsumption(input);
    println("Submarine power consumption is $powerConsumption")

    val lifeSupport = getLifeSupportRating(input);
    println("Submarine life support rating is $lifeSupport")
}

fun getLifeSupportRating(rows: List<String>): Int {
    val oxygenGeneratorRating = locateWithFiltering(rows) { getMostOccurring(it) }
    val gasScrubberRating = locateWithFiltering(rows) { getLeastOccurring(it) }

    return oxygenGeneratorRating * gasScrubberRating
}

fun getPowerConsumption(rows: List<String>): Int {
    val gammaRating = locate(rows) { getMostOccurring(it) }
    val epsilonRating = locate(rows) { getLeastOccurring(it) }

    return gammaRating * epsilonRating
}

fun locate(rows: List<String>, getOccuringFn: OccurrenceCalculation): Int {
    return (widthRange)
                .map { index -> getColumn(rows, index) }
                .map { values -> getOccuringFn(values) }
                .joinToString("")
                .toInt(2)
}

fun locateWithFiltering(rows: List<String>, getOccuringFn: OccurrenceCalculation): Int {
    val rowState = rows.toMutableList()

    for (index in widthRange) {
        val column = getColumn(rowState, index) 
        val result = getOccuringFn(column)

        rowState.removeIf { it.get(index) - '0' != result }

        if(rowState.size == 1) break;
    }

    return rowState.get(0).toInt(2)
}

fun getMostOccurring(values: List<Int>): Int? {
    val occurences = values.groupingBy { it }
        .eachCount()
        .toSortedMap(compareByDescending { it })

    return occurences.maxByOrNull { it.value }?.key
}

fun getLeastOccurring(values: List<Int>): Int? {
    val occurences = values.groupingBy { it }
        .eachCount()
        .toSortedMap(compareBy { it })

    return occurences.minByOrNull { it.value }?.key
}

fun getColumn(rows: List<String>, index: Int): List<Int> {
    return rows.map { row -> row.get(index) - '0' }
}

typealias OccurrenceCalculation = (values: List<Int>) -> Int?