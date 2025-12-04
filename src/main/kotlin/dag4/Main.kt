package dag4

import java.io.File

fun main() {
    val matrix: Array<CharArray> = File("data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    val width = matrix[0].count()
    val height = matrix.count()

    fun posCellCount(x: Int, y: Int) =
        if (x < 0 || x >= width || y < 0 || y >= height || matrix[y][x] != '@') 0 else 1

    fun posSum(x: Int, y: Int) =
        posCellCount(x + 1, y) + posCellCount(x + 1, y + 1) + posCellCount(x + 1, y - 1) + posCellCount(x - 1, y) + posCellCount(x - 1, y + 1) + posCellCount(x - 1, y - 1) + posCellCount(x, y + 1) + posCellCount(x, y - 1)

    fun del1(): Long {
        var sum = 0L
        for (x in 0 until width)
            for (y in 0 until height)
                if (matrix[y][x] == '@' && posSum(x, y) < 4) sum++
        return sum
    }

    fun del2(): Long {
        var sum = 0L
        do {
            var changed = false;
            for (x in 0 until width) {
                for (y in 0 until height) {
                    if (matrix[y][x] == '@' && posSum(x, y) < 4) {
                        sum++
                        matrix[y][x] = 'x'
                        changed = true
                    }
                }
            }
        } while (changed);
        return sum
    }

    println("del1: " + del1());
    println("del2: " + del2());
}
