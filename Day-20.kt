package be.vreijsen.aoc.day_20_1

import be.vreijsen.aoc.utils.*;

var step = 0;

fun main(args: Array<String>) {
    val algorithm = getPuzzleInput(20, 1)[0].map { it }
    var pixels = getPuzzleInput(20, 1).filterIndexed { index, _ -> index > 1}
                                      .map { line -> line.map { pixel -> pixel} }

    repeat(2) {
        pixels = enhance(algorithm, pixels)
        step++
    }

    println("Part 1: ${pixels.flatMap { it }.count { it == '#' }}")

    repeat(48) {
        pixels = enhance(algorithm, pixels)
        step++
    }

    println("Part 2: ${pixels.flatMap { it }.count { it == '#' }}")
}

fun enhance(algorithm: List<Char>, pixels: List<List<Char>>): List<List<Char>> {
    var processed = mutableListOf<MutableList<Char>>()

    (-1..pixels.size).forEach { y -> 
        val enhanced = mutableListOf<Char>()
        val current = pixels.getOrElse(y) { List(pixels[0].size) { '.' } }

        (-1..current.size).forEach { x -> 
            var default = if(algorithm[0] == '#' && step % 2 != 0) '#' else '.' 

            val result = getBasedOneSurrounding(pixels, x, y, algorithm, default)

            enhanced.add(result)
        }

        processed.add(enhanced)
    }    

    return processed
}

fun getBasedOneSurrounding(pixels: List<List<Char>>, x: Int, y: Int, algorithm: List<Char>, default: Char): Char {
    val top = getBinaryRow(pixels, x, y - 1, default)
    val middle = getBinaryRow(pixels, x, y, default)
    val bottom = getBinaryRow(pixels, x, y + 1, default)

    val binary = listOf(top, middle, bottom).joinToString("")

    return algorithm[binary.toInt(2)]
}

fun getBinaryRow(pixels: List<List<Char>>, x: Int, y: Int, default: Char): String {
    val left = pixels.filterIndexed { index, _ -> index == y }.flatMap { it.filterIndexed { xLeft, _ -> xLeft == x - 1 } }
    val middle = pixels.filterIndexed { index, _ -> index == y }.flatMap { it.filterIndexed { xLeft, _ -> xLeft == x } }
    val right = pixels.filterIndexed { index, _ -> index == y }.flatMap { it.filterIndexed { xLeft, _ -> xLeft == x + 1 } }

    return listOf(
        left.getOrElse(0) { default },
        middle.getOrElse(0) { default },
        right.getOrElse(0) { default }
    )
    .map { if(it == '#') 1 else 0 }
    .joinToString("")
}

fun visualize(pixels: List<List<Char>>) {
    pixels.forEach { line -> 
        println( line.joinToString("") )
    }
}