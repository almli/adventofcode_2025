package dag12

import java.io.File


fun main() {
    data class Board(val dimension: Pair<Int, Int>, val shapeCounts: List<Int>) {
        override fun toString() = "($dimension,$shapeCounts)"
    }

    data class Cell(val x: Int, val y: Int)

    data class Shape(val cells: Set<Cell>)

    fun parseBoard(s: String): Board =
        s.split(":").let { (d, c) ->
            d.split("x").map(String::toInt).let { (w, h) ->
                Board(w to h, c.trim().split(Regex("\\s+")).map(String::toInt))
            }
        }

    fun parseShape(lines: List<String>): Shape {
        val cells = lines.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, ch ->
                if (ch == '.') null else Cell(x, y)
            }
        }.toSet()

        return Shape(cells)
    }

    /**
     * Brente mye krutt på å forsøke å implementere en objektpakking algoritme (DLX med Algoritm x), men det ble etterhvert åpenbart at det var alt for mange muligheter i det store datasettet
     *
     * Prøvde for moro skyld å summere totalt antall ruter i pakkene og sammenligne med antall ruter i brettet..
     */
    fun del1(): Int {
        val parts = File("data.txt").readText(Charsets.UTF_8).split("\n\n")
        val boards = parts.takeLast(1).first().split("\n").map { parseBoard(it) }.toList()
        val shapes = parts.dropLast(1).map { parseShape(it.split("\n").drop(1)) }.toList()
        var sum = 0
        for (board in boards) {
            val bs = board.dimension.first * board.dimension.second
            var ss = 0
            shapes.forEachIndexed { index, shape ->
                ss += shape.cells.size * board.shapeCounts[index]
            }
            if (ss <= bs)
                sum++
        }
        return sum
    }
    println("del1: " + del1())
}
