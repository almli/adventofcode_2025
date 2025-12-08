package dag8

import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt

data class Distance(
    val distance: Double,
    val a: Int,
    val b: Int
)

fun calcDistance(a: Triple<Int, Int, Int>, b: Triple<Int, Int, Int>) =
    sqrt(
        (a.first - b.first).toDouble().pow(2) +
                (a.second - b.second).toDouble().pow(2) +
                (a.third - b.third).toDouble().pow(2)
    )

fun main() {
    val points = File("data.txt")
        .readLines()
        .map { line ->
            val (a, b, c) = line.split(",")
            Triple(a.toInt(), b.toInt(), c.toInt())
        }

    val distances = mutableListOf<Distance>()
    for (i in 0 until points.size - 1)
        for (j in i + 1 until points.size)
            distances += Distance(calcDistance(points[i], points[j]), i, j)

    distances.sortBy { it.distance }

    val connections = points.map { mutableListOf<Int>() }
    fun isConnected(a: Int, b: Int, visited: MutableSet<Int>): Boolean {
        if (!visited.add(a)) return false
        return connections[a].any { it == b || isConnected(it, b, visited) }
    }

    fun del1(maxConnections:Int): Int {
        for (distance in distances.take(maxConnections)) {
            if (!isConnected(distance.a, distance.b, mutableSetOf())) {
                connections[distance.a].add(distance.b)
                connections[distance.b].add(distance.a)
            }
        }
        val visited = mutableSetOf<Int>()
        val networkSizes = mutableListOf<Int>()
        for (i in connections.indices) {
            val preSize = visited.size
            isConnected(i, -1, visited)
            networkSizes += visited.size - preSize
        }
        networkSizes.sortDescending()
        return networkSizes.take(3).reduce { acc, i -> acc * i }
    }

    fun del2(): Int {
        var lastConnection: Pair<Int, Int>? = null
        for (distance in distances) {
            if (!isConnected(distance.a, distance.b, mutableSetOf())) {
                connections[distance.b].add(distance.a)
                connections[distance.a].add(distance.b)
                lastConnection = Pair(distance.a, distance.b)
            }
        }
        return  points[lastConnection!!.first].first * points[lastConnection!!.second].first
    }

    println("del1: " + del1(1000))
    println("del2: " + del2())
}
