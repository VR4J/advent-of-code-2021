package be.vreijsen.aoc.day_2

import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    val input = getPuzzleInput(2, 1)

    navigate(
        input,
        { value, (x, y) -> arrayOf(x + value, y) },
        { value, (x, y) -> arrayOf(x, y + value) },
        { value, (x, y) -> arrayOf(x, y - value) },
    );

    navigate(
        input,
        { value, (x, y, a) -> arrayOf(x + value, y + (a * value), a) },
        { value, (x, y, a) -> arrayOf(x, y, a + value) },
        { value, (x, y, a) -> arrayOf(x, y, a - value) }
    );
}

fun navigate(input: List<String>, forward: NavigationCommand, down: NavigationCommand, up: NavigationCommand) {
    var position = arrayOf(0, 0, 0)

    input.forEach { line -> 
        var (command, value) = line.split(" ")

        position = when (command) {
            "forward" ->
                forward(value.toInt(), position)
            "down" -> 
                down(value.toInt(), position)
            "up" -> 
                up(value.toInt(), position)
            else -> 
                // do nothing
        }
    }
    
    val (x, y) = position;

    println("Multiplied horizontal ($x) position by depth ($y) is ${x * y}")
}

typealias NavigationCommand = (value: Int, positions: Array<Int>) -> Array<Int>