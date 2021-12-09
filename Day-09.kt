package be.vreijsen.aoc.day_9

import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    var grid = getPuzzleInput(9, 1).map { it.map { it - '0'}}

    val lowpoints = getLowPoints(grid)
    
    println("Result first part: ${ lowpoints.values.map { it + 1 }.sum() }")

    val basins = getBasins(grid, lowpoints)

    println("Result second part: ${ basins.get(0).size * basins.get(1).size * basins.get(2).size }")
}

fun getLowPoints(grid: List<List<Int>>): Map<Coordinate, Int> {
    val lowpoints = mutableMapOf<Coordinate, Int>()

    grid.forEachIndexed { y, row ->
        row.forEachIndexed { x, value ->
            val matching = getNeighboursMeetingRequirement(grid, x, y) { _, neighbourValue -> neighbourValue > value }

            if(matching.size == 4) {
                lowpoints[Coordinate(x, y)] = value
            }            
        }
    }

    return lowpoints;
}

fun getBasins(grid: List<List<Int>>, lowpoints: Map<Coordinate, Int>): List<Map<Coordinate, Int>> {
    val basins = mutableListOf<Map<Coordinate, Int>>()

    lowpoints.forEach { (pointCoordinate, pointValue) ->
        val basin = mutableMapOf<Coordinate, Int>(
            Pair(pointCoordinate, pointValue)
        )

        walkTheGrid(grid, mutableListOf(), pointCoordinate) { coordinate, neighbourValue ->
            basin[coordinate] = neighbourValue
        }
    
        basins.add(basin)
    }

    basins.sortByDescending { it.size }

    return basins;
}

fun walkTheGrid(grid: List<List<Int>>, walked: MutableList<Coordinate>, target: Coordinate, onMatchingNeighbour: (coordinate: Coordinate, value: Int) -> Unit) {
    walked.add(target)

    val neighbours = getNeighboursMeetingRequirement(grid, target.x, target.y) { coordinate, value -> value < 9 && ! walked.contains(coordinate) }

    neighbours.forEach { (coordinate, value) -> 
        onMatchingNeighbour(coordinate, value)

        walkTheGrid(grid, walked, coordinate, onMatchingNeighbour)
    }
}

fun getNeighboursMeetingRequirement(grid: List<List<Int>>, x: Int, y: Int, requirement: (coordinate: Coordinate, value: Int) -> Boolean): Map<Coordinate, Int> {
    return getSurroundings(grid, x, y).filter { requirement(it.key, it.value) };
}

fun getSurroundings(grid: List<List<Int>>, x: Int, y: Int): Map<Coordinate, Int> {
    if(x == -1 || y == -1) return mapOf<Coordinate, Int>()

    var positions = mutableMapOf<Coordinate, Int>()

    // Top
    positions[Coordinate(x, y - 1)] = if(y > 0 && x <= grid[y - 1].size - 1) grid[y - 1][x] else Int.MAX_VALUE
    
    // Bottom
    positions[Coordinate(x, y + 1)] = if(y < grid.size - 1 && x <= grid[y + 1].size - 1) grid[y + 1][x] else Int.MAX_VALUE

    // Left
    positions[Coordinate(x - 1, y)] = if(x > 0 && y <= grid.size - 1) grid[y][x - 1] else Int.MAX_VALUE

    // Right
    positions[Coordinate(x + 1, y)] = if(y <= grid.size - 1 && x < grid[y].size - 1) grid[y][x + 1] else Int.MAX_VALUE

    return positions;
}

data class Coordinate(val x: Int, val y: Int)
