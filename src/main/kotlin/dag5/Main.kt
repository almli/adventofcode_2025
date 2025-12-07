package dag5

import java.io.File

fun main() {
    val freshRanges = mutableListOf<Pair<Long, Long>>()
    val candidates = mutableListOf<Long>()
    File("data.txt").readLines().forEach { line ->
        if ('-' in line) {
            freshRanges += line.trim()
                .split(",")
                .map { part ->
                    part.substringBefore('-').toLong() to part.substringAfter('-').toLong()
                }
        } else if (line.isNotBlank()) {
            candidates += line.toLong()
        }
    }

    fun del1(): Long {
        fun isFresh(it: Long): Boolean {
            for (range in freshRanges) {
                if (it in range.first..range.second) return true
            }
            return false
        }
        return candidates.count { isFresh(it) }.toLong()
    }

    fun del2() =
        mergeAll(mergeAll(freshRanges.sortedByDescending { it.second - it.first }.toMutableList())).map { it.second - it.first + 1 }.reduce { a, b -> a + b }

    println("del1: " + del1());
    println("del2: " + del2());
}

fun mergeAll(remaining: MutableList<Pair<Long, Long>>): MutableList<Pair<Long, Long>> {
    val mergetRanges = mutableListOf<Pair<Long, Long>>()
    while (remaining.isNotEmpty()) {
        var current = remaining.removeFirst();
        val it = remaining.iterator()
        var mergeDone: Boolean
        do {
            mergeDone = false
            while (it.hasNext()) {
                val candidate = it.next()
                if (isOverlapping(current, candidate)) {
                    current = mergeOverlapping(current, candidate)
                    it.remove()
                    mergeDone = true
                    break
                }

            }
        } while (mergeDone)
        mergetRanges.add(current)
    }
    return mergetRanges
}

fun mergeOverlapping(current: Pair<Long, Long>, candidate: Pair<Long, Long>) =
    (minOf(current.first, candidate.first)) to (maxOf(current.second, candidate.second))

fun isOverlapping(current: Pair<Long, Long>, candidate: Pair<Long, Long>) =
    (current.first in candidate.first..candidate.second || current.second in candidate.first..candidate.second) ||
            (candidate.first in current.first..current.second || candidate.second in current.first..current.second)



