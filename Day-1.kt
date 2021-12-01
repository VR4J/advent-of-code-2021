package be.vreijsen.aoc

import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    val input = getPuzzleInput(1, 1)
    var count = 0;

    for (i in input.indices) {
        if(i == 0) continue;

        val currentLine = input[i].toInt();
        val previousLine = input[i - 1].toInt();

        if(currentLine > previousLine) {
            count++;
        }
    }

    println("Depth measured increased $count times.")
}