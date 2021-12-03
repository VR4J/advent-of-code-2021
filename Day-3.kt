package be.vreijsen.aoc.day_3

import be.vreijsen.aoc.utils.*;
import kotlin.comparisons.compareByDescending

fun main(args: Array<String>) {
    val input = getPuzzleInput(3, 1)

    val powerConsumption = getPowerConsumption(input);
    println("Submarine power consumption is $powerConsumption")

    val lifeSupport = getLifeSupportRating(input);
    println("Submarine life support rating is $lifeSupport")
}

fun getLifeSupportRating(rows: List<String>): Int {
    val oxygenGeneratorRating = locateByFiltering(rows) { values -> getMostOccurring(values) }
    val gasScrubberRating = locateByFiltering(rows) { values -> getLeastOccurring(values) }

    return oxygenGeneratorRating * gasScrubberRating
}

fun getPowerConsumption(rows: List<String>): Int {
    val gammaRate = locate(rows) { values -> getMostOccurring(values) }
    val epsilonRate = locate(rows) { values -> getLeastOccurring(values) }

    return gammaRate * epsilonRate
}

fun locate(rows: List<String>, calculateFn: Calculation): Int {
    var state = ""

    getColumns(rows).forEach{ (index, values) -> 
        state += calculateFn(values)
    }

    return state.toInt(2)
}

fun locateByFiltering(rows: List<String>, calculateFn: Calculation): Int {
    var remaining = rows.copyOf()
    var state = "";

    repeat(12) { index ->
        var column = getColumns(remaining).get(index) !!
       
        state += calculateFn(column)

        remaining = remaining.filter { value ->
            value.startsWith(state)
        }

        if(remaining.size == 1) {
            return remaining.get(0).toInt(2)
        }
    }

    throw Exception("Could not locate an unique value in the column");
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

fun getColumns(rows: List<String>): Map<Int, MutableList<Int>> {
    var columns = HashMap<Int, MutableList<Int>>()

    rows.forEach { row -> 
        var values = row.map { it - '0' }
        
        values.forEachIndexed { index, value -> 
            var column = columns.getOrDefault(index, mutableListOf())
            
            column.add(value)
            columns.put(index, column)
        }
    }

    return columns;
}

typealias Calculation = (values: List<Int>) -> Int?