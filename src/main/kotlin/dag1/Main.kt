package dag1

import java.io.File

fun main() {
    val list = File("data.txt").readLines().map { it[0] to it.drop(1).toInt() }
    var current = 50
    var sum = 0
    var sum2 = 0
    list.forEach {
        val laps = it.second / 100
        val rest = it.second % 100
        val last = current
        if (it.first == 'L') {
            if (rest > 0 && last != 0 && rest >= last)
                sum2++
            current = (current - rest).mod(100)
        } else {
            if (current + rest >= 100)
                sum2++
            current = (current + rest).mod(100)
        }
        sum2 += laps
        if (current == 0) sum++
    }
    println("del1 sum: $sum")
    println("del2 sum: $sum2")
}
