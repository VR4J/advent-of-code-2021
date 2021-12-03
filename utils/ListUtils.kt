package be.vreijsen.aoc.utils

fun <T> List<T>.copyOf(): List<T> {
    val original = this
    return mutableListOf<T>().apply { addAll(original) }
}