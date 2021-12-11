package be.vreijsen.aoc.day_11

import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    var grid = getPuzzleInput(11, 1).flatMapIndexed { y, row -> row.mapIndexed { x, value -> Octopus(Coordinate(x, y), value - '0') } }

    simulate(grid, 100)
}

fun simulate(grid: List<Octopus>, steps: Int) {
    var flashes = 0L
    var step = 0;

    while(true) {
        val flashed = mutableSetOf<Coordinate>()

        grid.forEach { it.doStep(grid, flashed) }

        flashes += flashed.size

        if(steps == ++step) {
           println("After ${step} steps: $flashes flashes.")
        }

        if(flashed.size == grid.size) break;
    }

    println("All octupus flashed at step: $step")
}

data class Octopus(val position: Coordinate, var energy: Int) {

    fun doStep(grid: List<Octopus>, flashed: MutableSet<Coordinate>) {
        if(flashed.contains(this.position)) return;

        this.energy++;
        
        if(this.energy > 9) {
            flash(grid, flashed)
        }
    }

    fun flash(grid: List<Octopus>, flashed: MutableSet<Coordinate>) {
        flashed.add(this.position)

        val adjecent = getAdjecentOctopus(grid)
        
        adjecent.filter { ! flashed.contains(it.position) }
                .forEach { it.doStep(grid, flashed) }

        this.energy = 0;
    }

    fun getAdjecentOctopus(grid: List<Octopus>): List<Octopus> {
        return grid.filter { isHorizontallyAdjecentTo(it) || isVerticallyAdjecentTo(it) || isDiagonallyAdjecentTo(it) }
    }

    fun isHorizontallyAdjecentTo(other: Octopus): Boolean {
        return this.position.y == other.position.y && (this.position.x + 1 == other.position.x || this.position.x - 1 == other.position.x)
    }

    fun isVerticallyAdjecentTo(other: Octopus): Boolean {
        return this.position.x == other.position.x && (this.position.y + 1 == other.position.y || this.position.y - 1 == other.position.y)
    }

    fun isDiagonallyAdjecentTo(other: Octopus): Boolean {
        return (this.position.x - 1 == other.position.x && this.position.y -1 == other.position.y)
                || (this.position.x - 1 == other.position.x && this.position.y + 1 == other.position.y)
                || (this.position.x + 1 == other.position.x && this.position.y - 1 == other.position.y)
                || (this.position.x + 1 == other.position.x && this.position.y + 1 == other.position.y)
    }
}

data class Coordinate(val x: Int, val y: Int)