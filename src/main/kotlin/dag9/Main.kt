package dag9

import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun norm(a: Int, b: Int) =
    Range(min(a, b), max(a, b))

fun area(p1: Pair<Int, Int>, p2: Pair<Int, Int>) =
    (abs(p1.first - p2.first) + 1L) * (abs(p1.second - p2.second) + 1L)

data class Range(val min: Int, val max: Int)
data class Area(val sum: Long, val x: Range, val y: Range)

fun main() {
    val points = File("data.txt").readLines().map { it.substringBefore(',').toInt() to it.substringAfter(',').toInt() }.sortedBy { it.second }
    val areas = mutableListOf<Area>()

    for (i in 0 until points.size - 2) {
        for (j in i + 1 until points.size) {
            areas += Area(area(points[i], points[j]), norm(points[i].first, points[j].first), norm(points[i].second, points[j].second))
        }
    }
    areas.sortByDescending { it.sum }
    val horizontalLinesMap = mutableMapOf<Int, Pair<Int, Int>>()
    for (i in 0 until points.size - 1 step 2)
        horizontalLinesMap[points[i].second] = Pair(min(points[i].first, points[i + 1].first), max(points[i].first, points[i + 1].first))

    fun del1(): Long {
        return areas.first().sum
    }

    fun del2(): Long {
        fun noneInside(area: Area): Boolean {
            for (i in 0 until points.size) {
                val (x, y) = points[i]
                if ((x == area.x.min || x == area.x.max) && (y == area.y.min || y == area.y.max)) continue
                if (x in area.x.min..area.x.max && y in area.y.min..area.y.max) {
                    return false
                }
            }
            return true
        }

        fun noneCrossing(area: Area): Boolean {
            for (y in area.y.min + 1..area.y.max - 1) {
                val line = horizontalLinesMap[y]
                if (line != null && line.first < area.x.min && line.second > area.x.min) return false
            }
            return true
        }

        val area = areas.firstOrNull { noneInside(it) && noneCrossing(it) }
        return area!!.sum
    }

    println("del1: " + del1());
    println("del2: " + del2());
}
