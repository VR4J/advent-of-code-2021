package be.vreijsen.aoc.utils

import java.io.File;

fun getPuzzleInput(day: Number, part: Number): List<String> {
    val path = String.format("inputs/Day-%02d/Part-$part.txt", day)
    return File(path).readLines()
}
