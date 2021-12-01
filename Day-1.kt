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

        val current = input[i].toInt();
        val previous = input[i - 1].toInt();

        if(current > previous) {
            count++;
        }
    }

    println("Depth measured increased $count times.")
}

fun runPartTwo(input: List<String>) {
    val windows = getMeasurementWindows(input);
    var count = 0;

    for (i in windows.indices) {
        if(i == 0) continue;

        val (cFirst, cSecond, cThird) = windows[i]
        val (pFirst, pSecond, pThird) = windows[i - 1]

        val cTotal = cFirst + cSecond + cThird;
        val pTotal = pFirst + pSecond + pThird;

        if(cTotal > pTotal) {
            count++;
        }
    }

    println("Window sums increased $count times.")
}

fun getMeasurementWindows(input: List<String>): List<Triple<Int, Int, Int>> {
    val windows = ArrayList<Triple<Int, Int, Int>>()

    for (i in input.indices) {
        // Break if not enough measurements left to create a window.
        if((i + 2) > (input.size - 1)) break;

        val first = input[i].toInt()
        val second = input[i + 1].toInt()
        val third = input[i + 2].toInt()

        windows.add(
            Triple(first, second, third)
        )
    }

    return windows;
}