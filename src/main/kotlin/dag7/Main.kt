package dag7

import java.io.File

fun main() {
    val matrix: Array<CharArray> = File("data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    val width = matrix[0].count()
    val height = matrix.count()
    val startX = matrix[0].indices.filter({ matrix[0][it] == 'S' }).first()
    fun outside(pos: Pair<Int, Int>) = !(pos.first in 0..(width - 1) && pos.second in 0..(height - 1))

    fun del1(): Int {
        fun beam(acc: Int, pos: Pair<Int, Int>): Int {
            if (outside(pos)) return acc
            if (matrix[pos.second][pos.first] == '.') {
                matrix[pos.second][pos.first] = '|'
                return beam(acc, Pair(pos.first, pos.second + 1))
            } else if (matrix[pos.second][pos.first] == '^') {
                return beam(acc + 1, Pair(pos.first + 1, pos.second)) + beam(0, Pair(pos.first - 1, pos.second))
            }
            return acc
        }
        return beam(0, Pair(startX, 1))
    }

    fun del2(): Long {
        val beamCache = mutableMapOf<Pair<Int, Int>, Long>()
        fun beam(acc: Long, pos: Pair<Int, Int>): Long {
            if (outside(pos)) return acc
            if (beamCache[pos] != null) return beamCache[pos]!! + acc
            if (matrix[pos.second][pos.first] == '.') {
                beamCache[pos] = beam(0L, Pair(pos.first, pos.second + 1))
                return beamCache[pos]!! + acc
            } else if (matrix[pos.second][pos.first] == '^') {
                return beam(acc + 1, Pair(pos.first + 1, pos.second)) + beam(0L, Pair(pos.first - 1, pos.second))
            }
            return acc
        }
        return beam(1L, Pair(startX, 1))
    }

    println("del2: " + del2())
    println("del1: " + del1())
}
