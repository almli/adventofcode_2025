package dag10

import java.io.File

fun main() {
    val lines = File("data.txt").readLines()

    fun del1(): Int {
        val MAX_DEPTH = 20
        val LARGE_NUM = 1000
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

    fun del2(): Long {
        data class Equation(val variables: Set<String>, val result: Long) {
            fun substitute(values: Map<String, Long>): Equation? {
                val intersect = variables.intersect(values.keys)
                if (intersect.isEmpty())
                    return this
                val sum = result - intersect.map { values[it]!! }.sum()
                if (sum < 0)
                    return null
                val newEq = Equation(variables - intersect, sum)
                if (newEq.variables.isEmpty() && newEq.result != 0L)
                    return null
                return newEq
            }
        }

        data class ValueRange(val variable: String, val from: Long, val to: Long) {}

        data class Machine(val equations: List<Equation>)

        fun parseTarget(counters: String) =
            counters.split(',').mapIndexed { i, c -> "t$i" to c.toLong() }.toMap()

        fun parseButton(button: String): Set<String> =
            button.split(',').map { "t${it.toInt()}" }.toSet()

        val machines = mutableListOf<Machine>()
        lines.forEach {
            val parts = it.split(" ").drop(1)
            val target = parseTarget(parts.last().drop(1).dropLast(1))

            val buttons = parts.dropLast(1).map { parseButton(it.drop(1).dropLast(1)) }
            val equations = mutableListOf<Equation>()
            for (t in target.entries) {
                val variables = mutableSetOf<String>()
                for (i in buttons.indices) {
                    if (buttons[i].contains(t.key)) {
                        variables.add(('a' + i).toString())
                    }
                }
                equations.add(Equation(variables, t.value))
            }
            machines += Machine(equations)
        }

        fun findSolved(equations: List<Equation>): Map<String, Long> {
            val solved = mutableMapOf<String, Long>()
            val sorted = equations.sortedByDescending { it.variables.size }
            for (i in sorted.indices - 2) {
                for (j in sorted.indices - 1) {
                    if (equations[i].variables.size - equations[j].variables.size == 1) {
                        val diff = equations[i].variables - equations[j].variables
                        if (diff.size == 1) {
                            solved[diff.first()] = equations[i].result - equations[j].result
                        }
                    }
                }
            }
            return solved
        }

        fun selectVar(equations: List<Equation>): ValueRange {
            val v = equations.sortedBy { it.variables.size }.first().variables.first()
            val subjects = equations.filter { it.variables.contains(v) }
            return ValueRange(v, 0L, subjects.maxOf { it.result })
        }

        fun solve(equations: List<Equation>, knownValues: Map<String, Long>): Long? {
            var eqs = equations
            val known = knownValues.toMutableMap()
            while (known.isNotEmpty()) {
                var changed = false
                val remaining = buildList {
                    for (eq in eqs) {
                        val orgSize = eq.variables.size
                        val newEq = eq.substitute(known) ?: return null
                        when (newEq.variables.size) {
                            0 -> changed = true
                            1 -> {
                                known[newEq.variables.first()] = newEq.result
                                changed = true
                            }
                            else -> {
                                add(newEq)
                                changed = changed || (orgSize != newEq.variables.size)
                            }
                        }
                    }
                }
                if (remaining.isEmpty()) return known.values.sum()
                if (!changed) break
                eqs = remaining
            }

            val solved = findSolved(eqs)
            if (solved.values.any { it < 0 }) return null
            known += solved

            val range = selectVar(eqs)
            return (range.from..range.to)
                .mapNotNull { v -> solve(eqs, known + (range.variable to v)) }
                .minOrNull()
        }

        var sum = 0L
        machines.forEachIndexed { i, m ->
            sum += solve(m.equations, findSolved(m.equations))!!
            println("$i løst")
        }
        return sum
    }

    println("del1: " + del1())
    println("del2: " + del2())
}

