package be.vreijsen.aoc.day_10

import be.vreijsen.aoc.utils.*;

val syntaxScoreDefinition = listOf<Triple<Char, Char, Int>>(
    Triple('(', ')', 1), // 3
    Triple('[', ']', 2), // 57
    Triple('{', '}', 3), // 1197
    Triple('<', '>', 4)  // 251137
)

fun main(args: Array<String>) {
    var input = getPuzzleInput(10, 1).map { it.map { it } }

    run(input)
}

fun run(input: List<List<Char>>) {
    val occurrences = mutableMapOf<Char, Int>()

    val corruptSyntaxScore = mutableMapOf<Char, Int>()
    val incompleteSyntaxScore = mutableListOf<Long>()

    input.forEachIndexed { lineIndex, line -> 
        var isCorrupted = false
        val closingCharactersExpected = mutableListOf<Char>()

        line.forEachIndexed { characterIndex, character -> 
            if(! isCorrupted) {
                val definition = getCharacterDefinition(character)

                if(definition == null) {
                    throw IllegalStateException("Unknown character spotted:  $character")
                }

                // isOpeningCharacter
                if(definition.first == character) {
                    closingCharactersExpected.add(0, definition.second)
                }

                // isClosingCharacter
                if(definition.second == character) {
                    val expected = closingCharactersExpected.get(0)
                    
                    isCorrupted = character != expected

                    if(isCorrupted) {
                        val occurrence = occurrences.getOrDefault(character, 0) + 1

                        occurrences.put(character, occurrence)
                        corruptSyntaxScore.put(character, occurrence * definition.third)

                        println("Line ${lineIndex + 1},${characterIndex + 1}: Expected $expected but found ${character} instead.")
                    } else {
                        closingCharactersExpected.removeAt(0)
                    }
                }

                // isIncomplete
                if(characterIndex == line.size - 1 && closingCharactersExpected.size > 0) {
                    var score = 0L;

                    closingCharactersExpected.forEach { char ->
                        val def = getCharacterDefinition(char);

                        if(def == null) {
                            throw IllegalStateException("Unknown character spotted:  $character")
                        }
            
                        score = (score * 5) + def.third
                    }

                    incompleteSyntaxScore.add(score)                    
                }
            }
        }
    }

    incompleteSyntaxScore.sortDescending()
    val middle = (incompleteSyntaxScore.size - 1) / 2

    println("Result Part 1: ${corruptSyntaxScore.values.sum()}")
    println("Result Part 2: ${incompleteSyntaxScore.get(middle)}")
}

fun isOpeningCharacter(character: Char): Boolean {
    return syntaxScoreDefinition.any { it.first == character }
}

fun getCharacterDefinition(character: Char): Triple<Char, Char, Int>? {
    return syntaxScoreDefinition.find { it.first == character || it.second == character }
}