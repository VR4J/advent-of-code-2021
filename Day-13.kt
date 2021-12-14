package be.vreijsen.aoc.day_13

import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    var dots = getPuzzleInput(13, 1).filter { it.contains(",") }.map { 
        val x = it.split(",")[0].toInt()
        val y = it.split(",")[1].toInt()

        Dot(x, y)
    }

    var instructions = getPuzzleInput(13, 1).filter { it.startsWith("fold") }.map {
        val instruction = it.removePrefix("fold along")
        val axis = instruction.split("=")[0]
        val position = instruction.split("=")[1].toInt()

        Instruction(axis.trim(), position)
    }

    var grid = fold(dots, instructions[0])
    println("Part 1: ${grid.size}")
    
    grid = fold(dots, instructions = instructions.toTypedArray())
    visualize(grid)
}

fun visualize(dots: Set<Dot>) {
    dots.groupBy { it.y }.toSortedMap().forEach { (y, values) ->
        val xPositions = values.map { it.x }
        val max = xPositions.maxOrNull() !!
        var line = ""

        for(i in 0..max) {
            line += if(xPositions.contains(i)) "#" else "."
        }

        println(line)
    }
}

fun fold(dots: List<Dot>, vararg instructions: Instruction): Set<Dot> {
    var grid = dots

    instructions.forEach { instruction -> 
        if(instruction.axis == "y") {
            grid = getHorizontalFold(grid, instruction.position)
        } else {
            grid = getVerticalFold(grid, instruction.position)
        }
    }
    
    return grid.toSet()
}

fun getHorizontalFold(dots: List<Dot>, position: Int): List<Dot> {
    return dots.map { dot ->
        if(dot.y > position) {
            val diff = Math.abs(dot.y - position) * 2
            Dot(dot.x, dot.y - diff)
        } else {
            dot
        }
    }
}

fun getVerticalFold(dots: List<Dot>, position: Int): List<Dot> {
    return dots.map { dot ->
        if(dot.x > position) {
            val diff = Math.abs(dot.x - position) * 2
            Dot(dot.x - diff, dot.y)
        } else {
            dot
        }
    }
}

data class Instruction(val axis: String, val position: Int)
data class Dot(val x: Int, val y: Int)