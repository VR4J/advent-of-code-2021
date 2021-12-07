package be.vreijsen.aoc.day_5

import kotlin.math.*
import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    val input = getPuzzleInput(5, 1)
    val lines = getLines(input)

    getDangerousCoordinates(lines, 2) { line -> ! line.isDiagonal() }
    getDangerousCoordinates(lines, 2) { true }
}

fun getDangerousCoordinates(lines: List<Line>, threshold: Int, filter: (line: Line) -> Boolean) {
    val result = lines
                    .filter(filter)
                    .flatMap { line -> line.getPoints() }
                    .groupingBy { it }
                    .eachCount()
                    .filter { it.value >= threshold }

    println("Number of dangerous spots: ${result.keys.size}")
}

fun getLines(input: List<String>): List<Line>  {
    return input.map { it.trim().split(" -> ") }
                .map { 
                    val (x1, y1) = it[0].split(",")
                    val (x2, y2) = it[1].split(",")

                    Line(
                        Coordinate(x1.toInt(), y1.toInt()),
                        Coordinate(x2.toInt(), y2.toInt()),
                    )
                }
}

data class Line(val start: Coordinate, val end: Coordinate) {
    
    fun getPoints(): List<Coordinate> {
        if(start.x == end.x) {
            val range = Math.min(start.y, end.y)..Math.max(start.y, end.y)
            return range.map { Coordinate(start.x, it) }
        }

        if(start.y == end.y) {
            val range = Math.min(start.x, end.x)..Math.max(start.x, end.x)
            return range.map { Coordinate(it, start.y) }
        }

        val xDelta = (end.x - start.x).sign // Either +1, 0 or -1
        val yDelta = (end.y - start.y).sign // Either +1, 0 or -1

        // Max steps we need to take
        // Use absolute value to get go from -1 to 1
        val steps = maxOf((start.x - end.x).absoluteValue, (start.y - end.y).absoluteValue)

        // + +1, 0 or + -1 
        return (1 .. steps).scan(start) { last, _ -> Coordinate(last.x + xDelta, last.y + yDelta) }
    }

    fun isDiagonal(): Boolean = ! (start.y == end.y || start.x == end.x)
}

data class Coordinate(val x: Int, val y: Int)