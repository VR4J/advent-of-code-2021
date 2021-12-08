package be.vreijsen.aoc.utils

fun <T> List<T>.copyOf(): List<T> {
    val original = this
    return mutableListOf<T>().apply { addAll(original) }
}

fun <T> MutableList<MutableList<T>>.transpose(column: Int, rows: Int, default: T): MutableList<MutableList<T>> {
    val transposed = MutableList(column) { MutableList<T>(rows , { default }) }

    for(i in transposed.indices){
        for(j in transposed[i].indices){
            transposed[i][j] = this[j][i]
        }
    }

    return transposed;
}

fun <T> List<T>.hasEqualContents(target: List<T>): Boolean {
    val set: Set<T> = this.toSet()
    return this.size == target.size && set.containsAll(target);
}
