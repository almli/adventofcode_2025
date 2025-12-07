package dag3

import java.io.File

fun main() {
    val list = File("data.txt").readLines()
    println("del1: " + solve(list, 2));
    println("del2: " + solve(list, 12));
}

fun solve(list: List<String>, cellCount: Int): Long =
    list.map { best(it, cellCount) }.reduce { acc, i -> acc + i }

fun best(bank: String, cellCount: Int): Long {
    val digits = mutableListOf<Char>()
    var lastPos = -1
    for (i in 1..cellCount) {
        lastPos = maxDigitPosition(bank, lastPos + 1, bank.length - (cellCount - i) - 1)
        digits.add(bank[lastPos])
    }
    return digits.joinToString("").toLong();
}

fun maxDigitPosition(s: String, start: Int, end: Int) =
    (start..end).maxBy { s[it] }!!