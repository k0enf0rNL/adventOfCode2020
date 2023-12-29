package adventofcode.y2023

import adventofcode.utils.readInput
import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph

data class Connection(val component1: String, val component2: String)

fun main() {
    val input = readInput("2023/day25.txt")
    val connections: List<Connection> = input.flatMap { val line = Regex("(\\w+)").findAll(it)
        line.drop(1).map { Connection(line.first().value, it.value) } }
    val vertices: HashSet<String> = HashSet(input.flatMap { Regex("(\\w+)").findAll(it).map { it.value } })

    val graph = SimpleGraph<String, DefaultEdge>(DefaultEdge::class.java)
    vertices.forEach { graph.addVertex(it) }
    connections.forEach { graph.addEdge(it.component1, it.component2) }

    val oneSide = StoerWagnerMinimumCut(graph).minCut()
    println((vertices.size - oneSide.size) * oneSide.size)
}
