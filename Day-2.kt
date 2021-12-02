package be.vreijsen.aoc.day_2_2

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

fun navigate(input: List<String>, forward: NavigationMove, down: NavigationMove, up: NavigationMove) {
    var position = arrayOf(0, 0, 0)

    input.forEach { command -> 
        if(command.contains("forward")) {
            val value = command.substring("forward ".length)
            position = forward(value.toInt(), position)
        }
    
        if(command.contains("down")) {
            val value = command.substring("down ".length)
            position = down(value.toInt(), position)
        }
    
        if(command.contains("up")) {
            val value = command.substring("up ".length)
            position = up(value.toInt(), position)
        }
    }
    
    val (x, y) = position;

    println("Multiplied horizontal ($x) position by depth ($y) is ${x * y}")
}

typealias NavigationMove = (value: Int, positions: Array<Int>) -> Array<Int>