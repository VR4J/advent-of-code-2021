package be.vreijsen.aoc.day_7

import kotlin.math.abs
import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    val input = getPuzzleInput(7, 1).flatMap { it.split(",") }
                                    .map { it.toInt() }
                                    .sorted()
    
    runPartOne(input)
    runPartTwo(input)
}

fun runPartOne(positions: List<Int>) {
    val median = getMedian(positions)
    val fuel = getFuel(positions, median)

    println("Most efficient position is $median which will use $fuel amount of fuel.")
}

fun runPartTwo(positions: List<Int>) {
    val average = getNearestToAverage(positions)
    val fuel = getFuel(positions, average) { diff -> diff * (diff + 1) / 2 }

    println("Most efficient position is $average which will use $fuel amount of fuel.")
}

fun getFuel(numbers: List<Int>, median: Long): Long {
    return numbers.map { Math.abs(it - median) }.sum()
}

fun getMedian(numbers: List<Int>): Long {
    var median: Double

    if (numbers.size % 2 == 0) {
        median = (numbers.get(numbers.size / 2).toDouble() + numbers.get(numbers.size / 2 - 1)).toDouble() / 2;
    } else {
        median = numbers.get(numbers.size / 2).toDouble()
    }

    return Math.round(median)
}

fun getNearestToAverage(numbers: List<Int>): Long {
    val average = Math.round(numbers.average()).toLong()
    val nearest = numbers.map { Math.abs(it - average) }.minOrNull() !!

    return average - nearest
} 

fun getFuel(numbers: List<Int>, target: Long, applyBurnRate: (difference: Long) -> Long): Long {
    return numbers.map { Math.abs(it.toLong() - target)  }
                  .map(applyBurnRate)
                  .sum()
}