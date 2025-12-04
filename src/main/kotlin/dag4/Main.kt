package dag4

import java.io.File

fun main() {
    val matrix: Array<CharArray> = File("data.txt").readLines().map { it.toCharArray() }.toTypedArray()
    val width = matrix[0].count()
    val height = matrix.count()

    fun posRoll(x: Int, y: Int): Int {
        if (x < 0 || x >= width || y < 0 || y >= height) return 0
        return if (matrix[y][x] == '@') 1 else 0
    }

    fun posSum(x: Int, y: Int) =
        posRoll(x + 1, y) + posRoll(x + 1, y + 1) + posRoll(x + 1, y - 1) + posRoll(x - 1, y) + posRoll(x - 1, y + 1) + posRoll(x - 1, y - 1) + posRoll(x, y + 1) + posRoll(x, y - 1)

    fun del1() {
        var sum = 0
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (matrix[y][x] == '@' && posSum(x, y) < 4) sum++
            }
        }
        println("del1: " + sum);
    }

    fun del2() {
        var sum = 0
        do {
            var lastSum = sum;
            for (x in 0 until width) {
                for (y in 0 until height) {
                    if (matrix[y][x] == '@' && posSum(x, y) < 4) {
                        sum++
                        matrix[y][x] = 'x'
                    }
                }
            }
        } while (lastSum != sum);
        println("del2: " + sum);
    }
    //del1()
    del2()
}

