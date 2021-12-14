package be.vreijsen.aoc.day_14

import java.time.*;
import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    var template = getPuzzleInput(14, 1).get(0)
    var rules = getPuzzleInput(14, 1).filter { it.contains("->") }.map { 
        val left = it.split("->")[0].trim()
        val right = it.split("->")[1].trim()

        Rule(left[0], left[1], right[0])
     };

     polymerization(template, rules, 10)
     polymerization(template, rules, 40)
}

fun polymerization(template: String, rules: List<Rule>, steps: Int) {
    val pairs = rules.map { it.elements }
                     .associateWith { occurresIn(template, "${it.first}${it.second}") }
                     .toMutableMap()

    val characters = rules.map { it.insertion }
                    .associateWith { char -> template.count { it == char }.toLong() }
                    .toMutableMap()

    repeat(steps) {
        for((pair, value) in HashMap(pairs)) {
            decrement(pairs, pair, value)

            val rule = getRule(rules, pair) !!

            increment(pairs, Pair(pair.first, rule.insertion), value)
            increment(pairs, Pair(rule.insertion, pair.second), value)

            increment(characters, rule.insertion, value)
        }
    }

    val max = characters.maxByOrNull { it.value }!!.value
    val min = characters.minByOrNull { it.value }!!.value
    
    println("Polymer: $max - $min = ${max - min}")
}

fun getRule(rules: List<Rule>, pair: Pair<Char, Char>): Rule? {
    return rules.find { it.elements == pair }
}

fun occurresIn(string: String, pattern: String): Long {
    var index = 0
    var count = 0L
 
    while (true) {
        index = string.indexOf(pattern, index)
        index += if (index != -1)
        {
            count++
            pattern.length
        }
        else {
            return count
        }
    }
}

fun <K> increment(map: MutableMap<K, Long>, key: K, value: Long) {
    when (val count = map[key]) {
        null -> map[key] = value
        else -> map[key] = count + value
    }
}

fun <K> decrement(map: MutableMap<K, Long>, key: K, value: Long) {
    when (val count = map[key]) {
        null -> map[key] = value
        else -> map[key] = count - value
    }
}

data class Rule(val elements: Pair<Char, Char>, val insertion: Char) {

    constructor(first: Char, second: Char, insertion: Char): this(Pair(first, second), insertion) { }
}