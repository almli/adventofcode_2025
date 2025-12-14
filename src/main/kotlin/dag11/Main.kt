package dag11

import java.io.File


fun main() {
    fun del1(): Int {
        val nodes = File("data.txt").readLines().map { it.split(" ") }.map { it[0].dropLast(1) to it.drop(1).toSet() }.toMap()
        val visited = mutableSetOf<String>()
        var sum = 0
        fun solve(n: String) {
            if (n == "out") {
                sum++
                return
            }
            if (nodes[n] == null) return
            visited.add(n)
            nodes[n]?.filter { !visited.contains(it) }?.forEach { solve(it) }
            visited.remove(n)
        }
        solve("you")
        return sum
    }

    class NodeIo(val name: String, val input: MutableSet<String>, val output: MutableSet<String>) {
        override fun toString() = "$name in=${input.sorted()} out=${output.sorted()}"
    }

    fun sortTopological(nodes: Map<String, Set<String>>): List<NodeIo> {
        val nodeMap = mutableMapOf<String, NodeIo>()
        nodes.forEach { (from, tos) ->
            val fromNode = nodeMap.getOrPut(from) { NodeIo(from, mutableSetOf(), mutableSetOf()) }
            tos.forEach { to ->
                val toNode = nodeMap.getOrPut(to) { NodeIo(to, mutableSetOf(), mutableSetOf()) }
                fromNode.output += to
                toNode.input += from
            }
        }
        val nodeQueue = nodeMap.values.toMutableList()
        val topological = mutableListOf<NodeIo>()
        do {
            val current = nodeQueue.filter { it.input.size == 0 }
            require(current.isNotEmpty()) { "Syklus i grafen" }
            nodeQueue.removeAll(current)
            for (node in current) {
                for (output in node.output) {
                    nodeMap[output]!!.input.remove(node.name)
                }
            }
            topological.addAll(current)
        } while (nodeQueue.isNotEmpty())
        return topological
    }

    fun countFrom(start: String, topological: List<NodeIo>): Map<String, Long> {
        val idx = topological.indexOfFirst { it.name == start }
        if (idx < 0) return emptyMap()
        val dp = HashMap<String, Long>(topological.size * 2)
        dp[start] = 1L
        for (u in topological.subList(idx, topological.size)) {
            val w = dp[u.name] ?: 0L
            if (w == 0L) continue
            for (v in u.output) dp[v] = (dp[v] ?: 0L) + w
        }
        return dp
    }

    fun del2(): Long {
        val topological = sortTopological(File("data.txt").readLines().map { it.split(" ") }.map { it[0].dropLast(1) to it.drop(1).toSet() }.toMap())
        val fromSvr = countFrom("svr", topological)
        val fromFft = countFrom("fft", topological)
        val fromDac = countFrom("dac", topological)
        val pathsSvrToFft = fromSvr["fft"] ?: 0L
        val pathsFftToDac = fromFft["dac"] ?: 0L
        val pathsDacToOut = fromDac["out"] ?: 0L
        return pathsSvrToFft * pathsFftToDac * pathsDacToOut
    }
    println("del1: " + del1())
    println("del2: " + del2())
    // for generering av graph.png - for visualisering av grafen
    // val dot = toDot(File("data.txt").readLines())
    //java.io.File("graph.dot").writeText(dot)
    // last ned graphviz
    // f.eks brew install
    // kjør  /opt/homebrew/Cellar/graphviz/14.1.0/bin/dot  -Tpng graph.dot -o graph.png
}

/*
fun toDot(lines: List<String>): String {
    val edges = linkedSetOf<Pair<String, String>>()
    fun tok(s: String) = s.trim().takeIf { it.isNotEmpty() }

    for (raw in lines) {
        val line = raw.trim()
        if (line.isEmpty()) continue
        val parts = line.split(":", limit = 2)
        if (parts.size != 2) continue
        val from = tok(parts[0]) ?: continue
        val tos = parts[1].trim().split(Regex("\\s+")).mapNotNull(::tok)
        for (to in tos) edges += from to to
    }

    return buildString {
        appendLine("digraph G {")
        appendLine("  rankdir=LR;")
        appendLine("  node [shape=box];")
        for ((a, b) in edges) appendLine("  \"${a}\" -> \"${b}\";")
        appendLine("}")
    }
}
 */