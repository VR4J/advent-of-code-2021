package be.vreijsen.aoc.day_17

import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    val targetCoords = getPuzzleInput(17, 1)[0].substring("target area: ".length).split(", ")
    val x = targetCoords[0].split("x=")[1].split("..")
    val y = targetCoords[1].split("y=")[1].split("..")
    
    val target = Area(
        Coordinate(x[0], y[0]),
        Coordinate(x[1], y[1]),
    )

    var velocity = getHighestVelocity(Coordinate(0,0), target)
    var count = getAllVelocity(target)

    println("Part 1: $velocity")
    println("Part 2: $count")
}

fun getAllVelocity(target: Area): Int {
    var results = mutableListOf<Pair<Int, Int>>()

    (0..target.end.x).forEach { i ->
        (target.start.y..999).forEach { j ->
            var (x, y, step) = Triple(0, 0, 0)

            while(x <= target.end.x && y >= target.start.y) {
                if(x >= target.start.x && y <= target.end.y) {
                    results.add(Pair(x, y))
                    break;
                }

                x += Math.max(0, i - step)
                y += j - step
                step++
            }
        }
    }

    return results.size
}

fun getHighestVelocity(start: Coordinate, target: Area): Int {
    
    var x = start.x
    var xVelocity = 0

    while(x !in (target.start.x..target.end.x)) {
        xVelocity++
        x = (xVelocity * (xVelocity + 1)) / 2
    }

    var y = start.y
    var yVelocity = 1000
    var highest = 0;

    while(y !in (target.start.y..target.end.y)) {
        yVelocity--
        highest = 0

        y = 0
        var i = 0

        while(y >= target.end.y) {
            y += yVelocity - i

            if(highest < y) {
                highest = y
            }

            i++
        }
    }

    return highest;
}

data class Area(val start: Coordinate, val end: Coordinate) {

    fun contains(coordinate: Coordinate): Boolean {
        return coordinate in start..end
    }
}

class Coordinate(val x: Int, val y: Int): Comparable<Coordinate> {

    constructor(x: String, y: String): this(x.toInt(), y.toInt())

    override fun compareTo(other: Coordinate): Int {
        if (this.x != other.x) {
            return this.x - other.x
        }

        return this.y - other.y
    }
}