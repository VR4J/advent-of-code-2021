package be.vreijsen.aoc.utils

fun <K> MutableMap<K, Long>.increase(key: K, value: Long) {
    when (val count = this[key]) {
        null -> this[key] = value
        else -> this[key] = count + value
    }
}

fun <K> MutableMap<K, Long>.decrease(key: K, value: Long) {
    when (val count = this[key]) {
        null -> this[key] = value
        else -> this[key] = count - value
    }
}