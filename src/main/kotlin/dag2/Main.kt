package dag2

import java.io.File

fun main() {
    val list = File("data.txt").readText().trim().split(",").map { it.split('-') }.map { (a, b) -> a.toLong() to b.toLong() }
    del1(list)
    del2(list)
}

private fun del2(list: List<Pair<Long, Long>>) {
    val invalidIds = mutableListOf<Long>()
    list.forEach {
        var v = it.first
        val end = it.second
        while (v <= end) {
            val str = v.toString()
            val length = str.length
            for (antallDeler in 2..length) {
                if (length % antallDeler == 0) {
                    if (allEqual(splitIntoParts(str, antallDeler))) {
                        invalidIds.add(v)
                        break
                    }
                }
            }
            v++
        }
    }
    val sum = invalidIds.reduce { a, b -> a + b }
    println("del2 sum: $sum")
}

fun allEqual(strings: List<String>) =
    strings.distinct().size <= 1

fun splitIntoParts(s: String, parts: Int) =
    s.chunked(s.length / parts)

private fun del1(list: List<Pair<Long, Long>>) {
    val invalidIds = mutableListOf<Long>()
    list.forEach {
        var v = it.first
        val end = it.second
        while (v <= end) {
            val str = v.toString()
            val length = str.length
            if (length % 2 == 0) {
                if (allEqual(splitIntoParts(str, 2)))
                    invalidIds.add(v)
            }
            v++
        }
    }
    val sum = invalidIds.reduce { a, b -> a + b }
    println("del1 sum: $sum")
}

