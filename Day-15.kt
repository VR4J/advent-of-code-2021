package be.vreijsen.aoc.day_15

import java.time.*;
import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    var coordinates = getPuzzleInput(15, 1).flatMapIndexed { y, line -> line.mapIndexed { x, risk -> Coordinate(x, y, risk - '0') } }
    var distances = mutableMapOf<Coordinate, Long>()

    coordinates.forEach { coord -> distances.put(coord, Long.MAX_VALUE) }

    distances[coordinates.first()] = 0

    val positions = coordinates.toMutableList()
    
    while(! positions.isEmpty()) {
        val position = getClosest(positions, distances)
        positions.remove(position)

        var neighbours = position.neighbours(coordinates)

        neighbours.forEach { neighbour -> 
            if(distances[neighbour] !! > distances[position] !! + neighbour.risk) {
                distances[neighbour] = distances[position] !! + neighbour.risk
            }
        }
    }

    println(distances[coordinates.last()])
}

fun getClosest(grid: List<Coordinate>, distances: Map<Coordinate, Long>): Coordinate {
    return grid.minByOrNull { distances[it] !! } !!
}

data class Coordinate(val x: Int, val y: Int, val risk: Int) { 

    fun neighbours(grid: List<Coordinate>): List<Coordinate> {
        return grid.filter { 
            (it.x == this.x && it.y == this.y + 1)
                || (it.x == this.x && it.y == this.y - 1)
                || (it.x == this.x + 1 && it.y == this.y)
                || (it.x == this.x - 1 && it.y == this.y)
        }
    }
}