package be.vreijsen.aoc.day_18

import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    val input = getPuzzleInput(18, 1)
    val result = reduce(input)

    println("Result: $result")

    val magnitude = getMagnitude(result)

    println("Magnitude: $magnitude")   

    val highest = getHighestMagnitude(input)

    println("Highest Magnitude: $highest") 
}

fun getHighestMagnitude(input: List<String>): Long {
    var highest = 0L

    input.forEach { line -> 
        input.forEach { other ->
            if(line != other) {
                var result = add(line, other)
                var magnitude = getMagnitude(result)

                if(magnitude > highest) highest = magnitude
            }
        }
    }

    return highest
}

fun getMagnitude(original: String): Long {
    var value = original
    val pattern = Regex("\\[(\\d+),(\\d+)\\]")
    
    var match = pattern.find(value)

    while(match != null) {
        value = value.substring(0, match.range.start) + ((3 * match.groupValues[1].toInt()) + (2 * match.groupValues[2].toInt())) + value.substring(match.range.last + 1)
        match = pattern.find(value)
    }

    return value.toLong()
}

fun reduce(input: List<String>): String {
    var start = input[0]

    input.forEach { line -> 
        if(line != start) {
            start = add(start, line)
        }
    }

    return start;
}

fun add(first: String, second: String): String {
    var added = "[${first},${second}]"
    var result = added;

     do {
        var prevResult = result;
        var explodables = getExplodables(result)

        if(explodables.size > 0) {
            result = explode(explodables[0], result)
        } else {
            var splittables = getSplittables(result)

            if(splittables.size > 0) {
                result = split(splittables[0], result)
            }
        }
    } while(result != prevResult)

    return result;
}

fun explode(explodable: Explodable, original: String): String {
    var shift = 0
    var value = original;
    var left = walk(value, Side.LEFT, explodable.range.start) { char -> char !in listOf('[', ']', ',') }

    if(left != null) {
        val (index, number) = left

        if(number + explodable.left > 9 && number < 10) {
            shift += 1
        }

        // Replace
        value = value.substring(0, index - (number.toString().length - 1)) + (number + explodable.left) + value.substring(index + 1)
    }

    // find most right number
    var right = walk(value, Side.RIGHT, explodable.range.last) { char -> char !in listOf('[', ']', ',') }

    if(right != null) {
        val (index, number) = right

        // Replace
        value = value.substring(0, index) + (number + explodable.right) + value.substring(index + number.toString().length)
    }

    return value.substring(0, explodable.range.start + shift) + 0 + value.substring(explodable.range.last + shift + 1)
}

fun split(splittable: Splittable, original: String): String {
    var value = original;

    var left = Math.floor(splittable.number.toDouble() / 2).toInt()
    var right = Math.ceil(splittable.number.toDouble() / 2).toInt()

    return value.substring(0, splittable.range.start) + "[$left,$right]" + value.substring(splittable.range.last + 1) 
}

fun getDepth(original: String, index: Int): Int {
    var value = original.substring(0, index)
    var opening = value.count { it == '[' }
    var closing = value.count { it == ']'}

    return opening - closing
}

fun walk(value: String, side: Side, from: Int, isMatch: (char: Char) -> Boolean): Pair<Int, Int>? {
    when(side) {
        Side.LEFT -> {
            if(from == 0) return null
            
            val index = from - 1
            val matches = isMatch(value[index])
            
            if(matches) {
                if(isMatch(value[index - 1])) {
                    return Pair(index, value.substring(index - 1, index + 1).toInt())
                }

                return Pair(index, value[index] - '0')
            } else {
                return walk(value, side, index, isMatch)
            }
        }
        Side.RIGHT -> {
            if(from == value.length - 1) return null

            val index = from + 1
            val matches = isMatch(value[index])
            
            if(matches) {
                if(isMatch(value[index + 1])) {
                    return Pair(index, value.substring(index, index + 2).toInt())
                }

                return Pair(index, value[index] - '0')
            } else {
                return walk(value, side, index, isMatch)
            }
        }
    }
}

fun isExploding(position: Int, value: String): Boolean {
    return getDepth(value, position) > 4
}

fun getExplodables(value: String): List<Explodable> {
    val pattern = Regex("\\[(\\d{1,2}),(\\d{1,2})\\]")
    val matches = pattern.findAll(value)

    return matches.filter { isExploding(it.range.start + 1, value) }
                            .map { Explodable(it.range, it.groupValues[1].toInt(), it.groupValues[2].toInt()) }
                            .toList()
}

fun getSplittables(value: String): List<Splittable> {
    val pattern = Regex("\\d{2}")
    val matches = pattern.findAll(value)

    return matches.map { Splittable(it.range, it.groupValues[0].toInt()) }
                  .toList()
}

enum class Side {
    LEFT, RIGHT
}

data class Explodable(var range: IntRange, var left: Int, var right: Int)

data class Splittable(var range: IntRange, var number: Int)