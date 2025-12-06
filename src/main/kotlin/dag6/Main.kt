package dag6

import java.io.File

fun main() {

    fun plus(x: Long, y: Long) = x + y
    fun mult(x: Long, y: Long) = x * y
    fun del1(): Long {
        val matrix: Array<Array<String>> = File("data.txt").readLines().map { it.trim().split(Regex("\\s+")).map { it.trim() }.toTypedArray() }.toTypedArray()
        val height = matrix.count()
        var total = 0L
        for (x in matrix[0].indices) {
            val op = if (matrix[height - 1][x] == "+") ::plus else ::mult
            val columnValues = (0 until height - 1).map { y -> matrix[y][x].toLong() }
            total += columnValues.reduce(op)
        }
        return total
    }

    fun del2(): Long {
        val lines = File("data.txt").readLines().toTypedArray()
        val height = lines.count()
        val opLine = lines[lines.size - 1]
        val opIndex = opLine.indices.filter { opLine[it] == '+' || opLine[it] == '*' }

        fun charAtPos(x: Int, y: Int): Char? {
            if (x >= lines[y].length || lines[y][x] == ' ') return null
            return lines[y][x]
        }

        var total = 0L

        for (colIndex in opIndex.indices) {
            val cellStartIndex = opIndex[colIndex]
            val op = if (opLine[cellStartIndex] == '+') ::plus else ::mult
            val cellEndIndex = if (colIndex == opIndex.size - 1) cellStartIndex + 4 else opIndex[colIndex + 1] - 2
            val columnValues = mutableListOf<Long>()
            for (internalCellIndex in cellEndIndex downTo cellStartIndex) {
                var num = ""
                for (y in 0..height - 2) {
                    val c = charAtPos(internalCellIndex, y)
                    if (c != null) num += c
                }
                if (!num.isEmpty()) {
                    columnValues += num.toLong()
                }
            }
            total += columnValues.reduce(op)
        }
        return total
    }

    println("del1: " + del1())
    println("del2: " + del2())
}

