package be.vreijsen.aoc.utils

import java.io.File;

fun getPuzzleInput(day: Number, part: Number): List<String> {
    return File("inputs/Day-$day/Part-$part.txt").readLines()
}
