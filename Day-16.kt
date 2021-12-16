package be.vreijsen.aoc.day_16

import be.vreijsen.aoc.utils.*;

val mapping = mapOf(
    Pair('0', "0000"),
    Pair('1', "0001"),
    Pair('2', "0010"),
    Pair('3', "0011"),
    Pair('4', "0100"),
    Pair('5', "0101"),
    Pair('6', "0110"),
    Pair('7', "0111"),
    Pair('8', "1000"),
    Pair('9', "1001"),
    Pair('A', "1010"),
    Pair('B', "1011"),
    Pair('C', "1100"),
    Pair('D', "1101"),
    Pair('E', "1110"),
    Pair('F', "1111"),
)

var versions = 0;

fun main(args: Array<String>) {
    val binary = getPuzzleInput(16, 1)[0].map { mapping[it] }.joinToString("")    
    val result = read(binary).first

    println("Part 1: ${versions}")
    println("Part 2: ${result.toLong()}")
}

fun read(binary: String): Pair<String, Int> {
    val version = getValue(binary.substring(0, 3))
    val type = getValue(binary.substring(3, 6))

    versions += version - '0'

    return when (type) {
        '0' -> getOperatorPackage(binary) { state, value -> state + value }
        '1' -> getOperatorPackage(binary) { state, value ->  state * value }
        '2' -> getOperatorPackage(binary) { state, value -> Math.min(state, value) }
        '3' -> getOperatorPackage(binary) { state, value -> Math.max(state, value) }
        '4' -> getLiteralPackage(binary)
        '5' -> getOperatorPackage(binary) { state, value -> if(value < state) 1 else 0 }
        '6' -> getOperatorPackage(binary) { state, value -> if(value > state) 1 else 0 }
        '7' -> getOperatorPackage(binary) { state, value -> if(value == state) 1 else 0 }
        else -> throw IllegalArgumentException("Unknown type found.")
    }
}

fun getLiteralPackage(binary: String): Pair<String, Int> {
    var vBinary = ""
    var index = 6;

    do {
        var packet = binary.substring(index, index + 5)
        vBinary += packet.substring(1)
        index += 5
    } while(packet.startsWith('1'))

    val value = vBinary.toLong(2)

    return Pair(value.toString(), index)
}

fun getOperatorPackage(binary: String, fn: (state: Long, value: Long) -> Long): Pair<String, Int> {
    val lengthType = binary.substring(6, 7)[0]

    if(lengthType == '0') {
        val length = binary.substring(7, 22).toLong(2)
        var index = 22
        var value = -1L
        
        do {
            val result = read(binary.substring(index))
        
            if(value == -1L) {
                value = result.first.toLong()
            } else {
                value = fn(value, result.first.toLong())
            }
            
            index += result.second
        } while(index < length + 22)

        return Pair(value.toString(), index)
    }

    if(lengthType == '1') {
        val length = binary.substring(7, 18).toLong(2)
        var index = 18
        var count = 0
        var value = -1L
        
        do {
            val result = read(binary.substring(index))

            if(value == -1L) {
                value = result.first.toLong()
            } else {
                value = fn(value, result.first.toLong())
            }

            index += result.second
            count += 1
        } while(count < length)

        return Pair(value.toString(), index)
    }

    throw IllegalArgumentException("")
}

fun getValue(binary: String): Char {
    val reversed = mapping.entries.associate{ (k, v) -> v to k }
    return reversed[binary.padStart(4, '0')] !!
}
