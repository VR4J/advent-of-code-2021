package be.vreijsen.aoc.utils

fun LongArray.slide() {
    val left = first()
    this.copyInto(this, startIndex = 1)
    this[this.lastIndex] = left
}
