package be.vreijsen.aoc.day_6

import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    
    // Initial: [0, 1, 1, 2, 1, 0, 0, 0, 0]
    val window = LongArray(9).apply {
        getPuzzleInput(6, 1).get(0).split(",")
                            .map { it.toInt() }
                            .forEach { this[it] += 1L }       
    }

    val result = getFishAfterDays(window, 256)

    println("Fish after 80 days: $result")
}

fun getFishAfterDays(window: LongArray, days: Int): Long {
    for(day in 1..days) {
        // Slided window: [1, 1, 2, 1, 0, 0, 0, 0, 0]
        // Puts the 1 at the end (8) which is the time it takes for the new fish to reproduce
        window.slide()

        // Count the extra fish: [1, 1, 2, 1, 0, 0, 1, 0, 0]
        // Puts 1 at (6) as it takes 6 for the current fish to reproduce again
        window[6] += window[8]
    }

    return window.sum();
}
