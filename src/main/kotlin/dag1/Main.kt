package dag1

import java.io.File

fun main() {
    print(File("d.txt").absolutePath)
    val list = File("data.txt").readLines().map { it[0] to it.drop(1).toInt() }

    var current = 50
    var sum = 0
    var sum2 = 0
    list.forEach {
        val runder = it.second / 100
        val rest = it.second % 100
        val last = current
        if (it.first == 'L') {
            current -= rest
            if (current <= 0) {
                if (current < 0)
                    current += 100
                if (last != 0)
                    sum2++
            }
        } else {
            current += rest
            if (current >= 100) {
                current -= 100
                sum2++
            }
        }
        sum2 += runder
        if (current == 0)
            sum++
    }
    println("del1 sum: $sum")
    println("del2 sum: $sum2")
}
