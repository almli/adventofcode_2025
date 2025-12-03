package dag2

import java.io.File

fun main() {
    val list = File("data.txt").readText().trim().split(",").map { it.split('-') }.map { (a, b) -> a.toLong() to b.toLong() }
    println("del1 sum: " + solve(list, 2))
    println("del2 sum: " + solve(list, null))
}

private fun solve(list: List<Pair<Long, Long>>, maksAntallDeler: Int?): Long {
    val invalidIds = mutableListOf<Long>()
    list.forEach {
        var current = it.first
        val end = it.second
        while (current <= end) {
            val str = current.toString()
            val length = str.length
            for (antallDeler in 2..(maksAntallDeler ?: length)) {
                if (length % antallDeler == 0) {
                    if (allEqual(splitIntoParts(str, antallDeler))) {
                        invalidIds.add(current)
                        break
                    }
                }
            }
            current++
        }
    }
    return invalidIds.reduce { a, b -> a + b }
}

fun allEqual(strings: List<String>) =
    strings.distinct().size <= 1

fun splitIntoParts(s: String, parts: Int) =
    s.chunked(s.length / parts)