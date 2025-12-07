package dag2

import java.io.File

fun main() {
    val list = File("data.txt").readText().trim().split(",").map { it.split('-') }.map { (a, b) -> a.toLong() to b.toLong() }
    println("del1 sum: " + solve(list, 2))
    println("del2 sum: " + solve(list, null))
}

private fun solve(list: List<Pair<Long, Long>>, maxParts: Int?): Long {
    var sum = 0L
    list.forEach {
        var current = it.first
        while (current <= it.second) {
            val str = current.toString()
            val length = str.length
            for (parts in 2..(maxParts ?: length)) {
                if (length % parts == 0) {
                    if (str.chunked(str.length / parts).distinct().size <= 1) {
                        sum += current
                        break
                    }
                }
            }
            current++
        }
    }
    return sum
}