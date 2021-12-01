package be.vreijsen.aoc

import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    val inputPartOne = getPuzzleInput(1, 1)

    runPartOne(inputPartOne);
    runPartTwo(inputPartOne);
}

fun runPartOne(input: List<String>) {
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

fun runPartTwo(input: List<String>) {
    // TODO
}