package be.vreijsen.aoc.day_4

import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    val input = getPuzzleInput(4, 1)

    val draws = getDrawnNumbers(input)
    val boards = getBoards(input)

    play(draws, boards) { it.hasBingo() }
    play(draws, boards) { board -> board.hasBingo() && boards.count { it.hasBingo() } == boards.size }
}

fun play(draws: List<Int>, boards: List<Board>, validate: (board: Board) -> Boolean ) {
    draws.forEach { number -> 
        boards.forEachIndexed { index, board -> 
            board.mark(number) 

            if (validate(board)) {
                println("Board ${index + 1} has Bingo with final number $number")
                println("Result is ${board.getRemainingSum() * number}")
                return;
            }
        }
    }   
}

fun getDrawnNumbers(input: List<String>): List<Int> {
    return input.first().split(",").map { it.toInt() }
}

fun getBoards(input: List<String>): List<Board> {
    var boards: MutableList<Board> = mutableListOf()
    var board: Board = Board();

    input
        .filterIndexed { index, line -> index > 1 && line.length != 0 }
        .map { it.trim().split(" ") }
        .forEach { numbers ->                          
            val row = numbers.filter { it.isNotBlank() }.map { it.toInt() }
            
            board.put(row)
    
            if(board.isComplete()) {
                board.genColumns()
                boards.add(board)
                board = Board()
            }
        }

    return boards;
}

class Board {

    val rows = mutableListOf<MutableList<BingoNumber>>()
    var columns = mutableListOf<MutableList<BingoNumber>>()

    fun put(line: List<Int>) {
        val series = line.map { BingoNumber(it) }.toMutableList()
        rows.add(series)
    }

    fun mark(drawn: Int) {
        rows.forEach { row -> 
            row.filter { it.number == drawn }
               .forEach(BingoNumber::mark)
        }
    }

    fun hasBingo(): Boolean {
        return rows.any { row -> row.all { it.marked } }
                || columns.any { column -> column.all { it.marked } }
    }

    fun getRemainingSum(): Int {
        return rows.
            flatMap { row -> row.filter { ! it.marked }}
            .map { it.number }
            .sum()
    }

    fun genColumns() {
        columns = rows.transpose(5, 5, BingoNumber(0))
    }

    fun isComplete(): Boolean = rows.size == 5
}

data class BingoNumber (val number: Int, var marked: Boolean = false) {
    fun mark() { marked = true }
}
