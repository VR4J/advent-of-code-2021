package be.vreijsen.aoc.day_15

import java.time.*;
import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    var coordinates = getPuzzleInput(15, 1).flatMapIndexed { y, line -> line.mapIndexed { x, risk -> Coordinate(x, y) } }
    var risks = getPuzzleInput(15, 1).map { line -> line.map { risk -> risk - '0' }.toTypedArray() }.toTypedArray()

    risks = inflate(risks, 5)

    val xMax = risks[0].size - 1
    val yMax = risks.size - 1

    var done = mutableSetOf<Coordinate>()
    var distances = mutableMapOf<Coordinate, Long>()

    risks.forEachIndexed { y, line -> line.forEachIndexed { x, risk -> distances.put(Coordinate(x, y), Long.MAX_VALUE) }}

    distances[coordinates.first()] = 0

    val positions = mutableSetOf(coordinates.first())
    
    while(! positions.isEmpty()) {
        val position = getClosest(positions, distances)
        val neighbours = position.neighbours(xMax, yMax).filter { it !in done }

        neighbours.forEach { neighbour -> 
            var newRisk = distances[position] !! + risks[neighbour.x][neighbour.y]

            if(newRisk < distances[neighbour] !!) {
                distances[neighbour] = newRisk
            }
        }

        done.add(position)

        positions.remove(position)
        positions.addAll(neighbours)
    }

    println(distances[Coordinate(xMax, yMax)])
}

fun getClosest(grid: Set<Coordinate>, distances: Map<Coordinate, Long>): Coordinate {
    return grid.minByOrNull { distances[it] !! } !!
}

data class Coordinate(val x: Int, val y: Int) { 

    fun neighbours(xMax: Int, yMax: Int): List<Coordinate> {
        return listOf(
            Coordinate(x + 1, y),
            Coordinate(x - 1, y),
            Coordinate(x, y + 1),
            Coordinate(x, y - 1),
        ).filter { it.x <= xMax && it.y <= yMax && it.x >= 0 && it.y >= 0}
    }
}

fun inflate(risks: RiskLevels, times: Int): RiskLevels {

    val transform = { i: Int, j: Int ->
        val ret = (i + j) % 9
        if (ret != 0) ret else 9
    }

    val yMax = risks.size
    val xMax = risks[0].size

    val grid = Array(yMax * times) { Array(xMax * times) { 0 } }

    (0 until yMax).forEach { y ->
        (0 until xMax).forEach { x ->
            (0 until times).forEach { i -> 
                (0 until times).forEach { j -> 
                    grid[y + i * yMax][x + j * xMax] = transform(risks[y][x], i + j)
                }
            }
        }
    }

    return grid
}

typealias RiskLevels = Array<Array<Int>>