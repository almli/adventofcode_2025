package dag10

import java.io.File
import kotlin.collections.plusAssign

val MAX_DEPTH = 20
val LARGE_NUM = 1000

fun main() {
    val lines = File("data-simple.txt").readLines()

    fun del1(): Int {
        data class Machine(val lamps: UInt, val buttons: List<UInt>) {
            var currentLampButtonSolutionDepth: Int = LARGE_NUM
        }
        fun parseLamps(pattern: String): UInt {
            var res = 0u
            pattern.forEachIndexed { i, c ->
                res = res or ((if (c == '.') 0u else 1u) shl (pattern.length - i - 1))
            }
            return res
        }
        fun parseButton(pattern: String, lampCount: Int): UInt {
            var res = 0u
            pattern.split(',').map { it.toInt() }.forEachIndexed { i, lampIndex ->
                res = res or (1u shl (lampCount - lampIndex - 1))
            }
            return res
        }

        val machines = mutableListOf<Machine>()
        lines.forEach {
            val parts = it.split(" ")
            val lampPattern = parts[0].drop(1).dropLast(1)
            machines += Machine(parseLamps(lampPattern), parts.drop(1).dropLast(1).map { parseButton(it.drop(1).dropLast(1), lampPattern.length) })
        }
        fun solve(depth: Int, currentButtonIndex: Int, currentLamps: UInt, machine: Machine): Int {
            val newDepth = depth + 1
            if (newDepth >= MAX_DEPTH || newDepth >= machine.currentLampButtonSolutionDepth)
                return LARGE_NUM
            if (currentLamps == machine.lamps) {
                return newDepth
            }
            for (bIndex in machine.buttons.indices) {
                if (bIndex != currentButtonIndex) {
                    val res = solve(newDepth, bIndex, currentLamps xor machine.buttons[bIndex], machine)
                    if (res < machine.currentLampButtonSolutionDepth) {
                        machine.currentLampButtonSolutionDepth = res
                    }
                }
            }
            return machine.currentLampButtonSolutionDepth
        }

        return machines.sumOf { solve(-1, -1, 0u, it) }
    }

    println("del1: " + del1())
}